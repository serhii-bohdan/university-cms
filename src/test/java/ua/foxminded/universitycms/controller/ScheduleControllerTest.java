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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.InvalidUserRoleException;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.ScheduleService;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@WebMvcTest(controllers = {ScheduleController.class})
@Import({SecurityConfig.class})
class ScheduleControllerTest {

    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleServiceMock;

    @Mock
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserScheduleAndRedirectToLessonsListUrl_shouldRedirectToUrlWihLessonsList_whenRequestMadeForCorrectUrl() throws Exception {
        long scheduleId = 1L;
        LocalDate userCurrentLocalDate = LocalDate.now();
        String userCurrentLocalDateString = userCurrentLocalDate.toString();
        ScheduleDto scheduleMock = mock(ScheduleDto.class);
        when(scheduleMock.getId()).thenReturn(scheduleId);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SCHEDULE_READ")));
        when(scheduleServiceMock.getScheduleForUser(customUserDetails)).thenReturn(scheduleMock);
        when(scheduleServiceMock.getUserLocalDate(customUserDetails)).thenReturn(userCurrentLocalDate);

        mockMvc.perform(get("/ui/v1/schedule")
                .with(user(customUserDetails)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s"
                .formatted(scheduleId, userCurrentLocalDateString, userCurrentLocalDateString, userCurrentLocalDateString)));

        verify(scheduleServiceMock, times(1)).getScheduleForUser(customUserDetails);
        verify(scheduleServiceMock, times(1)).getUserLocalDate(customUserDetails);
    }

    @Test
    void getUserScheduleAndRedirectToLessonsListUrl_shouldPageWithErrorMessage_whenScheduleServiceThrowEntityNotFoundException() throws Exception {
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SCHEDULE_READ")));
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(scheduleServiceMock.getScheduleForUser(customUserDetails)).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/schedule")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(scheduleServiceMock, times(1)).getScheduleForUser(customUserDetails);
    }

    @Test
    void getUserScheduleAndRedirectToLessonsListUrl_shouldPageWithErrorMessage_whenScheduleServiceThrowInvalidUserRoleException() throws Exception {
        InvalidUserRoleException invalidUserRoleExceptionMock = mock(InvalidUserRoleException.class);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SCHEDULE_READ")));
        when(invalidUserRoleExceptionMock.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(invalidUserRoleExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(scheduleServiceMock.getScheduleForUser(customUserDetails)).thenThrow(invalidUserRoleExceptionMock);

        mockMvc.perform(get("/ui/v1/schedule")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(scheduleServiceMock, times(1)).getScheduleForUser(customUserDetails);
    }

    @Test
    @WithMockUser(authorities = {"SCHEDULE_READ"})
    void getSpecificScheduleAndRedirectToLessonsList_shouldRedirectToUrlWihLessonsList_whenRequestMadeForCorrectUrl() throws Exception {
        Long scheduleId = 1L;
        Long userId = 1L;
        String userFullName = "Full Name";
        String userRole = "MANAGER";
        String userLocationZoneOffset = "+00:00";
        LocalDate userCurrentLocalDate = LocalDate.now(ZoneOffset.of(userLocationZoneOffset));
        when(scheduleServiceMock.getUserLocalDate(userLocationZoneOffset)).thenReturn(userCurrentLocalDate);

        mockMvc.perform(get("/ui/v1/schedule/{scheduleId}", scheduleId)
                .param("userId", userId.toString())
                .param("userFullName", userFullName)
                .param("userRole", userRole)
                .param("userLocationZoneOffset", userLocationZoneOffset))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s&userId=%s&userFullName=%s&userRole=%s"
                .formatted(scheduleId, userCurrentLocalDate, userCurrentLocalDate, userCurrentLocalDate, userId, userFullName, userRole)));

        verify(scheduleServiceMock, times(1)).getUserLocalDate(userLocationZoneOffset);
    }

}
