package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.foxminded.universitycms.dto.*;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.exception.ValidationException;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying courses.
 * It maps GET requests to the {@code ui/v1/courses} path.
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
     * It utilizes the `courseService` to retrieve course data based on a provided keyword (optional)
     * and pagination information.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional search keyword for filtering courses by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return the logical view name "courses/all-courses" representing the course list template
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
     * This method handles GET requests to the "/my" endpoint under the "/ui/v1/courses" path. It determines the user's role
     * (either teacher or student) from the `customUserDetails` and fetches the courses accordingly, filtered by the provided `keyword` if present.
     * <p>
     * It adds the retrieved courses, a list of course names, the search keyword (if any), and an error flag (if an error occurred during retrieval) to the model.
     *
     * @param model             the Spring MVC Model object used to store data for the view
     * @param customUserDetails the details of the currently authenticated user
     * @param keyword           an optional search keyword for filtering courses by name (can be blank)
     * @return the logical view name "courses/user-courses" representing the user's course list template
     */
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithCoursesForUser(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestParam(name = "keyword", required = false) String keyword) {
        List<CourseDto> userCourses = new ArrayList<>();

        try {
            boolean isKeywordNotCorrect = StringUtils.isBlank(keyword);
            Long userId = customUserDetails.getId();
            RoleName userRole = customUserDetails.getRoleName();

            if (RoleName.TEACHER.equals(userRole)) {
                userCourses = isKeywordNotCorrect
                    ? courseService.getTeacherCourses(userId)
                    : courseService.getTeacherCourseByCourseName(userId, keyword);
            } else if (RoleName.STUDENT.equals(userRole)) {
                userCourses = isKeywordNotCorrect
                    ? courseService.getStudentCourses(userId)
                    : courseService.getStudentCourseByCourseName(userId, keyword);
            }
        } catch (UserNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(),
                "Unable to display courses, user information is missing.");
        }

        model.addAttribute(ModelAttributeNames.USER_COURSES_NAMES_ATTRIBUTE, getCoursesNames(userCourses))
            .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, userCourses)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.USER_COURSES;
    }

    /**
     * Renders a page containing details for a specific course.
     * This method handles GET requests to the path `/ui/v1/courses/{courseId}`, where `{courseId}` is the unique identifier of the course.
     * <p>
     * If the course is found, it adds the course details and author's full name to the model. Otherwise, it sets an error flag.
     *
     * @param model    the Spring MVC {@link Model} object used to pass data to the view
     * @param courseId the unique identifier of the course to retrieve
     * @return the logical name of the view template ("courses/course")
     */
    @GetMapping("/my/{courseId}")
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithSpecificCourse(Model model, @PathVariable("courseId") long courseId) {
        Optional<CourseDto> optional = courseService.getById(courseId);

        if (optional.isPresent()) {
            CourseDto course = optional.get();
            model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course)
                .addAttribute(ModelAttributeNames.TOPICS_ATTRIBUTE, getSortedTopicsByTopicOrder(course.getTopics()));
            return ViewNames.SPECIFIC_COURSE;
        }

        throw new CustomHttpException(HttpStatus.NOT_FOUND, "Course not found.");
    }

    /**
     * Retrieves the creation form for a new course.
     * <p>
     * This method handles GET requests to the `/my/new` endpoint. It checks if the authenticated user
     * has the "TEACHER" role. If so, it creates a new `CourseDto` object with the user's ID set as the author
     * and adds it to the model for the creation form. Otherwise, it throws a `CustomHttpException` with a bad request status.
     *
     * @param model             the Spring MVC Model object used to store data for the view
     * @param customUserDetails details of the authenticated user
     * @return the logical view name "courses/creation-form" representing the course creation template
     * @throws CustomHttpException if the user does not have the "TEACHER" role
     */
    @GetMapping("/my/new")
    @PreAuthorize("hasAuthority('COURSES_CREATE')")
    public String getCreationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (RoleName.TEACHER.equals(customUserDetails.getRoleName())) {
            CourseDto course = CourseDto.builder()
                .authorId(customUserDetails.getId())
                .build();

            model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course);
            return ViewNames.COURSE_CREATION_FORM;
        }

        throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Wrong course author.");
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
     * This method handles GET requests to the `/my/{courseId}/edit` endpoint. It attempts to retrieve the course data with the
     * provided course ID using the `courseService`. If the course is found, it adds the course data to the model and returns the
     * update form view name. Otherwise, it throws a `CustomHttpException` with a not found status.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param courseId the ID of the course to be updated
     * @return the logical view name "courses/update-form" representing the course update template
     * @throws CustomHttpException if the course with the provided ID is not found
     */
    @GetMapping("/my/{courseId}/edit")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("courseId") long courseId) {
        Optional<CourseDto> optional = courseService.getById(courseId);

        if (optional.isPresent()) {
            model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, optional.get());
            return ViewNames.COURSE_UPDATE_FORM;
        }

        throw new CustomHttpException(HttpStatus.NOT_FOUND, "Editing failed. Course not found.");
    }

    /**
     * Handles the update of an existing course.
     * <p>
     * This method processes the course update request by first validating the input using the {@link CourseDto}.
     * If validation fails, it returns the course update form with error messages. If the input is valid, it
     * proceeds to update the existing course using the {@link CourseService}. Upon successful update, it
     * redirects to the page of the updated course. If the course is not found, a {@link CustomHttpException}
     * is thrown with a relevant error message.
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

        try {
            courseService.update(course);
            return String.format(USER_SPECIFIC_COURSE_REDIRECT_URL, course.getId());
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Update failed. Course not found.");
        }
    }

    /**
     * Deletes a specified course.
     * <p>
     * This method handles DELETE requests to the `/my/{courseId}/delete` endpoint. It attempts to delete the course
     * with the provided `courseId` using the `courseService`. If successful, it redirects the user to the "my courses" page.
     * If the course is not found, it throws a `CustomHttpException` with a not found status.
     *
     * @param courseId the ID of the course to be deleted
     * @return a redirect URL on success or throws an exception
     * @throws CustomHttpException if the course with the provided ID is not found
     */
    @DeleteMapping("/my/{courseId}/delete")
    @PreAuthorize("hasAuthority('COURSES_DELETE')")
    public String performCourseDeletion(@PathVariable("courseId") long courseId) {
        try {
            courseService.deleteById(courseId);
            return USER_COURSES_REDIRECT_URL;
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Deletion failed. Course not found.");
        }
    }

    /**
     * Retrieves a list of students enrolled in a specified course.
     * <p>
     * This method handles GET requests to the `/my/{courseId}/students` endpoint. It retrieves the course with the provided
     * `courseId` using the `courseService`. If the course is found, it filters the course students based on an optional keyword
     * and adds the course and student information to the model for display. Otherwise, it throws a `CustomHttpException` with a
     * not found status.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param courseId the ID of the course
     * @param keyword  an optional search keyword for filtering students by email (can be blank)
     * @return the logical view name "courses/course-students" representing the course students list template
     * @throws CustomHttpException if the course with the provided ID is not found
     */
    @GetMapping("/my/{courseId}/students")
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getCourseStudents(Model model, @PathVariable("courseId") long courseId,
                                    @RequestParam(value = "keyword", required = false) String keyword) {
        Optional<CourseDto> optional = courseService.getById(courseId);

        if (optional.isPresent()) {
            CourseDto course = optional.get();
            Set<StudentDto> courseStudents = StringUtils.isBlank(keyword)
                ? course.getStudents()
                : findStudentByEmail(course.getStudents(), keyword);

            model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course)
                .addAttribute(ModelAttributeNames.COURSE_STUDENTS_ATTRIBUTE, courseStudents)
                .addAttribute(ModelAttributeNames.STUDENT_EMAILS_ATTRIBUTE, getStudentEmails(course.getStudents()))
                .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

            return ViewNames.COURSE_STUDENTS;
        }

        throw new CustomHttpException(HttpStatus.NOT_FOUND,
            "This course was not found. Cannot view optional students.");
    }

    /**
     * Deducts a student from a specified course.
     * <p>
     * This method handles DELETE requests to the `/my/{courseId}/students/{studentId}/deduct` endpoint. It attempts to
     * deduct the student with the provided `studentId` from the course with the provided `courseId` using the `courseService`.
     * If the student is not enrolled in the course, it throws a `ValidationException`. If the course or student is not found,
     * it throws a `CustomHttpException` with a not found status.
     *
     * @param courseId           the ID of the course
     * @param studentId          the ID of the student to be deducted
     * @param redirectAttributes the redirect attributes to add a flash message if necessary
     * @return a redirect URL to the course students page
     * @throws CustomHttpException if the course or student is not found
     * @throws ValidationException if the student is not enrolled in the course
     */
    @DeleteMapping("/my/{courseId}/students/{studentId}/deduct")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performDeductionStudentFromCourse(@PathVariable("courseId") long courseId, @PathVariable("studentId") long studentId,
                                                    RedirectAttributes redirectAttributes) {
        try {
            courseService.deductStudentFromCourse(courseId, studentId);
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR_MESSAGE_ATTRIBUTE, """
                An error occurred while trying to deduct a student from the course. The student
                is not enrolled in this course.""");
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), """
                The requested resource was not found. Please check the provided information and
                try again.""");
        }

        return COURSE_STUDENTS_REDIRECT_URL;
    }

    /**
     * Enrolls a student in a specified course.
     * <p>
     * This method handles POST requests to the `/my/{courseId}/students/{studentId}/enroll` endpoint. It attempts to
     * enroll the student with the provided `studentId` in the course with the provided `courseId` using the `courseService`.
     * If the student is already enrolled in the course, it throws a `ValidationException`. If the course or student is not found,
     * it throws a `CustomHttpException` with a not found status.
     *
     * @param courseId           the ID of the course
     * @param studentId          the ID of the student to be enrolled
     * @param redirectAttributes the redirect attributes to add a flash message if necessary
     * @return a redirect URL to the course students page
     * @throws CustomHttpException if the course or student is not found
     * @throws ValidationException if the student is already enrolled in the course
     */
    @PostMapping("/my/{courseId}/students/{studentId}/enroll")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performEnrollingStudentToCourse(@PathVariable("courseId") long courseId, @PathVariable("studentId") long studentId,
                                                  RedirectAttributes redirectAttributes) {
        try {
            courseService.enrollStudentInCourse(courseId, studentId);
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR_MESSAGE_ATTRIBUTE, """
                An error occurred while enrolling a student in the course. The student is already
                enrolled in this course.""");
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), """
                The requested resource was not found. Please check the provided information
                and try again.""");
        }

        return COURSE_STUDENTS_REDIRECT_URL;
    }

    /**
     * Enrolls all students from a specific group in a course.
     * <p>
     * This method handles POST requests to the `/my/{courseId}/group/{groupId}/enroll` endpoint. It attempts to enroll all
     * students from the group with the provided `groupId` in the course with the provided `courseId` using the `courseService`.
     * If the course is not found, it throws a `CustomHttpException` with a not found status.
     *
     * @param courseId the ID of the course
     * @param groupId  the ID of the group to be enrolled
     * @return a redirect URL to the course students page
     * @throws CustomHttpException if the course or group is not found
     */
    @PostMapping("/my/{courseId}/group/{groupId}/enroll")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performEnrollingGroupInCourse(@PathVariable("courseId") long courseId, @PathVariable("groupId") long groupId) {
        try {
            courseService.enrollAllStudentsFromGroupInCourse(courseId, groupId);
            return COURSE_STUDENTS_REDIRECT_URL;
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), """
                The requested resource was not found. Please check the provided information
                and try again.""");
        }
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
