package ua.foxminded.universitycms.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.MarkService;
import java.util.ArrayList;
import java.util.List;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying student marks for a specific course.
 * It maps GET requests to the {@code /ui/v1/courses/my/{courseId}/marks} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/courses/my/{courseId}/marks")
public class MarkController {

    /**
     * The {@link MarkService} used to interact with mark data.
     */
    private final MarkService markService;

    /**
     * Renders a page containing a list of marks for a specific course and student, optionally filtered by a keyword.
     * This method handles GET requests to the root path of the controller mapping (`/ui/v1/courses/my/{courseId}/marks`).
     * It retrieves the marks for the authenticated student from the `markService` based on their role and provided parameters.
     *
     * @param model             the Spring MVC {@link Model} object used to pass data to the view
     * @param customUserDetails the authenticated user's details, containing their ID and role (retrieved via `@AuthenticationPrincipal`)
     * @param courseId          the unique identifier of the course to retrieve marks for (from path variable)
     * @param courseName        the name of the course (from request parameter)
     * @param keyword           an optional keyword to filter marks by topic name (can be empty)
     * @return the logical name of the view template ("courses/student-marks")
     */
    @GetMapping
    @PreAuthorize("hasAuthority('MARKS_READ')")
    public String getPageWithStudentMarks(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @PathVariable("courseId") long courseId, @RequestParam(name = "courseName") String courseName,
                                          @RequestParam(name = "keyword", required = false) String keyword) {
        List<MarkDto> marks = new ArrayList<>();

        if (RoleName.STUDENT.equals(customUserDetails.getRoleName())) {
            Long studentId = customUserDetails.getId();
            marks = StringUtils.isBlank(keyword)
                ? markService.getStudentCourseMarks(studentId, courseId)
                : markService.getStudentCourseMarksByTopicName(studentId, courseId, keyword);
        }

        model.addAttribute("namesOfTopics", markService.getNamesOfTopicsInCourse(courseId))
            .addAttribute("marks", marks)
            .addAttribute("courseName", courseName)
            .addAttribute("keyword", keyword);

        return "courses/student-marks";
    }

}
