package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;
import java.util.*;

/**
 * Spring MVC Controller for handling course-related requests under the {@code /ui/v1/courses} path.
 * Manages operations such as displaying course lists, creating, updating, deleting courses, and
 * managing student enrollment. Uses {@link CourseService} for business logic. Annotated with
 * {@code @Controller} and {@code @RequiredArgsConstructor}.
 *
 * @author Serhii Bohdan
 * @see CourseService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/courses"})
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
     * Service for interacting with course data and performing business logic operations.
     */
    private final CourseService courseService;

    /**
     * Displays a paginated list of courses, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/courses}. Retrieves courses via
     * {@link CourseService#findCourses} and adds pagination data and names to the model.
     * Requires {@code COURSES_READ} authority.
     *
     * @param model    the {@link Model} to store view data
     * @param keyword  optional keyword to filter courses by name; may be blank
     * @param pageable pagination info from {@link PageableDefault}
     * @return view name {@link ViewNames#ALL_COURSES_PAGE} for the course list
     */
    @GetMapping
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithCourses(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                     @PageableDefault Pageable pageable) {
        Page<CourseDto> coursesPage = courseService.findCourses(pageable, keyword);

        model.addAttribute(ModelAttributeNames.COURSES_ALL_NAMES_ATTRIBUTE, courseService.extractCourseNames(courseService.getAll()))
            .addAttribute(ModelAttributeNames.COURSES_ATTRIBUTE, coursesPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, coursesPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, coursesPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_COURSES_PAGE;
    }

    /**
     * Displays a list of courses for the authenticated user, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/courses/my}. Retrieves user courses via
     * {@link CourseService#getUserCourses} and filters them. Requires {@code COURSES_READ}.
     *
     * @param model             the {@link Model} to store view data
     * @param customUserDetails authenticated user details from {@link CustomUserDetails}
     * @param keyword           optional keyword to filter courses by name; may be blank
     * @return view name {@link ViewNames#USER_COURSES} for the user's course list
     */
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithCoursesForUser(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestParam(name = "keyword", required = false) String keyword) {
        List<CourseDto> userCourses = courseService.getUserCourses(customUserDetails);
        List<CourseDto> filteredCourses = courseService.filterCoursesByName(userCourses, keyword);

        model.addAttribute(ModelAttributeNames.USER_COURSES_NAMES_ATTRIBUTE, courseService.extractCourseNames(userCourses))
            .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, filteredCourses)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.USER_COURSES;
    }

    /**
     * Displays details of a specific course for the user.
     * Handles GET requests to {@code /ui/v1/courses/my/{courseId}}. Retrieves course data via
     * {@link CourseService#getById} and adds it with sorted topics to the model. Requires
     * {@code COURSES_READ}.
     *
     * @param model    the {@link Model} to store view data
     * @param courseId the ID of the course to display
     * @return view name {@link ViewNames#SPECIFIC_COURSE} for course details
     */
    @GetMapping("/my/{courseId}")
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithSpecificCourse(Model model, @PathVariable("courseId") long courseId) {
        CourseDto course = courseService.getById(courseId);
        model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course)
            .addAttribute(ModelAttributeNames.TOPICS_ATTRIBUTE, sortTopicsByTopicOrder(course.getTopics()));
        return ViewNames.SPECIFIC_COURSE;
    }

    /**
     * Displays the form for creating a new course.
     * Handles GET requests to {@code /ui/v1/courses/my/new}. Prepares a {@link CourseDto} with the
     * user's ID as author. Requires {@code COURSES_CREATE} authority.
     *
     * @param model             the {@link Model} to store form data
     * @param customUserDetails authenticated user details from {@link CustomUserDetails}
     * @return view name {@link ViewNames#COURSE_CREATION_FORM} for the creation form
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
     * Processes the submission of the course creation form.
     * Handles POST requests to {@code /ui/v1/courses/my/create}. Validates {@link CourseDto} and
     * saves the course via {@link CourseService#save}. Returns the form on errors. Requires
     * {@code COURSES_CREATE}.
     *
     * @param course        the {@link CourseDto} with form data
     * @param bindingResult validation results for the DTO
     * @return redirect to {@link #USER_COURSES_REDIRECT_URL} or form view on errors
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
     * Displays the form for updating a course's information.
     * Handles GET requests to {@code /ui/v1/courses/my/{courseId}/edit}. Retrieves course data via
     * {@link CourseService#getById} for the form. Requires {@code COURSES_UPDATE}.
     *
     * @param model    the {@link Model} to store form data
     * @param courseId the ID of the course to update
     * @return view name {@link ViewNames#COURSE_UPDATE_FORM} for the update form
     */
    @GetMapping("/my/{courseId}/edit")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("courseId") long courseId) {
        model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, courseService.getById(courseId));
        return ViewNames.COURSE_UPDATE_FORM;
    }

    /**
     * Processes the update of a course's information.
     * Handles PUT requests to {@code /ui/v1/courses/my/update}. Validates {@link CourseDto} and
     * updates via {@link CourseService#update}. Returns form on errors. Requires
     * {@code COURSES_UPDATE}.
     *
     * @param course        the {@link CourseDto} with updated data
     * @param bindingResult validation results for the DTO
     * @return redirect to course page or form view on errors
     */
    @PutMapping("/my/update")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performCourseUpdate(@ModelAttribute("course") @Valid CourseDto course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.COURSE_UPDATE_FORM;
        }

        courseService.update(course);
        return USER_SPECIFIC_COURSE_REDIRECT_URL.formatted(course.getId());
    }

    /**
     * Deletes a course from the system.
     * Handles DELETE requests to {@code /ui/v1/courses/my/{courseId}/delete}. Deletes course via
     * {@link CourseService#deleteById} and redirects. Requires {@code COURSES_DELETE}.
     *
     * @param courseId the ID of the course to delete
     * @return redirect to {@link #USER_COURSES_REDIRECT_URL}
     */
    @DeleteMapping("/my/{courseId}/delete")
    @PreAuthorize("hasAuthority('COURSES_DELETE')")
    public String performCourseDeletion(@PathVariable("courseId") long courseId) {
        courseService.deleteById(courseId);
        return USER_COURSES_REDIRECT_URL;
    }

    /**
     * Displays students enrolled in a specific course, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/courses/my/{courseId}/students}. Retrieves course data
     * via {@link CourseService#getById} and filters students. Requires {@code STUDENTS_READ}.
     *
     * @param model    the {@link Model} to store view data
     * @param courseId the ID of the course to display students for
     * @param keyword  optional keyword to filter students by email; may be blank
     * @return view name {@link ViewNames#COURSE_STUDENTS} for the student list
     */
    @GetMapping("/my/{courseId}/students")
    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    public String getCourseStudents(Model model, @PathVariable("courseId") long courseId,
                                    @RequestParam(value = "keyword", required = false) String keyword) {
        CourseDto course = courseService.getById(courseId);
        Set<StudentDto> allStudents = course.getStudents();
        Set<StudentDto> displayedStudents = courseService.filterCourseStudentsByEmail(allStudents, keyword);

        model.addAttribute(ModelAttributeNames.COURSE_ATTRIBUTE, course)
            .addAttribute(ModelAttributeNames.COURSE_STUDENTS_ATTRIBUTE, displayedStudents)
            .addAttribute(ModelAttributeNames.STUDENT_EMAILS_ATTRIBUTE, extractStudentEmails(allStudents))
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.COURSE_STUDENTS;
    }

    /**
     * Deducts a student from a specific course.
     * Handles DELETE requests to {@code /ui/v1/courses/my/{courseId}/students/{studentId}/deduct}.
     * Deducts student via {@link CourseService#deductStudentFromCourse}. Requires
     * {@code COURSES_UPDATE}.
     *
     * @param courseId  the ID of the course
     * @param studentId the ID of the student to deduct
     * @return redirect to {@link #COURSE_STUDENTS_REDIRECT_URL}
     */
    @DeleteMapping("/my/{courseId}/students/{studentId}/deduct")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performDeductionStudentFromCourse(@PathVariable("courseId") long courseId, @PathVariable("studentId") long studentId) {
        courseService.deductStudentFromCourse(courseId, studentId);
        return COURSE_STUDENTS_REDIRECT_URL;
    }

    /**
     * Enrolls a student in a specific course.
     * Handles POST requests to {@code /ui/v1/courses/my/{courseId}/students/{studentId}/enroll}.
     * Enrolls student via {@link CourseService#enrollStudentInCourse}. Requires
     * {@code COURSES_UPDATE}.
     *
     * @param courseId  the ID of the course
     * @param studentId the ID of the student to enroll
     * @return redirect to {@link #COURSE_STUDENTS_REDIRECT_URL}
     */
    @PostMapping("/my/{courseId}/students/{studentId}/enroll")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performEnrollingStudentToCourse(@PathVariable("courseId") long courseId, @PathVariable("studentId") long studentId) {
        courseService.enrollStudentInCourse(courseId, studentId);
        return COURSE_STUDENTS_REDIRECT_URL;
    }

    /**
     * Enrolls all students from a group in a specific course.
     * Handles POST requests to {@code /ui/v1/courses/my/{courseId}/group/{groupId}/enroll}.
     * Enrolls group via {@link CourseService#enrollAllStudentsFromGroupInCourse}. Requires
     * {@code COURSES_UPDATE}.
     *
     * @param courseId the ID of the course
     * @param groupId  the ID of the group to enroll
     * @return redirect to {@link #COURSE_STUDENTS_REDIRECT_URL}
     */
    @PostMapping("/my/{courseId}/group/{groupId}/enroll")
    @PreAuthorize("hasAuthority('COURSES_UPDATE')")
    public String performEnrollingGroupInCourse(@PathVariable("courseId") long courseId, @PathVariable("groupId") long groupId) {
        courseService.enrollAllStudentsFromGroupInCourse(courseId, groupId);
        return COURSE_STUDENTS_REDIRECT_URL;
    }

    private List<TopicDto> sortTopicsByTopicOrder(Set<TopicDto> topics) {
        return topics.stream()
            .sorted(Comparator.comparing(TopicDto::getTopicOrder))
            .toList();
    }

    private List<String> extractStudentEmails(Collection<StudentDto> students) {
        return students.stream()
            .map(UserDto::getEmail)
            .toList();
    }

}
