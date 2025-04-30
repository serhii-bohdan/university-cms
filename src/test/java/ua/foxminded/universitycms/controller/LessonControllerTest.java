package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.ControllerTestConfig;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.model.enumeration.PermissionName;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.LessonService;

@WebMvcTest(controllers = {LessonController.class})
@ContextConfiguration(classes = {ControllerTestConfig.class})
@Import({SecurityConfig.class})
class LessonControllerTest {

    private static final String ERROR_MESSAGE = "Error message.";
    private static final String ZONE_OFFSET_STRING_FOR_TEST = "+00:00";
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of(ZONE_OFFSET_STRING_FOR_TEST);
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonServiceMock;

    @Mock
    private CustomUserDetails customUserDetails;

    @Test
    void getFilteredScheduleLessons_shouldReturnPageWithFilteredLessons_whenRequestIsValidAndUserHaveTeacherOrStudentRole() throws Exception {
        long scheduleId = 1;
        LocalDate userCurrentLocalDate = LocalDate.now();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, "date"));
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_READ.name())));
        when(lessonServiceMock.findLessonsByScheduleIdAndDateRange(scheduleId, userCurrentLocalDate, userCurrentLocalDate, pageable)).thenReturn(getEmptyLessonsPageForTest());

        mockMvc.perform(get("/ui/v1/lessons")
                .with(user(customUserDetails))
                .param("scheduleId", Long.toString(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lessons"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeDoesNotExist("userId"))
            .andExpect(model().attributeDoesNotExist("userFullName"))
            .andExpect(model().attributeDoesNotExist("userRole"))
            .andExpect(view().name("schedule/educator-lessons"));

        verify(lessonServiceMock, times(1)).findLessonsByScheduleIdAndDateRange(scheduleId, userCurrentLocalDate, userCurrentLocalDate, pageable);
    }

    @Test
    void getFilteredScheduleLessons_shouldReturnPageWithFilteredLessons_whenRequestIsValidAndUserHaveManagerRole() throws Exception {
        long scheduleId = 1;
        long userId = 1;
        String userFullName = "Full Name";
        String userRole = "MANAGER";
        String userLocationZoneOffset = "+00:00";
        LocalDate userCurrentLocalDate = LocalDate.now(ZoneOffset.of(userLocationZoneOffset));
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, "date"));
        when(customUserDetails.getRoleName()).thenReturn(RoleName.MANAGER);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_READ.name())));
        when(lessonServiceMock.findLessonsByScheduleIdAndDateRange(scheduleId, userCurrentLocalDate, userCurrentLocalDate, pageable)).thenReturn(getEmptyLessonsPageForTest());

        mockMvc.perform(get("/ui/v1/lessons")
                .with(user(customUserDetails))
                .param("scheduleId", Long.toString(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userId", Long.toString(userId))
                .param("userFullName", userFullName)
                .param("userRole", userRole))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lessons"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userFullName"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(view().name("schedule/educator-lessons"));

        verify(lessonServiceMock, times(1)).findLessonsByScheduleIdAndDateRange(scheduleId, userCurrentLocalDate, userCurrentLocalDate, pageable);
    }

    @Test
    @WithMockUser(authorities = {"LESSONS_READ"})
    void getFilteredScheduleLessons_shouldReturnPageWithFilteredLessons_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        long scheduleId = 1;
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        LocalDate userCurrentLocalDate = LocalDate.now();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, "date"));
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_READ.name())));
        when(lessonServiceMock.findLessonsByScheduleIdAndDateRange(scheduleId, userCurrentLocalDate, userCurrentLocalDate, pageable)).thenReturn(getEmptyLessonsPageForTest());

        mockMvc.perform(get("/ui/v1/lessons")
                .with(user(customUserDetails))
                .param("scheduleId", Long.toString(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lessons"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attributeExists("scheduleId"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(view().name("schedule/educator-lessons"));

        verify(lessonServiceMock, times(1)).findLessonsByScheduleIdAndDateRange(scheduleId, userCurrentLocalDate, userCurrentLocalDate, pageable);
    }

    @Test
    void getCreationForm_shouldPageWithFormToCreateNewLesson_whenRequestIsValidAndUserHaveTeacherOrStudentRole() throws Exception {
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.STUDENT;
        LocalDate userCurrentLocalDate = LocalDate.now();
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));
        when(lessonServiceMock.getUserCourses(userId, userRole)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/ui/v1/lessons/new")
                .with(user(customUserDetails))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeDoesNotExist("userFullName"))
            .andExpect(view().name("schedule/lesson-creation-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void getCreationForm_shouldPageWithFormToCreateNewLesson_whenRequestIsValidAndUserHaveManagerRole() throws Exception {
        long scheduleId = 1;
        long userId = 1;
        String userFullName = "Full Name";
        RoleName userRole = RoleName.MANAGER;
        LocalDate userCurrentLocalDate = LocalDate.now();
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));
        when(lessonServiceMock.getUserCourses(userId, userRole)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/ui/v1/lessons/new")
                .with(user(customUserDetails))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userFullName", userFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeExists("userFullName"))
            .andExpect(view().name("schedule/lesson-creation-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void performLessonCreation_shouldCreateNewLessonAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilledAndValidationWasSuccessfulAndUserHaveTeacherOrStudentRole() throws Exception {
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.TEACHER;
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        LocalTime lessonEndTime = lessonStartTime.plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));

        mockMvc.perform(post("/ui/v1/lessons/create")
                .with(csrf())
                .with(user(customUserDetails))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("zoneOffset", ZONE_OFFSET_STRING_FOR_TEST)
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", String.valueOf(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s"
                .formatted(scheduleId, userCurrentLocalDate, userCurrentLocalDate, userCurrentLocalDate)));

        verify(lessonServiceMock, times(1)).save(any(LessonDto.class));
    }

    @Test
    void performLessonCreation_shouldCreateNewLessonAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilledAndValidationWasSuccessfulAndUserHaveManagerRole() throws Exception {
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        String userFullName = "FullName";
        RoleName userRole = RoleName.MANAGER;
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        LocalTime lessonEndTime = lessonStartTime.plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));

        mockMvc.perform(post("/ui/v1/lessons/create")
                .with(csrf())
                .with(user(customUserDetails))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("zoneOffset", ZONE_OFFSET_STRING_FOR_TEST)
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", String.valueOf(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userFullName", userFullName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s&userId=%s&userFullName=%s&userRole=%s"
                .formatted(scheduleId, userCurrentLocalDate, userCurrentLocalDate, userCurrentLocalDate, userId, userFullName, userRole)));

        verify(lessonServiceMock, times(1)).save(any(LessonDto.class));
    }

    @Test
    void performLessonCreation_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilledAndValidationFailsAndUserHaveTeacherOrStudentRole() throws Exception {
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.TEACHER;
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));
        when(lessonServiceMock.getUserCourses(userId, userRole)).thenReturn(new HashMap<>());

        mockMvc.perform(post("/ui/v1/lessons/create")
                .with(csrf())
                .with(user(customUserDetails))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeDoesNotExist("userFullName"))
            .andExpect(view().name("schedule/lesson-creation-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void performLessonCreation_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilledAndValidationFailsAndUserHaveManagerRole() throws Exception {
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        String userFullName = "FullName";
        RoleName userRole = RoleName.MANAGER;
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_CREATE.name())));
        when(lessonServiceMock.getUserCourses(userId, userRole)).thenReturn(new HashMap<>());

        mockMvc.perform(post("/ui/v1/lessons/create")
                .with(csrf())
                .with(user(customUserDetails))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userFullName", userFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeExists("userFullName"))
            .andExpect(view().name("schedule/lesson-creation-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void getUpdateForm_shouldPageWithFormToUpdateExistentLesson_whenLessonWithGivenIdExistsAndUserHaveTeacherOrStudentRole() throws Exception {
        long lessonId = 1;
        long userId = 1;
        RoleName userRole = RoleName.TEACHER;
        LessonDto lesson = mock(LessonDto.class);
        LocalDate userCurrentLocalDate = LocalDate.now();
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));
        when(lessonServiceMock.getById(lessonId)).thenReturn(lesson);
        when(lessonServiceMock.getUserCourses(userId, userRole)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/ui/v1/lessons/{lessonId}/edit", lessonId)
                .with(user(customUserDetails))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeDoesNotExist("userFullName"))
            .andExpect(view().name("schedule/lesson-update-form"));

        verify(lessonServiceMock, times(1)).getById(lessonId);
        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void getUpdateForm_shouldPageWithFormToUpdateExistentLesson_whenLessonWithGivenIdExistsAndUserHaveManagerRole() throws Exception {
        long lessonId = 1;
        long userId = 1;
        RoleName userRole = RoleName.MANAGER;
        String userFullName = "Full Name";
        LessonDto lesson = mock(LessonDto.class);
        LocalDate userCurrentLocalDate = LocalDate.now();
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));
        when(lessonServiceMock.getById(lessonId)).thenReturn(lesson);
        when(lessonServiceMock.getUserCourses(userId, userRole)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/ui/v1/lessons/{lessonId}/edit", lessonId)
                .with(user(customUserDetails))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userFullName", userFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeExists("userFullName"))
            .andExpect(view().name("schedule/lesson-update-form"));

        verify(lessonServiceMock, times(1)).getById(lessonId);
        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void getUpdateForm_shouldReturnPageWithErrorMessage_whenLessonServiceThrowEntityNotFoundExceptionAndUserHaveTeacherOrStudentRole() throws Exception {
        long lessonId = 1;
        long userId = 1;
        RoleName userRole = RoleName.MANAGER;
        LocalDate userCurrentLocalDate = LocalDate.now();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(lessonServiceMock.getById(lessonId)).thenThrow(entityNotFoundExceptionMock);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(get("/ui/v1/lessons/{lessonId}/edit", lessonId)
                .with(user(customUserDetails))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(lessonServiceMock, times(1)).getById(lessonId);
    }

    @Test
    void performLessonUpdate_shouldUpdateLessonAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilledAndValidationWasSuccessfulAndUserHaveTeacherOrStudentRole() throws Exception {
        long lessonId = 1;
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.TEACHER;
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        LocalTime lessonEndTime = lessonStartTime.plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .with(user(customUserDetails))
                .param("id", String.valueOf(lessonId))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("zoneOffset", ZONE_OFFSET_STRING_FOR_TEST)
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s"
                .formatted(scheduleId, userCurrentLocalDate, userCurrentLocalDate, userCurrentLocalDate)));

        verify(lessonServiceMock, times(1)).update(any(LessonDto.class));
    }

    @Test
    void performLessonUpdate_shouldUpdateLessonAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilledAndValidationWasSuccessfulAndUserHaveManagerRole() throws Exception {
        long lessonId = 1;
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.MANAGER;
        String userFullName = "Full Name";
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        LocalTime lessonEndTime = lessonStartTime.plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .with(user(customUserDetails))
                .param("id", String.valueOf(lessonId))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("zoneOffset", ZONE_OFFSET_STRING_FOR_TEST)
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userFullName", userFullName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s&userId=%s&userFullName=%s&userRole=%s"
                .formatted(scheduleId, userCurrentLocalDate, userCurrentLocalDate, userCurrentLocalDate, userId, userFullName, userRole)));

        verify(lessonServiceMock, times(1)).update(any(LessonDto.class));
    }

    @Test
    void performLessonUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilledAndValidationFailsAndUserHaveTeacherOrStudentRole() throws Exception {
        long lessonId = 1;
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.STUDENT;
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .with(user(customUserDetails))
                .param("id", String.valueOf(lessonId))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeDoesNotExist("userFullName"))
            .andExpect(view().name("schedule/lesson-update-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void performLessonUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilledAndValidationFailsAndUserHaveManagerRole() throws Exception {
        long lessonId = 1;
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.MANAGER;
        String userFullName = "Full Name";
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .with(user(customUserDetails))
                .param("id", String.valueOf(lessonId))
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userFullName", userFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("lesson"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(model().attributeExists("userId"))
            .andExpect(model().attributeExists("userRole"))
            .andExpect(model().attributeExists("userCurrentDate"))
            .andExpect(model().attributeExists("startDate"))
            .andExpect(model().attributeExists("endDate"))
            .andExpect(model().attributeExists("userFullName"))
            .andExpect(view().name("schedule/lesson-update-form"));

        verify(lessonServiceMock, times(1)).getUserCourses(userId, userRole);
    }

    @Test
    void performLessonUpdate_shouldReturnPageWithErrorMessage_whenLessonServiceThrowEntityNotFoundExceptionAndUserHaveTeacherOrStudentRole() throws Exception {
        long courseId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.STUDENT;
        long minutesToAdd = 10;
        LocalDate userCurrentLocalDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZONE_OFFSET);
        LocalTime lessonStartTime = zonedDateTime.toLocalTime().plusMinutes(minutesToAdd);
        LocalTime lessonEndTime = lessonStartTime.plusMinutes(minutesToAdd);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(lessonServiceMock.update(any(LessonDto.class))).thenThrow(entityNotFoundExceptionMock);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_UPDATE.name())));

        mockMvc.perform(put("/ui/v1/lessons/update")
                .with(csrf())
                .param("date", zonedDateTime.toLocalDate().toString())
                .param("lessonStartTime", lessonStartTime.toString())
                .param("lessonEndTime", lessonEndTime.toString())
                .param("zoneOffset", ZONE_OFFSET_STRING_FOR_TEST)
                .param("courseId", String.valueOf(courseId))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(lessonServiceMock, times(1)).update(any(LessonDto.class));
    }

    @Test
    void performLessonDeletion_shouldDeleteLessonAndRedirectToAnotherUrl_whenLessonWithGivenIdExistAndUserHaveTeacherOrStudentRole() throws Exception {
        long lessonId = 1;
        long scheduleId = 1;
        RoleName userRole = RoleName.STUDENT;
        LocalDate userCurrentLocalDate = LocalDate.now();
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_DELETE.name())));

        mockMvc.perform(delete("/ui/v1/lessons/{lessonId}/delete", lessonId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .with(user(customUserDetails))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s"
                .formatted(scheduleId, userCurrentLocalDate, userCurrentLocalDate, userCurrentLocalDate)));

        verify(lessonServiceMock, times(1)).deleteById(lessonId);
    }

    @Test
    void performLessonDeletion_shouldDeleteLessonAndRedirectToAnotherUrl_whenLessonWithGivenIdExistAndUserHaveManagerRole() throws Exception {
        long lessonId = 1;
        long scheduleId = 1;
        long userId = 1;
        RoleName userRole = RoleName.MANAGER;
        String userFullName = "Full Name";
        LocalDate userCurrentLocalDate = LocalDate.now();
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_DELETE.name())));

        mockMvc.perform(delete("/ui/v1/lessons/{lessonId}/delete", lessonId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .with(user(customUserDetails))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString())
                .param("userId", Long.toString(userId))
                .param("userRole", userRole.name())
                .param("userFullName", userFullName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/lessons?scheduleId=%s&userCurrentLocalDate=%s&startDate=%s&endDate=%s&userId=%s&userFullName=%s&userRole=%s"
                .formatted(scheduleId, userCurrentLocalDate, userCurrentLocalDate, userCurrentLocalDate, userId, userFullName, userRole)));

        verify(lessonServiceMock, times(1)).deleteById(lessonId);
    }

    @Test
    void performTopicDeletion_shouldReturnPageWithErrorMessage_whenLessonServiceThrowEntityNotFoundExceptionAndUserHaveStudentRole() throws Exception {
        long lessonId = 1L;
        long scheduleId = 1;
        RoleName userRole = RoleName.STUDENT;
        LocalDate userCurrentLocalDate = LocalDate.now();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(customUserDetails.getRoleName()).thenReturn(userRole);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority(PermissionName.LESSONS_DELETE.name())));
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(lessonServiceMock).deleteById(lessonId);

        mockMvc.perform(delete("/ui/v1/lessons/{lessonId}/delete", lessonId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .with(user(customUserDetails))
                .param("scheduleId", String.valueOf(scheduleId))
                .param("userCurrentLocalDate", userCurrentLocalDate.toString())
                .param("startDate", userCurrentLocalDate.toString())
                .param("endDate", userCurrentLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(lessonServiceMock, times(1)).deleteById(lessonId);
    }

    private Page<LessonDto> getEmptyLessonsPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
