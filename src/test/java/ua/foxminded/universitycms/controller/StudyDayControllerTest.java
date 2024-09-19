package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.service.StudyDayService;
import java.time.LocalDate;
import java.util.Optional;

@WebMvcTest(controllers = StudyDayController.class)
@Import(SecurityConfig.class)
class StudyDayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyDayService studyDayServiceMock;

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_READ"})
    void getPageWithStudyDayFromSchedule_shouldReturnPageWithFoundStudyDay_whenValidScheduleIdAndDateProvided() throws Exception {
        long scheduleId = 1L;
        String date = "2023-12-03";
        LocalDate localDate = LocalDate.parse("2023-12-03");
        StudyDayDto studyDay = new StudyDayDto(localDate, localDate.getDayOfWeek(), scheduleId);
        when(studyDayServiceMock.getStudyDayByScheduleIdAndDate(scheduleId, localDate)).thenReturn(Optional.of(studyDay));

        mockMvc.perform(get("/ui/v1/schedule/{scheduleId}/studyDays/{date}", scheduleId, date))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studyDay"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(model().attributeExists("date"))
            .andExpect(view().name("schedule/study-day"));

        verify(studyDayServiceMock, times(1)).getStudyDayByScheduleIdAndDate(scheduleId, localDate);
    }

    @Test
    @WithMockUser(authorities = {"STUDY_DAYS_READ"})
    void getPageWithStudyDayFromSchedule_shouldReturnPageWithFakeStudyDay_whenNoStudyDayWithProvidedScheduleIdAndDate() throws Exception {
        long scheduleId = 1L;
        String date = "2023-12-03";
        LocalDate localDate = LocalDate.parse("2023-12-03");
        when(studyDayServiceMock.getStudyDayByScheduleIdAndDate(scheduleId, localDate)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/schedule/{scheduleId}/studyDays/{date}", scheduleId, date))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studyDay"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(model().attributeExists("date"))
            .andExpect(view().name("schedule/study-day"));

        verify(studyDayServiceMock, times(1)).getStudyDayByScheduleIdAndDate(scheduleId, localDate);
    }

}
