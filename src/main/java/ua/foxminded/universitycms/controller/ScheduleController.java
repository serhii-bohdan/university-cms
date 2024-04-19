package ua.foxminded.universitycms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.service.ScheduleService;
import java.util.Optional;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying student schedules.
 * It maps GET requests to the {@code /ui/v1/schedule} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/schedule")
public class ScheduleController {

    /**
     * A dummy student ID used for retrieving the schedule.
     */
    private static final long STUDENT_ID = 60;

    /**
     * The {@link ScheduleService} used to interact with schedule data.
     */
    private final ScheduleService scheduleService;

    /**
     * Constructs a new {@code ScheduleController} instance with the given {@link ScheduleService}.
     *
     * @param scheduleService the {@link ScheduleService} to use for schedule-related operations
     */
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Renders a page containing a calendar view of the student's schedule.
     * This method handles GET requests to the root path of the controller mapping (`/ui/v1/schedule`).
     *
     * @param model the Spring MVC {@link Model} object used to pass data to the view
     * @return the logical name of the view template ("schedule/calendar")
     */
    @GetMapping()
    public String getPageWithCalendar(Model model) {
        Optional<ScheduleDto> optional = scheduleService.getScheduleForStudent(STUDENT_ID);
        ScheduleDto schedule = optional.orElseGet(ScheduleDto::new);
        model.addAttribute("schedule", schedule);
        return "schedule/calendar";
    }

}
