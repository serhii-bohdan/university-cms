package ua.foxminded.universitycms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.service.StudyDayService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;
import java.time.LocalDate;
import java.util.Optional;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying individual study days within a student's schedule.
 * It maps GET requests to the {@code /ui/v1/schedule/{scheduleId}/studyDays/{date}} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/schedule/{scheduleId}/studyDays")
public class StudyDayController {

    /**
     * The {@link StudyDayService} used to interact with individual study day data.
     */
    private final StudyDayService studyDayService;

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
    @PreAuthorize("hasAuthority('STUDY_DAYS_READ')")
    public String getPageWithStudyDayFromSchedule(Model model, @PathVariable("scheduleId") long scheduleId,
                                                  @PathVariable("date") LocalDate date) {
        Optional<StudyDayDto> optional = studyDayService.getStudyDayByScheduleIdAndDate(scheduleId, date);
        StudyDayDto studyDay = optional.orElseGet(() -> StudyDayDto.builder()
            .date(date)
            .weekDay(date.getDayOfWeek())
            .scheduleId(scheduleId)
            .build()
        );

        model.addAttribute(ModelAttributeNames.STUDY_DAY_ATTRIBUTE, studyDay);
        return ViewNames.STUDY_DAY_PAGE;
    }

}
