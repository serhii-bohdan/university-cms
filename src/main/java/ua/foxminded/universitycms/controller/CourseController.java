package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.*;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying courses.
 * It maps GET requests to the {@code /ui/v1/courses} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/courses")
public class CourseController {

    /**
     * The redirect URL format used to redirect the user to the course students page.
     */
    private static final String COURSE_STUDENTS_REDIRECT_URL = "redirect:/ui/v1/courses/my/{courseId}/students";

    /**
     * Redirect URL to the user's courses page.
     */
    private static final String USER_COURSES_REDIRECT_URL = "redirect:/ui/v1/courses/my";

    /**
     * Redirect URL to a specific course page for the user.
     */
    private static final String USER_SPECIFIC_COURSE_REDIRECT_URL = "redirect:/ui/v1/courses/my/%s";

    /**
     * The {@link CourseService} used to interact with course data.
     */
    private final CourseService courseService;

    /**
     * Retrieves a page of course data for display and populates the model with necessary attributes.
     * <p>
     * This method handles GET requests to the endpoint responsible for displaying a paginated list of courses.
     * It utilizes the {@code courseService} to retrieve course data based on a provided keyword (optional)
     * and pagination information.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional search keyword for filtering courses by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return the logical view name {@code courses/all-courses} representing the course list template
     */
    @GetMapping
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithCourses(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                     @PageableDefault Pageable pageable) {
        Page<CourseDto> coursesPage = StringUtils.isBlank(keyword)
            ? courseService.getAllCoursesInPage(pageable)
            : courseService.getCourseByNameInPage(keyword, pageable);

        model.addAttribute(ModelAttributeNames.COURSES_ALL_NAMES_ATTRIBUTE, getCoursesNames(courseService.getAll()))
            .addAttribute(ModelAttributeNames.COURSES_ATTRIBUTE, coursesPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, coursesPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, coursesPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_COURSES_PAGE;
    }

    /**
     * Retrieves a page of courses for the currently authenticated user, optionally filtered by a keyword.
     * <p>
     * This method handles GET requests to the {@code /my} endpoint under the {@code /ui/v1/courses} path. It determines
     * the user's role (either teacher or student) from the {@code customUserDetails} and fetches the courses accordingly,
     * filtered by the provided {@code keyword} if present.
     * <p>
     * It adds the retrieved courses, a list of course names, the search keyword (if any), and an error flag (if an
     * error occurred during retrieval) to the model.
     *
     * @param model             the Spring MVC Model object used to store data for the view
     * @param customUserDetails the details of the currently authenticated user
     * @param keyword           an optional search keyword for filtering courses by name (can be blank)
     * @return the logical view name {@code courses/user-courses} representing the user's course list template
     */
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithCoursesForUser(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestParam(name = "keyword", required = false) String keyword) {
        boolean isKeywordNotCorrect = StringUtils.isBlank(keyword);
        Long userId = customUserDetails.getId();
        RoleName userRole = customUserDetails.getRoleName();
        List<CourseDto> userCourses = new ArrayList<>();

        if (RoleName.TEACHER.equals(userRole)) {
            userCourses = isKeywordNotCorrect
                ? courseService.getTeacherCourses(userId)
                : courseService.getTeacherCourseByCourseName(userId, keyword);
        } else if (RoleName.STUDENT.equals(userRole)) {
            userCourses = isKeywordNotCorrect
                ? courseService.getStudentCourses(userId)
                : courseService.getStudentCourseByCourseName(userId, keyword);
        }

        model.addAttribute(ModelAttributeNames.USER_COURSES_NAMES_ATTRIBUTE, getCoursesNames(userCourses))
            .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, userCourses)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.USER_COURSES;
    }

    /**
     * Renders a page containing details for a specific course.
     * This method handles GET requests to the path {@code /ui/v1/courses/{courseId}}, where {@code courseId} is the
     * unique identifier of the course.
     * <p>
     * If the course is found, it adds the course details and author's full name to the model. Otherwise, it sets an
     * error flag.
     *
     * @param model    the Spring MVC {@link Model} object used to pass data to the view
     * @param courseId the unique identifier of the course to retrieve
     * @return the logical name of the view template {@code courses/course}
     */
    @GetMapping("/my/{courseId}")
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithSpecificCourse(Model model, @PathVariable("courseId") long courseId) {
        CourseDto course = courseService.getById(courseId);
        model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course)
            .addAttribute(ModelAttributeNames.TOPICS_ATTRIBUTE, getSortedTopicsByTopicOrder(course.getTopics()));

