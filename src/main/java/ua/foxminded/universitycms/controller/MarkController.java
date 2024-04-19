package ua.foxminded.universitycms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.service.MarkService;
import java.util.List;
import java.util.Objects;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying student marks for a specific course.
 * It maps GET requests to the {@code /ui/v1/courses/my/{courseId}/marks} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/courses/my/{courseId}/marks")
public class MarkController {

    /**
     * A dummy student ID used for retrieving marks.
     */
    private static final long STUDENT_ID = 60;

    /**
     * The {@link MarkService} used to interact with mark data.
     */
    private final MarkService markService;

    /**
     * Constructs a new {@code MarkController} instance with the given {@link MarkService}.
     *
     * @param markService the {@link MarkService} to use for mark-related operations
     */
    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    /**
     * Renders a page containing a list of marks for a specific course and student, optionally filtered by a keyword.
     * This method handles GET requests to the root path of the controller mapping (`/ui/v1/courses/my/{courseId}/marks`).
     *
     * @param model      the Spring MVC {@link Model} object used to pass data to the view
     * @param courseId   the unique identifier of the course to retrieve marks for (from path variable)
     * @param courseName the name of the course (from request parameter)
     * @param keyword    an optional keyword to filter marks by topic name (can be empty)
     * @return the logical name of the view template ("courses/marks")
     */
    @GetMapping
    public String getPageWithStudentMarks(Model model, @PathVariable("courseId") long courseId,
                                          @RequestParam(name = "courseName") String courseName,
                                          @RequestParam(name = "keyword", required = false) String keyword) {
        List<MarkDto> marks;

        if (Objects.nonNull(keyword) && !keyword.isBlank()) {
            marks = markService.getStudentCourseMarksByTopicName(STUDENT_ID, courseId, keyword);
        } else {
            marks = markService.getStudentCourseMarks(STUDENT_ID, courseId);
        }

        model.addAttribute("namesOfTopics", markService.getNamesOfTopicsInCourse(courseId))
            .addAttribute("marks", marks)
            .addAttribute("courseName", courseName)
            .addAttribute("keyword", keyword);

        return "courses/marks";
    }

}
