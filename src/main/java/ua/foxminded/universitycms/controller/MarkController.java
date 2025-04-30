package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * Spring MVC Controller for handling mark-related requests under the {@code /ui/v1/marks} path.
 * Manages operations such as creating, updating, and deleting marks for students within courses.
 * Uses {@link MarkService} for business logic. Annotated with {@code @Controller} and
 * {@code @RequiredArgsConstructor}.
 *
 * @author Serhii Bohdan
 * @see MarkService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/marks"})
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
     * Redirect URL template to the marks page for a student and course, with placeholders for IDs and name.
     */
    private static final String STUDENT_MARKS_REDIRECT_URL = "redirect:/ui/v1/marks?sid=%s&cid=%s&fullName=%s";

    /**
     * Service for interacting with mark data and performing business logic operations.
     */
    private final MarkService markService;

    /**
     * Displays marks for a specific student in a course, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/marks}. Retrieves marks via
     * {@link MarkService#findStudentCourseMarksByTopicName} and adds them to the model. Requires
     * {@code MARKS_READ}.
     *
     * @param model           the {@link Model} to store view data
     * @param studentId       the ID of the student whose marks are retrieved
     * @param courseId        the ID of the course for the marks
     * @param studentFullName the full name of the student for display
     * @param keyword         optional keyword to filter marks by topic name; may be blank
     * @return view name {@link ViewNames#STUDENT_MARKS} for the marks list
     */
    @GetMapping
    @PreAuthorize("hasAuthority('MARKS_READ')")
    public String getPageWithStudentMarks(Model model, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                          @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                          @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName,
                                          @RequestParam(name = "keyword", required = false) String keyword) {
        List<MarkDto> studentCourseMarks = markService.findStudentCourseMarksByTopicName(studentId, courseId, keyword);

        model.addAttribute(ModelAttributeNames.TOPIC_NAMES_ATTRIBUTE, markService.getNamesOfTopicsInCourse(courseId))
            .addAttribute(ModelAttributeNames.MARKS_ATTRIBUTE, studentCourseMarks)
            .addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.STUDENT_MARKS;
    }

    /**
     * Displays the form for creating a new mark for a student in a course.
     * Handles GET requests to {@code /ui/v1/marks/new}. Prepares a {@link MarkDto} and unrated topics
     * for the form. Requires {@code MARKS_CREATE}.
     *
     * @param model           the {@link Model} to store form data
     * @param studentId       the ID of the student, from request parameter
     * @param courseId        the ID of the course, from request parameter
     * @param studentFullName the full name of the student, from request parameter
     * @return view name {@link ViewNames#MARK_CREATION_FORM} for the creation form
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
     * Processes the submission of the mark creation form.
     * Handles POST requests to {@code /ui/v1/marks/create}. Validates {@link MarkDto} and saves the
     * mark via {@link MarkService#save}. Returns the form on errors. Requires {@code MARKS_CREATE}.
     *
     * @param model           the {@link Model} for error handling
     * @param mark            the {@link MarkDto} with form data
     * @param bindingResult   validation results for the DTO
     * @param studentId       the ID of the student, from request parameter
     * @param courseId        the ID of the course, from request parameter
     * @param studentFullName the full name of the student, from request parameter
     * @return redirect to {@link #STUDENT_MARKS_REDIRECT_URL} or form view on errors
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('MARKS_CREATE')")
    public String performMarkCreation(Model model, @ModelAttribute("mark") @Valid MarkDto mark,
                                      BindingResult bindingResult, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                      @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                      @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
                .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
                .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName)
                .addAttribute(ModelAttributeNames.UNRATED_TOPICS_ATTRIBUTE, markService.getUnratedTopics(studentId, courseId));
            return ViewNames.MARK_CREATION_FORM;
        }

        markService.save(mark);
        return STUDENT_MARKS_REDIRECT_URL.formatted(studentId, courseId, studentFullName);
    }

    /**
     * Displays the form for updating a mark.
     * Handles GET requests to {@code /ui/v1/marks/{markId}/edit}. Retrieves mark data via
     * {@link MarkService#getById} for the form. Requires {@code MARKS_UPDATE}.
     *
     * @param model           the {@link Model} to store form data
     * @param markId          the ID of the mark to update, from path variable
     * @param studentId       the ID of the student, from request parameter
     * @param courseId        the ID of the course, from request parameter
     * @param studentFullName the full name of the student, from request parameter
     * @return view name {@link ViewNames#MARK_UPDATE_FORM} for the update form
     */
    @GetMapping("/{markId}/edit")
    @PreAuthorize("hasAuthority('MARKS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("markId") long markId,
                                @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        MarkDto mark = markService.getById(markId);

        model.addAttribute(ModelAttributeNames.MARK_ATTRIBUTE, mark)
            .addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName);

        return ViewNames.MARK_UPDATE_FORM;
    }

    /**
     * Processes the update of a mark.
     * Handles PUT requests to {@code /ui/v1/marks/update}. Validates {@link MarkDto} and updates via
     * {@link MarkService#update}. Returns form on errors. Requires {@code MARKS_UPDATE}.
     *
     * @param model           the {@link Model} for error handling
     * @param mark            the {@link MarkDto} with updated data
     * @param bindingResult   validation results for the DTO
     * @param studentId       the ID of the student, from request parameter
     * @param courseId        the ID of the course, from request parameter
     * @param studentFullName the full name of the student, from request parameter
     * @return redirect to {@link #STUDENT_MARKS_REDIRECT_URL} or form view on errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('MARKS_UPDATE')")
    public String performMarkUpdate(Model model, @ModelAttribute("mark") @Valid MarkDto mark,
                                    BindingResult bindingResult, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                    @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                    @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.STUDENT_ID_ATTRIBUTE, studentId)
                .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
                .addAttribute(ModelAttributeNames.STUDENT_FULL_NAME_ATTRIBUTE, studentFullName);
            return ViewNames.MARK_UPDATE_FORM;
        }

        markService.update(mark);
        return STUDENT_MARKS_REDIRECT_URL.formatted(studentId, courseId, studentFullName);
    }

    /**
     * Deletes a mark from the system.
     * Handles DELETE requests to {@code /ui/v1/marks/{markId}/delete}. Deletes mark via
     * {@link MarkService#deleteById} and redirects. Requires {@code MARKS_DELETE}.
     *
     * @param markId          the ID of the mark to delete, from path variable
     * @param studentId       the ID of the student, from request parameter
     * @param courseId        the ID of the course, from request parameter
     * @param studentFullName the full name of the student, from request parameter
     * @return redirect to {@link #STUDENT_MARKS_REDIRECT_URL}
     */
    @DeleteMapping("/{markId}/delete")
    @PreAuthorize("hasAuthority('MARKS_DELETE')")
    public String performMarkDeletion(@PathVariable("markId") long markId, @RequestParam(STUDENT_ID_PARAM_NAME) long studentId,
                                      @RequestParam(COURSE_ID_PARAM_NAME) long courseId,
                                      @RequestParam(STUDENT_FULL_NAME_PARAM_NAME) String studentFullName) {
        markService.deleteById(markId);
        return STUDENT_MARKS_REDIRECT_URL.formatted(studentId, courseId, studentFullName);
    }

}
