package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import ua.foxminded.universitycms.dto.AdminCreationDto;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.AdminRepository;
import ua.foxminded.universitycms.service.AdminService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = {AdminController.class})
@ContextConfiguration(classes = {ControllerTestConfig.class})
@Import({SecurityConfig.class})
class AdminControllerTest {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private AdminService adminServiceMock;

    @Test
    @WithMockUser(authorities = {"ADMINS_READ"})
    void getPageWithAdmins_shouldReturnPageWithAdminsThatFoundByKeyword_whenKeywordNotNull() throws Exception {
        String keyword = "test@gmail.com";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(adminServiceMock.getAll()).thenReturn(getEmptyAdminsListForTest());
        when(adminServiceMock.findAdmins(pageable, keyword)).thenReturn(getEmptyAdminsPageForTest());

        mockMvc.perform(get("/ui/v1/admins")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("admins"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("admins/all-admins"));

        verify(adminServiceMock, times(1)).findAdmins(pageable, keyword);
        verify(adminServiceMock, times(1)).getAll();
        verify(adminServiceMock, times(1)).extractAdminEmails(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_READ"})
    void getPageWithAdmins_shouldReturnPageWithAllAdmins_whenKeywordIsNull() throws Exception {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(adminServiceMock.getAll()).thenReturn(getEmptyAdminsListForTest());
        when(adminServiceMock.findAdmins(pageable, null)).thenReturn(getEmptyAdminsPageForTest());

        mockMvc.perform(get("/ui/v1/admins"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("admins"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(view().name("admins/all-admins"));

        verify(adminServiceMock, times(1)).findAdmins(pageable, null);
        verify(adminServiceMock, times(1)).getAll();
        verify(adminServiceMock, times(1)).extractAdminEmails(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_READ"})
    void getPageWithAdmins_shouldReturnPageWithAllAdmins_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(adminServiceMock.getAll()).thenReturn(getEmptyAdminsListForTest());
        when(adminServiceMock.findAdmins(pageable, null)).thenReturn(getEmptyAdminsPageForTest());

        mockMvc.perform(get("/ui/v1/admins")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("admins"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(view().name("admins/all-admins"));

        verify(adminServiceMock, times(1)).findAdmins(pageable, null);
        verify(adminServiceMock, times(1)).getAll();
        verify(adminServiceMock, times(1)).extractAdminEmails(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_READ"})
    void getPageWithParticularAdmin_shouldPageWithParticularAdmin_whenAdminWithGivenIdExists() throws Exception {
        long adminId = 1L;
        AdminDto adminMock = mock(AdminDto.class);
        when(adminServiceMock.getById(adminId)).thenReturn(adminMock);

        mockMvc.perform(get("/ui/v1/admins/{adminId}", adminId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("admin"))
            .andExpect(view().name("admins/particular-admin"));

        verify(adminServiceMock, times(1)).getById(adminId);
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_READ"})
    void getPageWithParticularAdmin_shouldReturnPageWithErrorMessage_whenAdminServiceThrowEntityNotFoundException() throws Exception {
        long adminId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(adminServiceMock.getById(adminId)).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/admins/{adminId}", adminId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(adminServiceMock, times(1)).getById(adminId);
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_CREATE"})
    void getCreationForm_shouldPageWithFormToAddNewAdmin_whenRequestMadeForCorrectUrl() throws Exception {
        mockMvc.perform(get("/ui/v1/admins/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("admin"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(view().name("admins/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_CREATE"})
    void performAdminAdding_shouldAddNewAdminAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String password = "password";
        String locationZoneOffset = "+00:00";

        mockMvc.perform(post("/ui/v1/admins/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password)
                .param("locationZoneOffset", locationZoneOffset))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/admins"));

        verify(adminServiceMock, times(1)).save(any(AdminCreationDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_CREATE"})
    void performAdminAdding_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String password = "password";

        mockMvc.perform(post("/ui/v1/admins/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("password", password))
            .andExpect(status().isOk())
            .andExpect(view().name("admins/creation-form"));

        verify(adminServiceMock, never()).save(any(AdminCreationDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void getUpdateForm_shouldPageWithFormToUpdateExistentAdmin_whenAdminWithGivenIdExists() throws Exception {
        long adminId = 1L;
        AdminDto adminMock = mock(AdminDto.class);
        when(adminServiceMock.getById(adminId)).thenReturn(adminMock);

        mockMvc.perform(get("/ui/v1/admins/{adminId}/edit", adminId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("admin"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(view().name("admins/update-form"));

        verify(adminServiceMock, times(1)).getById(adminId);
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void getUpdateForm_shouldPageWithErrorMessage_whenAdminServiceThrowEntityNotFoundException() throws Exception {
        long adminId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(adminServiceMock.getById(adminId)).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/admins/{adminId}/edit", adminId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(adminServiceMock, times(1)).getById(adminId);
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void performAdminUpdate_shouldUpdateAdminAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        long adminId = 1L;
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String locationZoneOffset = "+00:00";

        mockMvc.perform(put("/ui/v1/admins/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(adminId))
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("locationZoneOffset", locationZoneOffset))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/admins/%s".formatted(adminId)));

        verify(adminServiceMock, times(1)).update(any(AdminDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void performAdminUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        long adminId = 1L;
        String lastName = "LastName";
        String email = "test@email.com";

        mockMvc.perform(put("/ui/v1/admins/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(adminId))
                .param("lastName", lastName)
                .param("email", email))
            .andExpect(status().isOk())
            .andExpect(view().name("admins/update-form"));

        verify(adminServiceMock, never()).update(any(AdminDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void performAdminUpdate_shouldReturnPageWithErrorMessage_whenAdminServiceThrowEntityNotFoundException() throws Exception {
        long adminId = 1;
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String locationZoneOffset = "+00:00";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(adminServiceMock.update(any(AdminDto.class))).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(put("/ui/v1/admins/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(adminId))
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("locationZoneOffset", locationZoneOffset))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(adminServiceMock, times(1)).update(any(AdminDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_DELETE"})
    void performAdminDeletion_shouldDeleteAdminAndRedirectToAnotherUrl_whenAdminWithGivenIdExist() throws Exception {
        long adminId = 1L;

        mockMvc.perform(delete("/ui/v1/admins/{adminId}/delete", adminId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/admins"));

        verify(adminServiceMock, times(1)).deleteById(adminId);
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_DELETE"})
    void performAdminDeletion_shouldReturnPageWithErrorMessage_whenAdminServiceThrowEntityNotFoundException() throws Exception {
        long adminId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(adminServiceMock).deleteById(adminId);

        mockMvc.perform(delete("/ui/v1/admins/{adminId}/delete", adminId)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(adminServiceMock, times(1)).deleteById(adminId);
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void getChangePasswordForm_shouldPageWithFormToUpdatePassword_whenRequestMadeForCorrectUrl() throws Exception {
        long adminId = 1L;

        mockMvc.perform(get("/ui/v1/admins/{adminId}/change-pass", adminId))
            .andExpect(model().attributeExists("passwordUpdateRequest"))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void performPasswordUpdate_shouldUpdateAdminPasswordAndRedirectToAnotherUrl_whenPasswordValidityRulesNotViolated() throws Exception {
        long adminId = 1L;
        RoleName roleName = RoleName.ADMIN;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        Admin adminMock = mock(Admin.class);
        AdminRepository adminRepositoryMock = context.getBean(AdminRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(adminRepositoryMock.findById(adminId)).thenReturn(Optional.of(adminMock));
        when(adminMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));

        mockMvc.perform(patch("/ui/v1/admins/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(adminId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/admins/%s".formatted(adminId)));

        verify(adminServiceMock, times(1)).updateAdminPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void performPasswordUpdate_shouldPageWithPasswordUpdateForm_whenPasswordValidationRulesAreViolated() throws Exception {
        long adminId = 1L;
        RoleName roleName = RoleName.ADMIN;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "           ";
        Admin adminMock = mock(Admin.class);
        AdminRepository adminRepositoryMock = context.getBean(AdminRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(adminRepositoryMock.findById(adminId)).thenReturn(Optional.of(adminMock));
        when(adminMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));

        mockMvc.perform(patch("/ui/v1/admins/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(adminId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));

        verify(adminServiceMock, never()).updateAdminPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMINS_UPDATE"})
    void performPasswordUpdate_shouldReturnPageWithErrorMessage_whenAdminServiceThrowUserNotFoundException() throws Exception {
        long adminId = 1L;
        RoleName roleName = RoleName.ADMIN;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        Admin adminMock = mock(Admin.class);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        UserNotFoundException userNotFoundExceptionMock = mock(UserNotFoundException.class);
        AdminRepository adminRepositoryMock = context.getBean(AdminRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(adminRepositoryMock.findById(adminId)).thenReturn(Optional.of(adminMock));
        when(adminMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));
        when(userNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(userNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(userNotFoundExceptionMock).when(adminServiceMock).updateAdminPassword(any(PasswordUpdateRequestDto.class));

        mockMvc.perform(patch("/ui/v1/admins/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(adminId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(adminServiceMock, times(1)).updateAdminPassword(any(PasswordUpdateRequestDto.class));
    }

    private List<AdminDto> getEmptyAdminsListForTest() {
        return new ArrayList<>();
    }

    private Page<AdminDto> getEmptyAdminsPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
