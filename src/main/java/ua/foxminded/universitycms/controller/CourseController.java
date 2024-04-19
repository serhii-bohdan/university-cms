package ua.foxminded.universitycms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.CourseService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying courses.
 * It maps GET requests to the {@code ui/v1/courses} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/courses")
public class CourseController {

    /**
     * The attribute name used to store error flag in the model.
     */
    private static final String HAS_ERROR = "hasError";

    /**
     * A dummy student ID used for retrieving user courses.
     */
    private static final long STUDENT_ID = 60;

    /**
     * The {@link CourseService} used to interact with course data.
     */
    private final CourseService courseService;

    /**
     * Constructs a new {@code CourseController} instance with the given {@link CourseService}.
     *
     * @param courseService the {@link CourseService} to use for course-related operations
     */
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

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
    public String getPageWithCourses(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                     @PageableDefault Pageable pageable) {
        Page<CourseDto> coursesPage = new PageImpl<>(new ArrayList<>());
        boolean hasError = false;

        try {
            if (Objects.nonNull(keyword) && !keyword.isBlank()) {
                coursesPage = courseService.getCourseByNameInPage(keyword, pageable);
            } else {
                coursesPage = courseService.getAllCoursesInPage(pageable);
            }
        } catch (ServiceException e) {
            hasError = true;
        }

        model.addAttribute("allNamesOfCourses", courseService.getAllNamesOfCourses())
            .addAttribute("courses", coursesPage.getContent())
            .addAttribute("page", pageable.getPageNumber())
            .addAttribute("totalItems", coursesPage.getTotalElements())
            .addAttribute("totalPages", coursesPage.getTotalPages())
            .addAttribute("size", pageable.getPageSize())
            .addAttribute("keyword", keyword)
            .addAttribute(HAS_ERROR, hasError);

        return "courses/all-courses";
    }

    /**
     * Renders a page containing a list of courses enrolled by the user, optionally filtered by a keyword.
     * This method handles GET requests to `/ui/v1/courses/my`.
     *
     * @param model   the Spring MVC {@link Model} object used to pass data to the view
     * @param keyword an optional keyword to filter user courses by name (can be empty)
     * @return the logical name of the view template ("courses/user-courses")
     */
    @GetMapping("/my")
    public String getPageWithCoursesForUser(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        List<CourseDto> userCourses = new ArrayList<>();
        List<String> userCoursesNames = new ArrayList<>();
        boolean hasError = false;

        try {
            userCoursesNames = courseService.getStudentCoursesNames(STUDENT_ID);

            if (Objects.nonNull(keyword) && !keyword.isBlank()) {
                userCourses = courseService.getStudentCourseByCourseName(STUDENT_ID, keyword);
            } else {
                userCourses = courseService.getStudentCourses(STUDENT_ID);
            }
        } catch (ServiceException e) {
            hasError = true;
        }

        model.addAttribute("userCoursesNames", userCoursesNames)
            .addAttribute("userCourses", userCourses)
            .addAttribute("keyword", keyword)
            .addAttribute(HAS_ERROR, hasError);

        return "courses/user-courses";
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
    public String getPageWithSpecificCourse(Model model, @PathVariable("courseId") long courseId) {
        Optional<CourseDto> optional = courseService.getById(courseId);

        if (optional.isPresent()) {
            CourseDto course = optional.get();
            model.addAttribute("course", course)
                .addAttribute("authorName", courseService
                    .getAuthorFullNameByTeacherId(course.getAuthorId()));
        } else {
            model.addAttribute(HAS_ERROR, true);
        }

        return "courses/course";
    }

}
