package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.ControllerTestConfig;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = TeacherController.class)
@ContextConfiguration(classes = ControllerTestConfig.class)
@Import(SecurityConfig.class)
class TeacherControllerTest {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private TeacherService teacherServiceMock;

    @Test
    @WithMockUser(authorities = {"TEACHERS_READ"})
    void getPageWithTeachers_shouldReturnPageWithTeachersThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "FirstName1 LastName1";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAll()).thenReturn(getAllTeachersForTest());
        when(teacherServiceMock.getTeacherInPageByEmail(keyword, pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeacherInPageByEmail(keyword, pageable);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_READ"})
    void getPageWithTeachers_shouldReturnPageWithAllTeachers_whenKeywordIsNull() throws Exception {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAll()).thenReturn(getAllTeachersForTest());
        when(teacherServiceMock.getTeachersPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeachersPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_READ"})
    void getPageWithTeachers_shouldReturnPageWithAllTeachers_whenKeywordIsBlank() throws Exception {
        String keyword = "               ";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAll()).thenReturn(getAllTeachersForTest());
        when(teacherServiceMock.getTeachersPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", ""))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeachersPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_READ"})
    void getPageWithTeachers_shouldReturnPageWithAllTeachers_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAll()).thenReturn(getAllTeachersForTest());
        when(teacherServiceMock.getTeachersPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeachersPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_READ"})
    void getPageWithParticularTeacher_shouldPageWithParticularTeacher_whenTeacherWithGivenIdExists() throws Exception {
        long teacherId = 1L;
        TeacherDto teacherMock = mock(TeacherDto.class);
        when(teacherServiceMock.getById(teacherId)).thenReturn(teacherMock);

        mockMvc.perform(get("/ui/v1/teachers/{teacherId}", teacherId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teacher"))
            .andExpect(view().name("teachers/particular-teacher"));

        verify(teacherServiceMock, times(1)).getById(teacherId);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_READ"})
    void getPageWithParticularTeacher_shouldReturnPageWithErrorMessage_whenTeacherServiceThrowEntityNotFoundException() throws Exception {
        long teacherId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(teacherServiceMock.getById(teacherId)).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/teachers/{teacherId}", teacherId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(teacherServiceMock, times(1)).getById(teacherId);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_CREATE"})
    void getCreationForm_shouldPageWithFormToAddNewTeacher_whenRequestMadeForCorrectUrl() throws Exception {
        mockMvc.perform(get("/ui/v1/teachers/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teacher"))
            .andExpect(view().name("teachers/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_CREATE"})
    void performTeacherAdding_shouldAddNewTeacherAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String password = "password";
        Boolean isActive = true;

        mockMvc.perform(post("/ui/v1/teachers/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password)
                .param("isActive", String.valueOf(isActive)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/teachers"));

        verify(teacherServiceMock, times(1)).save(any(TeacherCreationDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_CREATE"})
    void performTeacherAdding_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String password = "password";
        Boolean isActive = true;

        mockMvc.perform(post("/ui/v1/teachers/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("password", password)
                .param("isActive", String.valueOf(isActive)))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/creation-form"));

        verify(teacherServiceMock, never()).save(any(TeacherCreationDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void getUpdateForm_shouldPageWithFormToUpdateExistentTeacher_whenTeacherWithGivenIdExists() throws Exception {
        long teacherId = 1L;
        TeacherDto teacherMock = mock(TeacherDto.class);
        when(teacherServiceMock.getById(teacherId)).thenReturn(teacherMock);

        mockMvc.perform(get("/ui/v1/teachers/{teacherId}/edit", teacherId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teacher"))
            .andExpect(view().name("teachers/update-form"));

        verify(teacherServiceMock, times(1)).getById(teacherId);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void getUpdateForm_shouldPageWithErrorMessage_whenTeacherServiceThrowEntityNotFoundException() throws Exception {
        long teacherId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(teacherServiceMock.getById(teacherId)).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/teachers/{teacherId}/edit", teacherId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(teacherServiceMock, times(1)).getById(teacherId);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void performTeacherUpdate_shouldUpdateTeacherAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        long teacherId = 1L;
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        Boolean isActive = true;

        mockMvc.perform(put("/ui/v1/teachers/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(teacherId))
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("isActive", String.valueOf(isActive)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/teachers/%s", teacherId)));

        verify(teacherServiceMock, times(1)).update(any(TeacherDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void performTeacherUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";

        mockMvc.perform(put("/ui/v1/teachers/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/update-form"));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void performTeacherUpdate_shouldReturnPageWithErrorMessage_whenTeacherServiceThrowEntityNotFoundException() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        Boolean isActive = true;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(teacherServiceMock.update(any(TeacherDto.class))).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(put("/ui/v1/teachers/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("isActive", String.valueOf(isActive)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(teacherServiceMock, times(1)).update(any(TeacherDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_DELETE"})
    void performTeacherDeletion_shouldDeleteTeacherAndRedirectToAnotherUrl_whenTeacherWithGivenIdExist() throws Exception {
        long teacherId = 1L;

        mockMvc.perform(delete("/ui/v1/teachers/{teacherId}/delete", teacherId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/teachers"));

        verify(teacherServiceMock, times(1)).deleteById(teacherId);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_DELETE"})
    void performTeacherDeletion_shouldReturnPageWithErrorMessage_whenTeacherServiceThrowEntityNotFoundException() throws Exception {
        long teacherId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(teacherServiceMock).deleteById(teacherId);

        mockMvc.perform(delete("/ui/v1/teachers/{teacherId}/delete", teacherId)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(teacherServiceMock, times(1)).deleteById(teacherId);
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void getChangePasswordForm_shouldPageWithFormToUpdatePassword_whenRequestMadeForCorrectUrl() throws Exception {
        long teacherId = 1L;

        mockMvc.perform(get("/ui/v1/teachers/{teacherId}/change-pass", teacherId))
            .andExpect(model().attributeExists("passwordUpdateRequest"))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void performPasswordUpdate_shouldUpdateTeacherPasswordAndRedirectToAnotherUrl_whenPasswordValidityRulesNotViolated() throws Exception {
        long teacherId = 1L;
        RoleName roleName = RoleName.TEACHER;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        Teacher teacherMock = mock(Teacher.class);
        TeacherRepository teacherRepositoryMock = context.getBean(TeacherRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(teacherRepositoryMock.findById(teacherId)).thenReturn(Optional.of(teacherMock));
        when(teacherMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));

        mockMvc.perform(patch("/ui/v1/teachers/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(teacherId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/teachers/%s", teacherId)));

        verify(teacherServiceMock, times(1)).updateTeacherPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void performPasswordUpdate_shouldPageWithPasswordUpdateForm_whenPasswordValidationRulesAreViolated() throws Exception {
        long teacherId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "           ";

        mockMvc.perform(patch("/ui/v1/teachers/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(teacherId))
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));
    }

    @Test
    @WithMockUser(authorities = {"TEACHERS_UPDATE"})
    void performPasswordUpdate_shouldReturnPageWithErrorMessage_whenTeacherServiceThrowEntityNotFoundException() throws Exception {
        long teacherId = 1L;
        RoleName roleName = RoleName.TEACHER;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        Teacher teacherMock = mock(Teacher.class);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        TeacherRepository teacherRepositoryMock = context.getBean(TeacherRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(teacherRepositoryMock.findById(teacherId)).thenReturn(Optional.of(teacherMock));
        when(teacherMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(teacherServiceMock).updateTeacherPassword(any(PasswordUpdateRequestDto.class));

        mockMvc.perform(patch("/ui/v1/teachers/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(teacherId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(teacherServiceMock, times(1)).updateTeacherPassword(any(PasswordUpdateRequestDto.class));
    }

    private List<TeacherDto> getAllTeachersForTest() {
        return new ArrayList<>();
    }

    private Page<TeacherDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
