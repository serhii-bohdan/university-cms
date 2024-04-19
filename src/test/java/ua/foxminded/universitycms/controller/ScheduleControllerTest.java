package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.service.ScheduleService;
import java.util.Optional;

@WebMvcTest(controllers = ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleServiceMock;

    @Test
    void getPageWithCalendar_shouldReturnPageWithScheduleForStudent_whenValidStudentIdProvided() throws Exception {
        long studentId = 60L;
        ScheduleDto scheduleForStudent = new ScheduleDto();
        scheduleForStudent.setId(1L);
        when(scheduleServiceMock.getScheduleForStudent(studentId)).thenReturn(Optional.of(scheduleForStudent));

        mockMvc.perform(get("/ui/v1/schedule"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("schedule"))
            .andExpect(view().name("schedule/calendar"));

        verify(scheduleServiceMock, times(1)).getScheduleForStudent(studentId);
    }

    @Test
    void getPageWithCalendar_shouldReturnPageWithEmptySchedule_whenInvalidStudentIdProvided() throws Exception {
        long studentId = 60L;
        when(scheduleServiceMock.getScheduleForStudent(studentId)).thenReturn(Optional.of(new ScheduleDto()));

        mockMvc.perform(get("/ui/v1/schedule"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("schedule"))
            .andExpect(view().name("schedule/calendar"));

        verify(scheduleServiceMock, times(1)).getScheduleForStudent(studentId);
    }

}
