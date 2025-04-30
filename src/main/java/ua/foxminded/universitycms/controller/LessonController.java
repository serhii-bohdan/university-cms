package ua.foxminded.universitycms.controller;

import java.time.LocalDate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.LessonService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller handling lesson-related requests under {@code /ui/v1/lessons}.
 * Manages lesson operations like listing, creating, updating, and deleting within schedules. Uses
 * {@link LessonService} for business logic and enforces authorization via user permissions.
 * Annotated with {@code @Controller} and {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @author Serhii Bohdan
 * @see LessonService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/lessons"})
public class LessonController {

    /**
     * URL template for redirecting to the lessons list with schedule and date parameters.
     */
    private static final String LESSONS_LIST_REDIRECT_URL = "redirect:/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s";

    /**
     * URL template for redirecting to the lessons list with schedule, date, and additional user parameters.
     */
    private static final String LESSONS_LIST_WITH_USER_REDIRECT_URL = "redirect:/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s&userId=%s&userFullName=%s&userRole=%s";

    /**
     * Service for interacting with lesson data and performing business logic operations.
     */
    private final LessonService lessonService;

    /**
     * Displays a paginated list of lessons for a specified schedule and date range.
     * <p>
     * Handles GET requests to {@code /ui/v1/lessons}. Retrieves lessons using
     * {@link LessonService#findLessonsByScheduleIdAndDateRange} and populates the model with lesson data,
     * pagination details, and optional user parameters (ID, full name, and role). Requires
     * {@code LESSONS_READ} authority.
     *
     * @param model                the {@link Model} to hold lesson, pagination, and user attributes
     * @param pageable             the {@link Pageable} for pagination and sorting, defaults to ascending by date
     * @param scheduleId           the ID of the schedule to filter lessons by
     * @param userCurrentLocalDate the user's current local date
     * @param startDate            the start date of the range to filter lessons
     * @param endDate              the end date of the range to filter lessons
     * @param userId               the optional ID of the user associated with the schedule
     * @param userFullName         the optional full name of the user
     * @param userRole             the optional role of the user, as a {@link RoleName}
     * @return the {@link ViewNames#EDUCATOR_LESSONS} view name
     */
    @GetMapping
    @PreAuthorize("hasAuthority('LESSONS_READ')")
    public String getFilteredScheduleLessons(Model model, @PageableDefault(sort = {"date"}, direction = Sort.Direction.ASC) Pageable pageable,
                                             @RequestParam("scheduleId") long scheduleId, @RequestParam("userCurrentLocalDate") LocalDate userCurrentLocalDate,
                                             @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
                                             @RequestParam(value = "userId", required = false) Long userId,
                                             @RequestParam(value = "userFullName", required = false) String userFullName,
                                             @RequestParam(value = "userRole", required = false) RoleName userRole) {
        Page<LessonDto> lessonsPage = lessonService.findLessonsByScheduleIdAndDateRange(scheduleId, startDate, endDate, pageable);

        model.addAttribute(ModelAttributeNames.LESSONS_ATTRIBUTE, lessonsPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, lessonsPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, lessonsPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.SCHEDULE_ID_ATTRIBUTE, scheduleId)
            .addAttribute(ModelAttributeNames.USER_CURRENT_DATE_ATTRIBUTE, userCurrentLocalDate)
            .addAttribute(ModelAttributeNames.START_DATE_ATTRIBUTE, startDate)
            .addAttribute(ModelAttributeNames.END_DATE_ATTRIBUTE, endDate)
            .addAttribute(ModelAttributeNames.USER_ID_ATTRIBUTE, userId)
            .addAttribute(ModelAttributeNames.USER_FULL_NAME_ATTRIBUTE, userFullName)
            .addAttribute(ModelAttributeNames.USER_ROLE_ATTRIBUTE, userRole);

        return ViewNames.EDUCATOR_LESSONS;
    }

    /**
     * Displays the form for creating a new lesson within a specified schedule.
     * <p>
     * Handles GET requests to {@code /ui/v1/lessons/new}. Initializes a {@link LessonDto} with the given
     * schedule ID and populates the model with user courses, time zones, and user parameters (ID, role,
     * and optional full name). Requires {@code LESSONS_CREATE} authority.
     *
     * @param model                the {@link Model} to hold form attributes
     * @param userId               the ID of the user creating the lesson
     * @param userRole             the role of the user, as a {@link RoleName}
     * @param scheduleId           the ID of the schedule for the new lesson
     * @param userCurrentLocalDate the user's current local date
     * @param startDate            the start date of the current lesson list view
     * @param endDate              the end date of the current lesson list view
     * @param userFullName         the optional full name of the user
     * @return the {@link ViewNames#LESSON_CREATION_FORM} view name
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('LESSONS_CREATE')")
    public String getCreationForm(Model model, @RequestParam("userId") Long userId, @RequestParam("userRole") RoleName userRole,
                                  @RequestParam("scheduleId") long scheduleId, @RequestParam("userCurrentLocalDate") LocalDate userCurrentLocalDate,
                                  @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
                                  @RequestParam(value = "userFullName", required = false) String userFullName) {
        LessonDto lesson = LessonDto.builder()
            .scheduleId(scheduleId)
            .build();

        model.addAttribute(ModelAttributeNames.LESSON_ATTRIBUTE, lesson)
            .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(userId, userRole))
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST)
            .addAttribute(ModelAttributeNames.USER_CURRENT_DATE_ATTRIBUTE, userCurrentLocalDate)
            .addAttribute(ModelAttributeNames.START_DATE_ATTRIBUTE, startDate)
            .addAttribute(ModelAttributeNames.END_DATE_ATTRIBUTE, endDate)
            .addAttribute(ModelAttributeNames.USER_ID_ATTRIBUTE, userId)
            .addAttribute(ModelAttributeNames.USER_ROLE_ATTRIBUTE, userRole)
            .addAttribute(ModelAttributeNames.USER_FULL_NAME_ATTRIBUTE, userFullName);

        return ViewNames.LESSON_CREATION_FORM;
    }

    /**
     * Handles the creation of a new lesson via a POST request.
     * <p>
     * Validates the submitted {@link LessonDto} and saves it using {@link LessonService#save}. If validation fails,
     * returns the creation form with user courses, time zones, and user parameters; otherwise, redirects to the
     * lessons list. For users with {@link RoleName#MANAGER} role, redirects include additional user parameters
     * (ID, full name, role). Requires {@code LESSONS_CREATE} authority.
     *
     * @param model                the {@link Model} to hold attributes if validation fails
     * @param lesson               the {@link LessonDto} containing form data, must be valid
     * @param bindingResult        the {@link BindingResult} containing validation results
     * @param customUserDetails    the authenticated user's details from Spring Security
     * @param userId               the ID of the user creating the lesson
     * @param userRole             the role of the user, as a {@link RoleName}
     * @param userCurrentLocalDate the user's current local date
     * @param startDate            the start date of the current lesson list view
     * @param endDate              the end date of the current lesson list view
     * @param userFullName         the optional full name of the user
     * @return redirect to {@link #LESSONS_LIST_REDIRECT_URL} or
     * {@link #LESSONS_LIST_WITH_USER_REDIRECT_URL} on success, or
     * {@link ViewNames#LESSON_CREATION_FORM} on validation errors
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('LESSONS_CREATE')")
    public String performLessonCreation(Model model, @ModelAttribute("lesson") @Valid LessonDto lesson, BindingResult bindingResult,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @RequestParam("userId") Long userId, @RequestParam("userRole") RoleName userRole,
                                        @RequestParam("userCurrentLocalDate") LocalDate userCurrentLocalDate,
                                        @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
                                        @RequestParam(value = "userFullName", required = false) String userFullName) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(userId, userRole))
                .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST)
                .addAttribute(ModelAttributeNames.USER_CURRENT_DATE_ATTRIBUTE, userCurrentLocalDate)
                .addAttribute(ModelAttributeNames.START_DATE_ATTRIBUTE, startDate)
                .addAttribute(ModelAttributeNames.END_DATE_ATTRIBUTE, endDate)
                .addAttribute(ModelAttributeNames.USER_ID_ATTRIBUTE, userId)
                .addAttribute(ModelAttributeNames.USER_ROLE_ATTRIBUTE, userRole)
                .addAttribute(ModelAttributeNames.USER_FULL_NAME_ATTRIBUTE, userFullName);
            return ViewNames.LESSON_CREATION_FORM;
        }

        lessonService.save(lesson);
        return customUserDetails.getRoleName().equals(RoleName.MANAGER)
            ? LESSONS_LIST_WITH_USER_REDIRECT_URL.formatted(lesson.getScheduleId(), userCurrentLocalDate, startDate, endDate, userId, userFullName, userRole)
            : LESSONS_LIST_REDIRECT_URL.formatted(lesson.getScheduleId(), userCurrentLocalDate, startDate, endDate);
    }

    /**
     * Displays the form for updating an existing lesson.
     * <p>
     * Handles GET requests to {@code /ui/v1/lessons/{lessonId}/edit}. Retrieves the {@link LessonDto} by ID
     * using {@link LessonService#getById} and populates the model with lesson data, user courses, time zones,
     * and user parameters (ID, role, and optional full name). Requires {@code LESSONS_UPDATE} authority.
     *
     * @param model                the {@link Model} to hold form attributes
     * @param userId               the ID of the user updating the lesson
     * @param userRole             the role of the user, as a {@link RoleName}
     * @param lessonId             the ID of the lesson to update
     * @param userCurrentLocalDate the user's current local date
     * @param startDate            the start date of the current lesson list view
     * @param endDate              the end date of the current lesson list view
     * @param userFullName         the optional full name of the user
     * @return the {@link ViewNames#LESSON_UPDATE_FORM} view name
     */
    @GetMapping("/{lessonId}/edit")
    @PreAuthorize("hasAuthority('LESSONS_UPDATE')")
    public String getUpdateForm(Model model, @RequestParam("userId") Long userId, @RequestParam("userRole") RoleName userRole,
                                @PathVariable long lessonId, @RequestParam("userCurrentLocalDate") LocalDate userCurrentLocalDate,
                                @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
                                @RequestParam(value = "userFullName", required = false) String userFullName) {
        LessonDto lesson = lessonService.getById(lessonId);

        model.addAttribute(ModelAttributeNames.LESSON_ATTRIBUTE, lesson)
            .addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(userId, userRole))
            .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST)
            .addAttribute(ModelAttributeNames.USER_CURRENT_DATE_ATTRIBUTE, userCurrentLocalDate)
            .addAttribute(ModelAttributeNames.START_DATE_ATTRIBUTE, startDate)
            .addAttribute(ModelAttributeNames.END_DATE_ATTRIBUTE, endDate)
            .addAttribute(ModelAttributeNames.USER_ID_ATTRIBUTE, userId)
            .addAttribute(ModelAttributeNames.USER_ROLE_ATTRIBUTE, userRole)
            .addAttribute(ModelAttributeNames.USER_FULL_NAME_ATTRIBUTE, userFullName);

        return ViewNames.LESSON_UPDATE_FORM;
    }

    /**
     * Handles the update of an existing lesson via a PUT request.
     * <p>
     * Validates the submitted {@link LessonDto} and updates it using {@link LessonService#update}. If validation fails,
     * returns the update form with user courses, time zones, and user parameters; otherwise, redirects to the
     * lessons list. For users with {@link RoleName#MANAGER} role, redirects include additional user parameters
     * (ID, full name, role). Requires {@code LESSONS_UPDATE} authority.
     *
     * @param model                the {@link Model} to hold attributes if validation fails
     * @param lesson               the {@link LessonDto} containing updated data, must be valid
     * @param bindingResult        the {@link BindingResult} containing validation results
     * @param customUserDetails    the authenticated user's details from Spring Security
     * @param userId               the ID of the user updating the lesson
     * @param userRole             the role of the user, as a {@link RoleName}
     * @param userCurrentLocalDate the user's current local date
     * @param startDate            the start date of the current lesson list view
     * @param endDate              the end date of the current lesson list view
     * @param userFullName         the optional full name of the user
     * @return redirect to {@link #LESSONS_LIST_REDIRECT_URL} or
     * {@link #LESSONS_LIST_WITH_USER_REDIRECT_URL} on success, or
     * {@link ViewNames#LESSON_UPDATE_FORM} on validation errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('LESSONS_UPDATE')")
    public String performLessonUpdate(Model model, @ModelAttribute("lesson") @Valid LessonDto lesson, BindingResult bindingResult,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestParam("userId") Long userId, @RequestParam("userRole") RoleName userRole,
                                      @RequestParam("userCurrentLocalDate") LocalDate userCurrentLocalDate,
                                      @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
                                      @RequestParam(value = "userFullName", required = false) String userFullName) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelAttributeNames.USER_COURSES_ATTRIBUTE, lessonService.getUserCourses(userId, userRole))
                .addAttribute(ModelAttributeNames.TIME_ZONES_ATTRIBUTE, ModelAttributeNames.TIME_ZONES_LIST)
                .addAttribute(ModelAttributeNames.USER_CURRENT_DATE_ATTRIBUTE, userCurrentLocalDate)
                .addAttribute(ModelAttributeNames.START_DATE_ATTRIBUTE, startDate)
                .addAttribute(ModelAttributeNames.END_DATE_ATTRIBUTE, endDate)
                .addAttribute(ModelAttributeNames.USER_ID_ATTRIBUTE, userId)
                .addAttribute(ModelAttributeNames.USER_ROLE_ATTRIBUTE, userRole)
                .addAttribute(ModelAttributeNames.USER_FULL_NAME_ATTRIBUTE, userFullName);
            return ViewNames.LESSON_UPDATE_FORM;
        }

        lessonService.update(lesson);
        return customUserDetails.getRoleName().equals(RoleName.MANAGER)
            ? LESSONS_LIST_WITH_USER_REDIRECT_URL.formatted(lesson.getScheduleId(), userCurrentLocalDate, startDate, endDate, userId, userFullName, userRole)
            : LESSONS_LIST_REDIRECT_URL.formatted(lesson.getScheduleId(), userCurrentLocalDate, startDate, endDate);
    }

    /**
     * Deletes a lesson via a DELETE request.
     * <p>
     * Removes the lesson identified by {@code lessonId} using {@link LessonService#deleteById} and redirects
     * to the lessons list for the associated schedule. For users with {@link RoleName#MANAGER} role,
     * redirects include additional user parameters (ID, full name, role). Requires {@code LESSONS_DELETE} authority.
     *
     * @param customUserDetails    the authenticated user's details from Spring Security
     * @param lessonId             the ID of the lesson to delete
     * @param scheduleId           the ID of the schedule containing the lesson
     * @param userCurrentLocalDate the user's current local date
     * @param startDate            the start date of the current lesson list view
     * @param endDate              the end date of the current lesson list view
     * @param userId               the optional ID of the user associated with the schedule
     * @param userFullName         the optional full name of the user
     * @param userRole             the optional role of the user, as a {@link RoleName}
     * @return redirect to {@link #LESSONS_LIST_REDIRECT_URL} or
     * {@link #LESSONS_LIST_WITH_USER_REDIRECT_URL}
     */
    @DeleteMapping("/{lessonId}/delete")
    @PreAuthorize("hasAuthority('LESSONS_DELETE')")
    public String performLessonDeletion(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @PathVariable("lessonId") long lessonId, @RequestParam("scheduleId") long scheduleId,
                                        @RequestParam("userCurrentLocalDate") LocalDate userCurrentLocalDate,
                                        @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
                                        @RequestParam(value = "userId", required = false) Long userId,
                                        @RequestParam(value = "userFullName", required = false) String userFullName,
                                        @RequestParam(value = "userRole", required = false) RoleName userRole) {
        lessonService.deleteById(lessonId);
        return customUserDetails.getRoleName().equals(RoleName.MANAGER)
            ? LESSONS_LIST_WITH_USER_REDIRECT_URL.formatted(scheduleId, userCurrentLocalDate, startDate, endDate, userId, userFullName, userRole)
            : LESSONS_LIST_REDIRECT_URL.formatted(scheduleId, userCurrentLocalDate, startDate, endDate);
    }

}
