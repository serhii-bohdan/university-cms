package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.service.MarkService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;
import java.util.List;

/**
 * Controller class responsible for managing mark-related functionalities within the application.
 * <p>
 * This controller provides endpoints for creating, updating, and deleting marks assigned to students for topics within
 * courses. It utilizes the {@link MarkService} to interact with mark data and provides a user interface for mark management.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/marks")
public class MarkController {

    /**
     * The name of the request parameter used to specify the student ID.
     */
    private static final String STUDENT_ID_PARAM_NAME = "sid";

    /**
     * The name of the request parameter used to specify the course ID.
     */
    private static final String COURSE_ID_PARAM_NAME = "cid";

    /**
     * The name of the request parameter used to specify the student's full name.
     */
    private static final String STUDENT_FULL_NAME_PARAM_NAME = "fullName";

    /**
     * The redirect URL format used to redirect the user to the marks page for a specific student, course, and optionally
     * the student's full name.
     */
    private static final String STUDENT_MARKS_REDIRECT_URL = "redirect:/ui/v1/marks?sid=%s&cid=%s&fullName=%s";

    /**
     * The {@link MarkService} used to interact with mark data.
     */
    private final MarkService markService;

    /**
     * Handles requests to display the marks of a specific student for a specific course.
     * <p>
     * This method processes GET requests to the `{@code /ui/v1/marks}` endpoint. It retrieves the marks
     * of a student for a course, optionally filtered by a keyword. The retrieved marks and
     * additional data (such as topic names, student information, and the search keyword) are added
     * to the model for rendering in the view.
     *
     * @param model           the {@link Model} object used to supply attributes to the view
     * @param studentId       the ID of the student whose marks are being retrieved
     * @param courseId        the ID of the course for which the marks are being retrieved
     * @param studentFullName the full name of the student, used for display purposes
     * @param keyword         an optional keyword to filter marks by topic name; can be null or blank
     * @return the name of the view that displays the student's marks
     */
    @GetMapping
    @PreAuthorize("hasAuthority('MARKS_READ')")
    public String getPageWithStudentMarks(Model model, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                          @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                          @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName,
                                          @RequestParam(name = "keyword", required = false) String keyword) {
        List<MarkDto> marks = StringUtils.isBlank(keyword)
            ? markService.getStudentCourseMarks(studentId, courseId)
            : markService.getStudentCourseMarksByTopicName(studentId, courseId, keyword);

        model.addAttribute(ModelAttributeNames.TOPIC_NAMES_ATTRIBUTE, markService.getNamesOfTopicsInCourse(courseId))
            .addAttribute(ModelAttributeNames.MARKS_ATTRIBUTE, marks)
            .addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.STUDENT_MARKS;
    }

    /**
     * Retrieves the mark creation form for a specific student and course.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/marks/new} endpoint. It retrieves student ID, course ID,
     * and student full name from request parameters. It then creates a new empty {@code MarkDto} object with the provided
     * student ID. Additionally, it retrieves a list of unrated topics for the given student and course using the
     * `markService`. These details are added to the model for display in the mark creation form template.
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return the logical view name {@code MARKS_CREATION_FORM} representing the mark creation template
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('MARKS_CREATE')")
    public String getCreationForm(Model model, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                  @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                  @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        MarkDto newMark = MarkDto.builder()
            .studentId(studentId)
            .build();

        model.addAttribute(ModelAttributeNames.MARK_ATTRIBUTE, newMark)
            .addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName)
            .addAttribute(ModelAttributeNames.UNRATED_TOPICS_ATTRIBUTE, markService.getUnratedTopics(studentId, courseId));

        return ViewNames.MARK_CREATION_FORM;
    }

    /**
     * Attempts to create a new mark for a student within a course.
     * <p>
     * This method handles POST requests to the {@code /ui/v1/marks/create} endpoint. It binds the request parameters
     * to a {@link MarkDto} object and validates it. If there are validation errors, it returns the mark creation form
     * view name. Otherwise, it attempts to save the mark using the {@code markService}. If successful, it redirects
     * the user to the marks page for the specific student, course, and student's full name (using request parameters).
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param mark            the mark data to be created (received from the form)
     * @param bindingResult   the binding result containing any validation errors
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return a redirect URL on success, the mark creation form view name on validation errors, or throws an exception
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('MARKS_CREATE')")
    public String performMarkCreation(Model model, @ModelAttribute("mark") @Valid MarkDto mark, BindingResult bindingResult,
                                      @RequestParam(STUDENT_ID_PARAM_NAME) long studentId, @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                      @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
                .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
                .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName)
                .addAttribute(ModelAttributeNames.UNRATED_TOPICS_ATTRIBUTE, markService.getUnratedTopics(studentId, courseId));
            return ViewNames.MARK_CREATION_FORM;
        }

        markService.save(mark);
        return String.format(STUDENT_MARKS_REDIRECT_URL, studentId, courseId, studentFullName);
    }

    /**
     * Retrieves the mark update form for a specific mark.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/marks/{markId}/edit} endpoint. It retrieves the mark ID
     * from the path variable and student ID, course ID, and student full name from request parameters. It attempts to
     * get the mark with the provided ID using the {@code markService}. If the mark is found, it adds the mark data and
     * other details to the model for display in the mark update form template.
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param markId          the ID of the mark to be updated (from path variable)
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return the logical view name {@code MARKS_UPDATE_FORM} representing the mark update template
     */
    @GetMapping("/{markId}/edit")
    @PreAuthorize("hasAuthority('MARKS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("markId") long markId, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                @RequestParam(COURSE_ID_PARAM_NAME) long courseId, @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        MarkDto mark = markService.getById(markId);
        model.addAttribute(ModelAttributeNames.MARK_ATTRIBUTE, mark)
            .addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName);

        return ViewNames.MARK_UPDATE_FORM;
    }

    /**
     * Attempts to update an existing mark.
     * <p>
     * This method handles PUT requests to the {@code /ui/v1/marks/update} endpoint. It binds the request parameters
     * to a {@link MarkDto} object and validates it. If there are validation errors, it returns the mark update form
     * view name. Otherwise, it attempts to update the mark using the {@code markService}. If successful, it redirects
     * the user to the marks page for the specific student, course, and student's full name (using request parameters).
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param mark            the mark data to be updated (received from the form)
     * @param bindingResult   the binding result containing any validation errors
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return a redirect URL on success, the mark update form view name on validation errors, or throws an exception
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('MARKS_UPDATE')")
    public String performMarkUpdate(Model model, @ModelAttribute("mark") @Valid MarkDto mark, BindingResult bindingResult,
                                    @RequestParam(STUDENT_ID_PARAM_NAME) long studentId, @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                    @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
                .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
                .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName);
            return ViewNames.MARK_UPDATE_FORM;
        }

        markService.update(mark);
        return String.format(STUDENT_MARKS_REDIRECT_URL, studentId, courseId, studentFullName);
    }

    /**
     * Deletes a specified mark.
     * <p>
     * This method handles DELETE requests to the {@code /ui/v1/marks/{markId}/delete} endpoint. It attempts to delete
     * the mark with the provided {@code markId} using the {@code markService}. If successful, it redirects the user to
     * the marks page for the specific student, course, and student's full name (using request parameters).
     *
     * @param markId          the ID of the mark to be deleted
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return a redirect URL to the marks page for the specific student, course, and student's full name
     */
    @DeleteMapping("/{markId}/delete")
    @PreAuthorize("hasAuthority('MARKS_DELETE')")
    public String performMarkDeletion(@PathVariable("markId") long markId, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                      @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                      @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        markService.deleteById(markId);
        return String.format(STUDENT_MARKS_REDIRECT_URL, studentId, courseId, studentFullName);
    }

}
