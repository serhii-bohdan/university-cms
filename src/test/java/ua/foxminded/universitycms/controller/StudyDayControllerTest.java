package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.ControllerTestConfig;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.service.StudyDayService;
import java.time.LocalDate;
import java.util.Optional;

@WebMvcTest(controllers = StudyDayController.class)
@ContextConfiguration(classes = {ControllerTestConfig.class})
@Import(SecurityConfig.class)
class StudyDayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyDayService studyDayServiceMock;

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_READ"})
    void getPageWithStudyDayFromSchedule_shouldReturnPageWithStudyDayThatWasInThePast_whenValidScheduleIdAndPastDateProvided() throws Exception {
        long scheduleId = 1L;
        String date = "2023-12-03";
        LocalDate pastLocalDate = LocalDate.parse(date);
        StudyDayDto studyDay = StudyDayDto.builder()
            .date(pastLocalDate)
            .weekDay(pastLocalDate.getDayOfWeek())
            .scheduleId(scheduleId)
            .build();
        when(studyDayServiceMock.getStudyDayByScheduleIdAndDate(scheduleId, pastLocalDate)).thenReturn(Optional.of(studyDay));

        mockMvc.perform(get("/ui/v1/study-days/{date}", date)
                .param("scheduleId", String.valueOf(scheduleId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studyDay"))
            .andExpect(view().name("schedule/past-study-day"));

        verify(studyDayServiceMock, times(1)).getStudyDayByScheduleIdAndDate(scheduleId, pastLocalDate);
    }

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_READ"})
    void getPageWithStudyDayFromSchedule_shouldReturnPageWithCurrentStudyDay_whenValidScheduleIdAndCurrentDateProvided() throws Exception {
        long scheduleId = 1L;
        LocalDate pastLocalDate = LocalDate.now();
        StudyDayDto studyDay = StudyDayDto.builder()
            .date(pastLocalDate)
            .weekDay(pastLocalDate.getDayOfWeek())
            .scheduleId(scheduleId)
            .build();
        when(studyDayServiceMock.getStudyDayByScheduleIdAndDate(scheduleId, pastLocalDate)).thenReturn(Optional.of(studyDay));

        mockMvc.perform(get("/ui/v1/study-days/{date}", pastLocalDate.toString())
                .param("scheduleId", String.valueOf(scheduleId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studyDay"))
            .andExpect(view().name("schedule/actual-study-day"));

        verify(studyDayServiceMock, times(1)).getStudyDayByScheduleIdAndDate(scheduleId, pastLocalDate);
    }

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_READ"})
    void getPageWithStudyDayFromSchedule_shouldReturnPageWithStudyDayWhichWillBeInTheFuture_whenValidScheduleIdAndFeatureDateProvided() throws Exception {
        long scheduleId = 1L;
        long daysToAdd = 2;
        LocalDate localDate = LocalDate.now().plusDays(daysToAdd);
        StudyDayDto studyDay = StudyDayDto.builder()
            .date(localDate)
            .weekDay(localDate.getDayOfWeek())
            .scheduleId(scheduleId)
            .build();
        when(studyDayServiceMock.getStudyDayByScheduleIdAndDate(scheduleId, localDate)).thenReturn(Optional.of(studyDay));

        mockMvc.perform(get("/ui/v1/study-days/{date}", localDate.toString())
                .param("scheduleId", String.valueOf(scheduleId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studyDay"))
            .andExpect(view().name("schedule/actual-study-day"));

        verify(studyDayServiceMock, times(1)).getStudyDayByScheduleIdAndDate(scheduleId, localDate);
    }

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_READ"})
    void getPageWithStudyDayFromSchedule_shouldReturnPageWithFakeStudyDayThatWasInThePast_whenNoStudyDayWithProvidedScheduleIdAndPastDate() throws Exception {
        long scheduleId = 1L;
        String date = "2023-12-03";
        LocalDate pastLocalDate = LocalDate.parse(date);
        when(studyDayServiceMock.getStudyDayByScheduleIdAndDate(scheduleId, pastLocalDate)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/study-days/{date}", date)
                .param("scheduleId", String.valueOf(scheduleId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studyDay"))
            .andExpect(view().name("schedule/past-study-day"));

        verify(studyDayServiceMock, times(1)).getStudyDayByScheduleIdAndDate(scheduleId, pastLocalDate);
    }

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_READ"})
    void getPageWithStudyDayFromSchedule_shouldReturnPageWithFakeStudyDayWhichWillBeInTheFuture_whenNoStudyDayWithProvidedScheduleIdAndFutureDate() throws Exception {
        long scheduleId = 1L;
        long daysToAdd = 3;
        LocalDate featureLocalDate = LocalDate.now().plusDays(daysToAdd);
        when(studyDayServiceMock.getStudyDayByScheduleIdAndDate(scheduleId, featureLocalDate)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/study-days/{date}", featureLocalDate.toString())
                .param("scheduleId", String.valueOf(scheduleId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studyDay"))
            .andExpect(view().name("schedule/actual-study-day"));

        verify(studyDayServiceMock, times(1)).getStudyDayByScheduleIdAndDate(scheduleId, featureLocalDate);
    }

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_CREATE"})
    void performStudyDayAdding_shouldAddNewStudyDayAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        long scheduleId = 1L;
        long daysToAdd = 1;
        LocalDate localDate = LocalDate.now().plusDays(daysToAdd);
        String date = localDate.toString();

        mockMvc.perform(post("/ui/v1/study-days/add")
                .param("date", localDate.toString())
                .param("weekDay", localDate.getDayOfWeek().toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/study-days/%s?scheduleId=%s", date, scheduleId)));

        verify(studyDayServiceMock, times(1)).save(any(StudyDayDto.class));
    }

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_CREATE"})
    void performStudyDayAdding_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilledAndValidationFails() throws Exception {
        long scheduleId = 1L;
        long daysToAdd = 1;
        LocalDate localDate = LocalDate.now().plusDays(daysToAdd);

        mockMvc.perform(post("/ui/v1/study-days/add")
                .param("date", localDate.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("schedule/actual-study-day"));

        verify(studyDayServiceMock, never()).save(any(StudyDayDto.class));
    }

}
