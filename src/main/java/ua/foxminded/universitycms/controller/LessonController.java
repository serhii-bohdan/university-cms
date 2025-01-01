package ua.foxminded.universitycms.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.LessonService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Handles operations related to lessons, including creating, updating, and deleting lesson records.
 * Supports rendering forms for lesson creation and updates, as well as processing user input.
 * Ensures proper authorization for each operation based on user permissions.
 * <p>
 * This controller interacts with the LessonService to manage lesson data and prepare the necessary
 * information for the user interface.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/lessons")
public class LessonController {

    /**
     * The URL template used to redirect to a specific study day page. It includes placeholders for
     * the date and schedule ID.
     */
    private static final String SPECIFIC_STUDY_DAY_REDIRECT_URL = "redirect:/ui/v1/study-days/%s?scheduleId=%s";

    /**
     * The service responsible for handling lesson-related operations,
     * such as retrieving, creating, updating, and deleting lesson records.
     */
    private final LessonService lessonService;

    /**
     * Displays the lesson creation form.
     * <p>
     * This method initializes a new {@link LessonDto} with the given study day ID, course, and other details.
     * It populates the model with the necessary attributes such as available time zones, user-specific courses,
     * and the date and schedule ID. The user must have the {@code LESSONS_CREATE} authority to access this method.
     *
     * @param model             the model to which attributes are added for the view
     * @param customUserDetails the currently authenticated user's details
     * @param studyDayId        the ID of the study day to which the lesson will be added
     * @param date              the date of the lesson
     * @param scheduleId        the schedule ID associated with the lesson
     * @return the name of the view for the lesson creation form
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('LESSONS_CREATE')")
    public String getCreationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @RequestParam("studyDayId") long studyDayId, @RequestParam("date") LocalDate date,
                                  @RequestParam("scheduleId") long scheduleId) {
        LessonDto lesson = LessonDto.builder()
            .studyDayId(studyDayId)
            .course(CourseDto.builder().build())
            .build();

        model.addAttribute(ModelAttributeNames.LESSON_ATTRIBUTE, lesson)
            .addAttribute(ModelAttributeNames.AVAILABLE_ZONE_IDS_ATTRIBUTE, ZoneId.getAvailableZoneIds())
            .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(customUserDetails))
            .addAttribute(ModelAttributeNames.DATE_ATTRIBUTE, date)
            .addAttribute(ModelAttributeNames.SCHEDULE_ID_ATTRIBUTE, scheduleId);

        return ViewNames.LESSON_CREATION_FORM;
    }

    /**
     * Handles the creation of a new lesson.
     * <p>
     * This method processes the lesson creation form submission by validating the provided {@link LessonDto}.
     * If there are validation errors, it redisplays the lesson creation form with error messages.
     * If the form is valid, the lesson is saved, and the user is redirected to the corresponding study day page.
     * The user must have the {@code LESSONS_CREATE} authority to access this method.
     *
     * @param model             the model to which attributes are added for the view
     * @param customUserDetails the currently authenticated user's details
     * @param lesson            the {@link LessonDto} object containing the lesson data
     * @param bindingResult     the result of binding the submitted form data to the {@link LessonDto}
     * @param date              the date of the lesson
     * @param scheduleId        the schedule ID associated with the lesson
     * @return the view name for either the lesson creation form (if validation fails) or a redirect to the study day page
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('LESSONS_CREATE')")
    public String performLessonCreation(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @ModelAttribute("lesson") @Valid LessonDto lesson, BindingResult bindingResult,
                                        @RequestParam("date") LocalDate date, @RequestParam("scheduleId") long scheduleId) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.AVAILABLE_ZONE_IDS_ATTRIBUTE, ZoneId.getAvailableZoneIds())
                .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(customUserDetails))
                .addAttribute(ModelAttributeNames.DATE_ATTRIBUTE, date)
                .addAttribute(ModelAttributeNames.SCHEDULE_ID_ATTRIBUTE, scheduleId);
            return ViewNames.LESSON_CREATION_FORM;
        }

        lessonService.save(lesson);
        return String.format(SPECIFIC_STUDY_DAY_REDIRECT_URL, date, scheduleId);
    }

    /**
     * Displays the form to update an existing lesson.
     * <p>
     * This method retrieves the {@link LessonDto} for the given lesson ID, and if the lesson exists,
     * it populates the model with necessary attributes for the update form. The user must have the
     * {@code LESSONS_UPDATE} authority to access this method.
     * with a {@code NOT_FOUND} status is thrown.
     *
     * @param model             the model to which attributes are added for the view
     * @param customUserDetails the currently authenticated user's details
     * @param lessonId          the ID of the lesson to update
     * @param date              the date of the lesson
     * @param scheduleId        the schedule ID associated with the lesson
     * @return the view name for the lesson update form if the lesson exists
     */
    @GetMapping("/{lessonId}/edit")
    @PreAuthorize("hasAuthority('LESSONS_UPDATE')")
    public String getUpdateForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @PathVariable long lessonId, @RequestParam("date") LocalDate date,
                                @RequestParam("scheduleId") long scheduleId) {
        LessonDto lesson = lessonService.getById(lessonId);
        model.addAttribute(ModelAttributeNames.LESSON_ATTRIBUTE, lesson)
            .addAttribute(ModelAttributeNames.AVAILABLE_ZONE_IDS_ATTRIBUTE, ZoneId.getAvailableZoneIds())
            .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(customUserDetails))
            .addAttribute(ModelAttributeNames.DATE_ATTRIBUTE, date)
            .addAttribute(ModelAttributeNames.SCHEDULE_ID_ATTRIBUTE, scheduleId);

        return ViewNames.LESSON_UPDATE_FORM;
    }

    /**
     * Updates an existing lesson based on the provided {@link LessonDto}.
     * <p>
     * This method validates the {@link LessonDto} for errors and, if any exist, returns the user to the update
     * form with the errors displayed. If the validation is successful, the lesson is updated using the
     * {@link LessonService}. The user must have the {@code LESSONS_UPDATE} authority to access this method.
     *
     * @param model             the model to which attributes are added for the view
     * @param customUserDetails the currently authenticated user's details
     * @param lesson            the {@link LessonDto} containing the lesson data to be updated
     * @param bindingResult     the result of the validation process for the lesson data
     * @param date              the date of the lesson
     * @param scheduleId        the schedule ID associated with the lesson
     * @return the redirect URL to the specific study day page if the lesson update is successful
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('LESSONS_UPDATE')")
    public String performLessonUpdate(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @ModelAttribute("lesson") @Valid LessonDto lesson, BindingResult bindingResult,
                                      @RequestParam("date") LocalDate date, @RequestParam("scheduleId") long scheduleId) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.AVAILABLE_ZONE_IDS_ATTRIBUTE, ZoneId.getAvailableZoneIds())
                .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(customUserDetails))
                .addAttribute(ModelAttributeNames.DATE_ATTRIBUTE, date)
                .addAttribute(ModelAttributeNames.SCHEDULE_ID_ATTRIBUTE, scheduleId);
            return ViewNames.LESSON_UPDATE_FORM;
        }

        lessonService.update(lesson);
        return String.format(SPECIFIC_STUDY_DAY_REDIRECT_URL, date, scheduleId);
    }

    /**
     * Deletes a lesson identified by its {@code lessonId}.
     * <p>
     * This method performs a deletion operation using the {@link LessonService} and handles any errors that occur
     * during the process. The user must have the {@code LESSONS_DELETE} authority to access this method. After
     * successful deletion, the user is redirected to the specific study day page.
     *
     * @param lessonId   the ID of the lesson to be deleted
     * @param date       the date associated with the lesson
     * @param scheduleId the schedule ID associated with the lesson
     * @return the redirect URL to the specific study day page after the lesson is deleted
     */
    @DeleteMapping("/{lessonId}/delete")
    @PreAuthorize("hasAuthority('LESSONS_DELETE')")
    public String performLessonDeletion(@PathVariable("lessonId") long lessonId, @RequestParam("date") LocalDate date,
                                        @RequestParam("scheduleId") long scheduleId) {
        lessonService.deleteById(lessonId);
        return String.format(SPECIFIC_STUDY_DAY_REDIRECT_URL, date, scheduleId);
    }

}
