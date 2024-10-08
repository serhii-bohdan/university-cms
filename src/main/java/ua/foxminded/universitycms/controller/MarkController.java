package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.service.MarkService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for managing mark-related functionalities within the application.
 * <p>
 * This controller provides endpoints for creating, updating, and deleting marks assigned to students for topics within courses.
 * It utilizes the `MarkService` to interact with mark data and provides a user interface for mark management.
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
     * Renders a page containing a list of marks for a specific course and student, optionally filtered by a keyword.
     * This method handles GET requests to the root path of the controller mapping (`/ui/v1/courses/my/{courseId}/marks`).
     * It retrieves the marks for the authenticated student from the `markService` based on their role and provided parameters.
     *
     * @param model    the Spring MVC {@link Model} object used to pass data to the view
     * @param courseId the unique identifier of the course to retrieve marks for (from path variable)
     * @param keyword  an optional keyword to filter marks by topic name (can be empty)
     * @return the logical name of the view template ("courses/student-marks")
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
     * This method handles GET requests to the `/new` endpoint. It retrieves student ID, course ID, and student full name from
     * request parameters. It then creates a new empty `MarkDto` object with the provided student ID. Additionally, it retrieves
     * a list of unrated topics for the given student and course using the `markService`. These details are added to the
     * model for display in the mark creation form template.
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return the logical view name `MARKS_CREATION_FORM` representing the mark creation template
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
     * This method handles POST requests to the `/create` endpoint. It binds the request parameters to a {@link MarkDto} object and
     * validates it. If there are validation errors, it returns the mark creation form view name. Otherwise, it attempts to
     * save the mark using the `markService`. If successful, it redirects the user to the marks page for the specific student,
     * course, and student's full name (using request parameters).
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param mark            the mark data to be created (received from the form)
     * @param bindingResult   the binding result containing any validation errors
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return a redirect URL on success, the mark creation form view name on validation errors, or throws an exception
     * @throws CustomHttpException if an unexpected error occurs during mark creation
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
     * This method handles GET requests to the `/{markId}/edit` endpoint. It retrieves the mark ID from the path variable and
     * student ID, course ID, and student full name from request parameters. It attempts to get the mark with the provided ID
     * using the `markService`. If the mark is found, it adds the mark data and other details to the model for display in the
     * mark update form template. Otherwise, it throws a `CustomHttpException` with a not found status.
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param markId          the ID of the mark to be updated (from path variable)
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return the logical view name `MARKS_UPDATE_FORM` representing the mark update template
     * @throws CustomHttpException if the mark with the provided ID is not found
     */
    @GetMapping("/{markId}/edit")
    @PreAuthorize("hasAuthority('MARKS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("markId") long markId, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                @RequestParam(COURSE_ID_PARAM_NAME) long courseId, @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        Optional<MarkDto> optional = markService.getById(markId);

        if (optional.isPresent()) {
            model.addAttribute(ModelAttributeNames.MARK_ATTRIBUTE, optional.get())
                .addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
                .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
                .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName);
            return ViewNames.MARK_UPDATE_FORM;
        }

        throw new CustomHttpException(HttpStatus.NOT_FOUND, "Editing failed. Mark not found.");
    }

    /**
     * Attempts to update an existing mark.
     * <p>
     * This method handles PUT requests to the `/update` endpoint. It binds the request parameters to a {@link MarkDto} object and
     * validates it. If there are validation errors, it returns the mark update form view name. Otherwise, it attempts to
     * update the mark using the `markService`. If successful, it redirects the user to the marks page for the specific student,
     * course, and student's full name (using request parameters).
     *
     * @param model           the Spring MVC Model object used to store data for the view
     * @param mark            the mark data to be updated (received from the form)
     * @param bindingResult   the binding result containing any validation errors
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return a redirect URL on success, the mark update form view name on validation errors, or throws an exception
     * @throws CustomHttpException if an unexpected error occurs during mark update
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
     * This method handles DELETE requests to the `/{markId}/delete` endpoint. It attempts to delete the mark with the
     * provided `markId` using the `markService`. If successful, it redirects the user to the marks page for the specific student,
     * course, and student's full name (using request parameters). If the mark is not found, it throws a `CustomHttpException`
     * with a not found status.
     *
     * @param markId          the ID of the mark to be deleted
     * @param studentId       the ID of the student (from request parameter)
     * @param courseId        the ID of the course (from request parameter)
     * @param studentFullName the full name of the student (from request parameter)
     * @return a redirect URL to the marks page for the specific student, course, and student's full name
     * @throws CustomHttpException if the mark with the provided ID is not found
     */
    @DeleteMapping("/{markId}/delete")
    @PreAuthorize("hasAuthority('MARKS_DELETE')")
    public String performMarkDeletion(@PathVariable("markId") long markId, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                      @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                      @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        try {
            markService.deleteById(markId);
            return String.format(STUDENT_MARKS_REDIRECT_URL, studentId, courseId, studentFullName);
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Deletion failed. Mark not found.");
        }
    }

}
