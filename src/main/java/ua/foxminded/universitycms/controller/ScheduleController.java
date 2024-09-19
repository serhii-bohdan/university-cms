package ua.foxminded.universitycms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.ScheduleService;
import java.util.Optional;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying student schedules.
 * It maps GET requests to the {@code /ui/v1/schedule} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/schedule")
public class ScheduleController {

    /**
     * The {@link ScheduleService} used to interact with schedule data.
     */
    private final ScheduleService scheduleService;

    /**
     * Renders a page containing a calendar view of the student's schedule.
     * This method handles GET requests to the root path of the controller mapping (`/ui/v1/schedule`).
     *
     * @param model             the Spring MVC {@link Model} object used to pass data to the view
     * @param customUserDetails the authenticated user's details, containing their ID and role
     * @return the logical name of the view template ("schedule/calendar")
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCHEDULE_READ')")
    public String getPageWithCalendar(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();
        RoleName userRole = customUserDetails.getRoleName();
        Optional<ScheduleDto> optional = Optional.empty();

        if (RoleName.TEACHER.equals(userRole)) {
            optional = scheduleService.getScheduleForTeacher(userId);
        } else if (RoleName.STUDENT.equals(userRole)) {
            optional = scheduleService.getScheduleForStudent(userId);
        }

        model.addAttribute("schedule", optional.orElseGet(ScheduleDto::new));
        return "schedule/calendar";
    }

}