        return ViewNames.SPECIFIC_COURSE;
    }

    /**
     * Retrieves the creation form for a new course.
     * <p>
     * This method handles GET requests to the `{@code /ui/v1/courses/my/new}` endpoint. If so, it creates a new
     * {@code CourseDto} object with the user's ID set as the author and adds it to the model for the creation form.
     *
     * @param model             the Spring MVC Model object used to store data for the view
     * @param customUserDetails details of the authenticated user
     * @return the logical view name "courses/creation-form" representing the course creation template
     */
    @GetMapping("/my/new")
    @PreAuthorize("hasAuthority('COURSES_CREATE')")
    public String getCreationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CourseDto course = CourseDto.builder()
            .authorId(customUserDetails.getId())
            .build();

        model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course);
        return ViewNames.COURSE_CREATION_FORM;
    }

    /**
     * Handles the creation of a new course.
     * <p>
     * This method processes the course creation request by first validating the input using the {@link CourseDto}.
     * If validation fails, it returns the course creation form with error messages. If the input is valid, it
     * proceeds to save the new course using the {@link CourseService}. Upon successful creation, it redirects to
     * the user's courses page.
     *
     * @param course        the {@link CourseDto} containing the course details to be created
     * @param bindingResult the result of validating the {@link CourseDto}
     * @return a redirection URL to the user's courses page or the course creation form if validation fails
     */
    @PostMapping("/my/create")
    @PreAuthorize("hasAuthority('COURSES_CREATE')")
    public String performCourseCreation(@ModelAttribute("course") @Valid CourseDto course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.COURSE_CREATION_FORM;
        }

        courseService.save(course);
        return USER_COURSES_REDIRECT_URL;
    }

    /**
     * Retrieves the update form for an existing course.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/courses/my/{courseId}/edit} endpoint. It attempts to
     * retrieve the course data with the provided course ID using the {@code courseService}. If the course is found,
     * it adds the course data to the model and returns the update form view name.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param courseId the ID of the course to be updated
     * @return the logical view name {@code courses/update-form} representing the course update template
     */
    @GetMapping("/my/{courseId}/edit")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("courseId") long courseId) {
        model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, courseService.getById(courseId));
        return ViewNames.COURSE_UPDATE_FORM;
    }

    /**
     * Handles the update of an existing course.
     * <p>
     * This method processes the course update request by first validating the input using the {@link CourseDto}.
     * If validation fails, it returns the course update form with error messages. If the input is valid, it
     * proceeds to update the existing course using the {@link CourseService}.
     *
     * @param course        the {@link CourseDto} containing the updated course details
     * @param bindingResult the result of validating the {@link CourseDto}
     * @return a redirection URL to the specific course page or the course update form if validation fails
     */
    @PutMapping("/my/update")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performCourseUpdate(@ModelAttribute("course") @Valid CourseDto course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.COURSE_UPDATE_FORM;
        }

        courseService.update(course);
        return String.format(USER_SPECIFIC_COURSE_REDIRECT_URL, course.getId());
    }

    /**
     * Deletes a specified course.
     * <p>
     * This method handles DELETE requests to the {@code /ui/v1/courses/my/{courseId}/delete} endpoint. It attempts to
     * delete the course with the provided {@code courseId} using the {@code courseService}. If successful, it redirects
     * the user to the "my courses" page.
     *
     * @param courseId the ID of the course to be deleted
     * @return a redirect URL on success or throws an exception
     */
    @DeleteMapping("/my/{courseId}/delete")
    @PreAuthorize("hasAuthority('COURSES_DELETE')")
    public String performCourseDeletion(@PathVariable("courseId") long courseId) {
        courseService.deleteById(courseId);
        return USER_COURSES_REDIRECT_URL;
    }

    /**
     * Retrieves a list of students enrolled in a specified course.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/courses/my/{courseId}/students} endpoint. It retrieves the
     * course with the provided {@code courseId} using the {@code courseService}. If the course is found, it filters the
     * course students based on an optional keyword and adds the course and student information to the model for display.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param courseId the ID of the course
     * @param keyword  an optional search keyword for filtering students by email (can be blank)
     * @return the logical view name {@code courses/course-students} representing the course students list template
     */
    @GetMapping("/my/{courseId}/students")
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getCourseStudents(Model model, @PathVariable("courseId") long courseId,
                                    @RequestParam(value = "keyword", required = false) String keyword) {
        CourseDto course = courseService.getById(courseId);
        Set<StudentDto> courseStudents = StringUtils.isBlank(keyword)
            ? course.getStudents()
            : findStudentByEmail(course.getStudents(), keyword);

        model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course)
            .addAttribute(ModelAttributeNames.COURSE_STUDENTS_ATTRIBUTE, courseStudents)
            .addAttribute(ModelAttributeNames.STUDENT_EMAILS_ATTRIBUTE, getStudentEmails(course.getStudents()))
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.COURSE_STUDENTS;
    }

    /**
     * Deducts a student from a specified course.
     * <p>
     * This method handles DELETE requests to the {@code /ui/v1/courses/my/{courseId}/students/{studentId}/deduct} endpoint.
     * It attempts to deduct the student with the provided {@code studentId} from the course with the provided
     * {@code courseId} using the {@code courseService}.
     *
     * @param courseId  the ID of the course
     * @param studentId the ID of the student to be deducted
     * @return a redirect URL to the course students page
     */
    @DeleteMapping("/my/{courseId}/students/{studentId}/deduct")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performDeductionStudentFromCourse(@PathVariable("courseId") long courseId, @PathVariable("studentId") long studentId) {
        courseService.deductStudentFromCourse(courseId, studentId);
        return COURSE_STUDENTS_REDIRECT_URL;
    }

    /**
     * Enrolls a student in a specified course.
     * <p>
     * This method handles POST requests to the {@code /ui/v1/courses/my/{courseId}/students/{studentId}/enroll} endpoint.
     * It attempts to enroll the student with the provided {@code studentId} in the course with the provided {@code courseId}
     * using the {@code courseService}.
     *
     * @param courseId  the ID of the course
     * @param studentId the ID of the student to be enrolled
     * @return a redirect URL to the course students page
     */
    @PostMapping("/my/{courseId}/students/{studentId}/enroll")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performEnrollingStudentToCourse(@PathVariable("courseId") long courseId, @PathVariable("studentId") long studentId) {
        courseService.enrollStudentInCourse(courseId, studentId);
        return COURSE_STUDENTS_REDIRECT_URL;
    }

    /**
     * Enrolls all students from a specific group in a course.
     * <p>
     * This method handles POST requests to the {@code /ui/v1/courses/my/{courseId}/group/{groupId}/enroll} endpoint.
     * It attempts to enroll all students from the group with the provided {@code groupId} in the course with the
     * provided {@code courseId} using the {@code courseService}.
     *
     * @param courseId the ID of the course
     * @param groupId  the ID of the group to be enrolled
     * @return a redirect URL to the course students page
     */
    @PostMapping("/my/{courseId}/group/{groupId}/enroll")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performEnrollingGroupInCourse(@PathVariable("courseId") long courseId, @PathVariable("groupId") long groupId) {
        courseService.enrollAllStudentsFromGroupInCourse(courseId, groupId);
        return COURSE_STUDENTS_REDIRECT_URL;
    }

    private List<String> getCoursesNames(Collection<CourseDto> courses) {
        return courses.stream()
            .map(CourseDto::getCourseName)
            .toList();
    }

    private List<TopicDto> getSortedTopicsByTopicOrder(Set<TopicDto> topics) {
        return topics.stream()
            .sorted(Comparator.comparing(TopicDto::getTopicOrder))
            .toList();
    }

    private List<String> getStudentEmails(Collection<StudentDto> students) {
        return students.stream()
            .map(UserDto::getEmail)
            .toList();
    }

    private Set<StudentDto> findStudentByEmail(Collection<StudentDto> students, String email) {
        return students.stream()
            .filter(s -> s.getEmail().equals(email))
            .collect(Collectors.toSet());
    }

}
