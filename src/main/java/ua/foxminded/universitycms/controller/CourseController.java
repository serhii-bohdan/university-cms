package ua.foxminded.universitycms.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.CourseService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        model.addAttribute("allNamesOfCourses", courseService.getAllNamesOfCourses())
            .addAttribute("courses", coursesPage.getContent())
            .addAttribute("page", pageable.getPageNumber())
            .addAttribute("totalItems", coursesPage.getTotalElements())
            .addAttribute("totalPages", coursesPage.getTotalPages())
            .addAttribute("size", pageable.getPageSize())
            .addAttribute("keyword", keyword);

        return "courses/all-courses";
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
            throw new CustomHttpException(e.getHttpStatus(), "Unable to display courses, user information is missing");
        }

        List<String> userCoursesNames = courseService.getCoursesNames(userCourses);
        model.addAttribute("userCoursesNames", userCoursesNames)
            .addAttribute("userCourses", userCourses)
            .addAttribute("keyword", keyword);

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
    @PreAuthorize("hasAuthority('COURSES_READ')")
    public String getPageWithSpecificCourse(Model model, @PathVariable("courseId") long courseId) {
        Optional<CourseDto> optional = courseService.getById(courseId);

        if (optional.isPresent()) {
            model.addAttribute("course", optional.get());
        } else {
            throw new CustomHttpException(HttpStatus.NOT_FOUND, "Course not found");
        }

        return "courses/course";
    }

}
