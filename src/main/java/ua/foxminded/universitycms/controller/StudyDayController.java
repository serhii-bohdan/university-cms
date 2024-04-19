package ua.foxminded.universitycms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.service.StudyDayService;
import java.time.LocalDate;
import java.util.Optional;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying individual study days within a student's schedule.
 * It maps GET requests to the {@code /ui/v1/schedule/{scheduleId}/studyDays/{date}} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/schedule/{scheduleId}/studyDays")
public class StudyDayController {

    /**
     * The {@link StudyDayService} used to interact with individual study day data.
     */
    private final StudyDayService studyDayService;

    /**
     * Constructs a new {@code StudyDayController} instance with the given {@link StudyDayService}.
     *
     * @param studyDayService the {@link StudyDayService} to use for study day-related operations
     */
    public StudyDayController(StudyDayService studyDayService) {
        this.studyDayService = studyDayService;
    }

    /**
     * Renders a page containing details for a specific study day within a student's schedule.
     * This method handles GET requests to the path `/ui/v1/schedule/{scheduleId}/studyDays/{date}`, where:
     * <ul>
     *   <li>`{scheduleId}` is the unique identifier of the schedule.</li>
     *   <li>`{date}` is the date of the study day in YYYY-MM-DD format.</li>
     * </ul>
     *
     * @param model      the Spring MVC {@link Model} object used to pass data to the view
     * @param scheduleId the unique identifier of the schedule to retrieve a study day from (from path variable)
     * @param date       the date of the study day to retrieve (from path variable) in YYYY-MM-DD format
     * @return the logical name of the view template ("schedule/study-day")
     */
    @GetMapping("/{date}")
    public String getPageWithStudyDayFromSchedule(Model model, @PathVariable("scheduleId") long scheduleId,
                                                  @PathVariable("date") LocalDate date) {
        Optional<StudyDayDto> optional = studyDayService.getStudyDayByScheduleIdAndDate(scheduleId, date);
        StudyDayDto studyDay = optional.orElseGet(() -> new StudyDayDto(date, date.getDayOfWeek(), scheduleId));

        model.addAttribute("studyDay", studyDay)
            .addAttribute("scheduleId", scheduleId)
            .addAttribute("date", date);

        return "schedule/study-day";
    }

}
