package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.ScheduleService;
import java.util.*;

@WebMvcTest(controllers = ScheduleController.class)
@Import(SecurityConfig.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleServiceMock;

    @Mock
    private CustomUserDetails customUserDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPageWithCalendar_shouldReturnPageWithScheduleForStudent_whenLoggedInUserHasStudentRole() throws Exception {
        long studentId = 1L;
        long scheduleId = 1L;
        ScheduleDto studentSchedule = ScheduleDto.builder()
            .id(scheduleId)
            .build();
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SCHEDULE_READ")));
        when(scheduleServiceMock.getScheduleForStudent(studentId)).thenReturn(Optional.of(studentSchedule));

        mockMvc.perform(get("/ui/v1/schedule")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("schedule"))
            .andExpect(view().name("schedule/calendar"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(scheduleServiceMock, times(1)).getScheduleForStudent(studentId);
    }

    @Test
    void getPageWithCalendar_shouldPageWithScheduleForTeacher_whenLoggedInUserHasTeacherRole() throws Exception {
        long teacherId = 1L;
        long scheduleId = 1L;
        ScheduleDto teacherSchedule = ScheduleDto.builder()
            .id(scheduleId)
            .build();
        when(customUserDetails.getId()).thenReturn(teacherId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.TEACHER);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SCHEDULE_READ")));
        when(scheduleServiceMock.getScheduleForTeacher(teacherId)).thenReturn(Optional.of(teacherSchedule));

        mockMvc.perform(get("/ui/v1/schedule")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("schedule"))
            .andExpect(view().name("schedule/calendar"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(scheduleServiceMock, times(1)).getScheduleForTeacher(teacherId);
    }

    @Test
    void getPageWithCalendar_shouldForbiddenError_whenLoggedInUserDoesNotHaveRequiredPermission() throws Exception {
        long userId = 1L;
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SOME_PERMISSION")));

        mockMvc.perform(get("/ui/v1/schedule")
                .with(user(customUserDetails)))
            .andExpect(status().isForbidden());

        verify(customUserDetails, never()).getId();
        verify(customUserDetails, never()).getRoleName();
        verify(scheduleServiceMock, never()).getScheduleForTeacher(userId);
        verify(scheduleServiceMock, never()).getScheduleForStudent(userId);
    }

}
