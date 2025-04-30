package ua.foxminded.universitycms.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.ScheduleService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller handling schedule-related requests under {@code /ui/v1/schedule}.
 * Manages display of user schedules, primarily for teachers and students, using {@link ScheduleService} for
 * business logic. Annotated with {@code @Controller} and {@code @RequiredArgsConstructor} for
 * dependency injection.
 *
 * @author Serhii Bohdan
 * @see ScheduleService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/schedule"})
public class ScheduleController {

    /**
     * URL template for redirecting to the lessons list with schedule and date parameters.
     */
    private static final String LESSONS_LIST_REDIRECT_URL = "redirect:/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s";

    /**
     * URL template for redirecting to the lessons list with schedule, date, and additional user parameters.
     */
    private static final String LESSONS_LIST_WITH_USER_REDIRECT_URL = "redirect:/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s&userId=%s&userFullName=%s&userRole=%s";

    /**
     * Service for interacting with schedule data and performing business logic operations.
     */
    private final ScheduleService scheduleService;

    /**
     * Handles GET requests to {@code /ui/v1/schedule} and redirects to the lessons list.
     * Retrieves the user's schedule and local date via {@link ScheduleService}, then redirects
     * to the lessons list URL with the schedule ID and date parameters. Requires
     * {@code SCHEDULE_READ} authority.
     *
     * @param customUserDetails the authenticated user's details from {@link CustomUserDetails}
     * @return a redirect URL to the lessons list with schedule and date parameters
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCHEDULE_READ')")
    public String getUserScheduleAndRedirectToLessonsListUrl(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ScheduleDto schedule = scheduleService.getScheduleForUser(customUserDetails);
        LocalDate userLocalDate = scheduleService.getUserLocalDate(customUserDetails);
        return LESSONS_LIST_REDIRECT_URL.formatted(schedule.getId(), userLocalDate, userLocalDate, userLocalDate);
    }

    /**
     * Handles GET requests to {@code /ui/v1/schedule/{scheduleId}} and redirects to the lessons list.
     * <p>
     * Retrieves the user’s local date based on the provided time zone offset using
     * {@link ScheduleService#getUserLocalDate(String)} and redirects to the lessons list with
     * schedule ID, date, and additional user parameters (ID, full name, and role).
     * Requires {@code SCHEDULE_READ} authority.
     *
     * @param scheduleId             the ID of the schedule to display lessons for
     * @param userId                 the ID of the user associated with the schedule
     * @param userFullName           the full name of the user
     * @param userRole               the role of the user, as a {@link RoleName}
     * @param userLocationZoneOffset the user’s time zone offset (e.g., "+02:00")
     * @return a redirect URL to the lessons list with schedule, date, and user parameters
     */
    @GetMapping("/{scheduleId}")
    @PreAuthorize("hasAuthority('SCHEDULE_READ')")
    public String getSpecificScheduleAndRedirectToLessonsList(@PathVariable("scheduleId") long scheduleId, @RequestParam("userId") long userId,
                                                              @RequestParam("userFullName") String userFullName, @RequestParam("userRole") RoleName userRole,
                                                              @RequestParam("userLocationZoneOffset") String userLocationZoneOffset) {
        LocalDate userLocalDate = scheduleService.getUserLocalDate(userLocationZoneOffset);
        return LESSONS_LIST_WITH_USER_REDIRECT_URL.formatted(scheduleId, userLocalDate, userLocalDate, userLocalDate, userId, userFullName, userRole);
    }

}
