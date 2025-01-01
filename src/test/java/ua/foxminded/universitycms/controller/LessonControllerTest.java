package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.ControllerTestConfig;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.model.enumeration.PermissionName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.LessonService;

@WebMvcTest(controllers = LessonController.class)
@ContextConfiguration(classes = {ControllerTestConfig.class})
@Import({SecurityConfig.class})
class LessonControllerTest {

    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonServiceMock;

    @Mock
    private CustomUserDetails customUserDetails;

    @Test
    void getCreationForm_shouldPageWithFormToCreateNewLesson_whenRequestIsValid() throws Exception {
        long studyDayId = 1;
        long scheduleId = 1;
        LocalDate date = LocalDate.now();
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));
        when(lessonServiceMock.getUserCourses(customUserDetails)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/ui/v1/lessons/new")
                .param("studyDayId", String.valueOf(studyDayId))
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("availableZoneIds"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("date"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(view().name("schedule/lesson-creation-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(customUserDetails);
    }

    @Test
    void performLessonCreation_shouldCreateNewLessonAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilledAndValidationWasSuccessful() throws Exception {
        long courseId = 1;
        long studyDayId = 1;
        long scheduleId = 1;
        long hoursToAdd = 1;
        LocalTime lessonStartTime = LocalTime.now();
        LocalTime lessonEndTime = LocalTime.now().plusHours(hoursToAdd);
        String timezone = "Timezone";
        LocalDate date = LocalDate.now();
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));

        mockMvc.perform(post("/ui/v1/lessons/create")
                .with(csrf())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("timezone", timezone)
                .param("course.id", String.valueOf(courseId))
                .param("studyDayId", String.valueOf(studyDayId))
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/study-days/%s?scheduleId=%s", date, scheduleId)));

        verify(lessonServiceMock, times(1)).save(any(LessonDto.class));
    }

    @Test
    void performLessonCreation_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilledAndValidationFails() throws Exception {
        long courseId = 1;
        long studyDayId = 1;
        long scheduleId = 1;
        LocalTime lessonStartTime = LocalTime.now();
        LocalDate date = LocalDate.now();
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));
        when(lessonServiceMock.getUserCourses(customUserDetails)).thenReturn(new HashMap<>());

        mockMvc.perform(post("/ui/v1/lessons/create")
                .with(csrf())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("course.id", String.valueOf(courseId))
                .param("studyDayId", String.valueOf(studyDayId))
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("date"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(view().name("schedule/lesson-creation-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(customUserDetails);
    }

    @Test
    void getUpdateForm_shouldPageWithFormToUpdateExistentLesson_whenLessonWithGivenIdExists() throws Exception {
        long lessonId = 1;
        long courseId = 1;
        long scheduleId = 1;
        LocalDate date = LocalDate.now();
        LessonDto lesson = mock(LessonDto.class);
        CourseDto course = mock(CourseDto.class);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));
        when(lessonServiceMock.getById(lessonId)).thenReturn(lesson);
        when(lessonServiceMock.getUserCourses(customUserDetails)).thenReturn(new HashMap<>());
        when(lesson.getCourse()).thenReturn(course);
        when(course.getId()).thenReturn(courseId);

        mockMvc.perform(get("/ui/v1/lessons/{lessonId}/edit", lessonId)
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("availableZoneIds"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("date"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(view().name("schedule/lesson-update-form"));

        verify(lessonServiceMock, times(1)).getById(lessonId);
        verify(lessonServiceMock, times(1)).getUserCourses(customUserDetails);
    }

    @Test
    void getUpdateForm_shouldReturnPageWithErrorMessage_whenLessonServiceThrowEntityNotFoundException() throws Exception {
        long lessonId = 1;
        long scheduleId = 1;
        LocalDate date = LocalDate.now();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(lessonServiceMock.getById(lessonId)).thenThrow(entityNotFoundExceptionMock);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(get("/ui/v1/lessons/{lessonId}/edit", lessonId)
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(lessonServiceMock, times(1)).getById(lessonId);
    }

    @Test
    void performLessonUpdate_shouldUpdateLessonAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilledAndValidationWasSuccessful() throws Exception {
        long lessonId = 1;
        long hoursToAdd = 1;
        long courseId = 1;
        long studyDayId = 1;
        long scheduleId = 1;
        LocalTime lessonStartTime = LocalTime.now();
        LocalTime lessonEndTime = LocalTime.now().plusHours(hoursToAdd);
        String timezone = "Timezone";
        LocalDate date = LocalDate.now();
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .param("id", String.valueOf(lessonId))
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("timezone", timezone)
                .param("course.id", String.valueOf(courseId))
                .param("studyDayId", String.valueOf(studyDayId))
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/study-days/%s?scheduleId=%s", date, scheduleId)));

        verify(lessonServiceMock, times(1)).update(any(LessonDto.class));
    }

    @Test
    void performLessonUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilledAndValidationFails() throws Exception {
        long lessonId = 1;
        long courseId = 1;
        long studyDayId = 1;
        long scheduleId = 1;
        LocalTime lessonStartTime = LocalTime.now();
        LocalDate date = LocalDate.now();
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .param("id", String.valueOf(lessonId))
                .param("lessonStartTime", lessonStartTime.toString())
                .param("course.id", String.valueOf(courseId))
                .param("studyDayId", String.valueOf(studyDayId))
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("availableZoneIds"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("date"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(view().name("schedule/lesson-update-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(customUserDetails);
    }

    @Test
    void performLessonUpdate_shouldReturnPageWithErrorMessage_whenLessonServiceThrowEntityNotFoundException() throws Exception {
        long hoursToAdd = 1;
        long courseId = 1;
        long studyDayId = 1;
        long scheduleId = 1;
        LocalTime lessonStartTime = LocalTime.now();
        LocalTime lessonEndTime = LocalTime.now().plusHours(hoursToAdd);
        String timezone = "Timezone";
        LocalDate date = LocalDate.now();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(lessonServiceMock.update(any(LessonDto.class))).thenThrow(entityNotFoundExceptionMock);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("timezone", timezone)
                .param("course.id", String.valueOf(courseId))
                .param("studyDayId", String.valueOf(studyDayId))
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId))
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(lessonServiceMock, times(1)).update(any(LessonDto.class));
    }

    @Test
    @WithMockUser(authorities = {"LESSONS_DELETE"})
    void performLessonDeletion_shouldDeleteLessonAndRedirectToAnotherUrl_whenLessonWithGivenIdExist() throws Exception {
        long lessonId = 1;
        long scheduleId = 1;
        LocalDate date = LocalDate.now();

        mockMvc.perform(delete("/ui/v1/lessons/{lessonId}/delete", lessonId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/study-days/%s?scheduleId=%s", date, scheduleId)));

        verify(lessonServiceMock, times(1)).deleteById(lessonId);
    }

    @Test
    @WithMockUser(authorities = {"LESSONS_DELETE"})
    void performTopicDeletion_shouldReturnPageWithErrorMessage_whenLessonServiceThrowEntityNotFoundException() throws Exception {
        long lessonId = 1L;
        long scheduleId = 1L;
        LocalDate date = LocalDate.now();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(lessonServiceMock).deleteById(lessonId);

        mockMvc.perform(delete("/ui/v1/lessons/{lessonId}/delete", lessonId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("date", date.toString())
                .param("scheduleId", String.valueOf(scheduleId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(lessonServiceMock, times(1)).deleteById(lessonId);
    }

}
