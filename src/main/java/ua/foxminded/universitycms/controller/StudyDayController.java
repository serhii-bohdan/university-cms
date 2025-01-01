package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.service.StudyDayService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;
import java.time.LocalDate;
import java.util.Optional;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying individual study days within
 * a student's schedule. It maps GET requests to the {@code /ui/v1/study-days} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/study-days")
public class StudyDayController {

    /**
     * Redirect URL template for accessing a specific study day by its date and schedule ID.
     */
    private static final String SPECIFIC_STUDY_DAY_REDIRECT_URL = "redirect:/ui/v1/study-days/%s?scheduleId=%s";

    /**
     * The {@link StudyDayService} used to interact with individual study day data.
     */
    private final StudyDayService studyDayService;

    /**
     * Handles GET requests to retrieve the study day for a given date and schedule.
     * If the study day does not exist, a new {@link StudyDayDto} is created and added to the model.
     *
     * @param model      the {@link Model} object used to pass attributes to the view
     * @param date       the date of the study day to retrieve
     * @param scheduleId the ID of the schedule associated with the study day
     * @return the name of the view displaying the study day
     */
    @GetMapping("/{date}")
    @PreAuthorize("hasAuthority('STUDY_DAYS_READ')")
    public String getPageWithStudyDayFromSchedule(Model model, @PathVariable("date") LocalDate date,
                                                  @RequestParam("scheduleId") long scheduleId) {
        Optional<StudyDayDto> optional = studyDayService.getStudyDayByScheduleIdAndDate(scheduleId, date);
        StudyDayDto studyDay = optional.orElseGet(() -> StudyDayDto.builder()
            .date(date)
            .weekDay(date.getDayOfWeek())
            .scheduleId(scheduleId)
            .build()
        );

        LocalDate today = LocalDate.now();
        model.addAttribute(ModelAttributeNames.STUDY_DAY_ATTRIBUTE, studyDay);
        return (today.isEqual(date) || today.isBefore(date))
            ? ViewNames.ACTUAL_STUDY_DAY_PAGE
            : ViewNames.PAST_STUDY_DAY_PAGE;
    }

    /**
     * Handles POST requests to add a new study day to the schedule. Validates the provided
     * {@link StudyDayDto}, and if validation passes, saves the study day to the database.
     *
     * @param studyDay      the {@link StudyDayDto} object containing study day data
     * @param bindingResult the {@link BindingResult} containing validation errors, if any
     * @return the redirect URL to the newly added study day, or the view name if validation fails
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('STUDY_DAYS_CREATE')")
    public String performStudyDayAdding(@ModelAttribute("studyDay") @Valid StudyDayDto studyDay,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.ACTUAL_STUDY_DAY_PAGE;
        }

        studyDayService.save(studyDay);
        return String.format(SPECIFIC_STUDY_DAY_REDIRECT_URL, studyDay.getDate(), studyDay.getScheduleId());
    }

}
