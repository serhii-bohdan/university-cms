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
import ua.foxminded.universitycms.dto.ManagerCreationDto;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.ManagerRepository;
import ua.foxminded.universitycms.service.ManagerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = {ManagerController.class})
@ContextConfiguration(classes = {ControllerTestConfig.class})
@Import({SecurityConfig.class})
class ManagerControllerTest {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private ManagerService managerServiceMock;

    @Test
    @WithMockUser(authorities = {"MANAGERS_READ"})
    void getPageWithManagers_shouldReturnPageWithManagersThatFoundByKeyword_whenKeywordNotNull() throws Exception {
        String keyword = "test@gmail.com";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(managerServiceMock.getAll()).thenReturn(getEmptyManagersListForTest());
        when(managerServiceMock.findManagers(pageable, keyword)).thenReturn(getEmptyManagersPageForTest());

        mockMvc.perform(get("/ui/v1/managers")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("managers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("managers/all-managers"));

        verify(managerServiceMock, times(1)).findManagers(pageable, keyword);
        verify(managerServiceMock, times(1)).getAll();
        verify(managerServiceMock, times(1)).extractManagerEmails(any());
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_READ"})
    void getPageWithManagers_shouldReturnPageWithAllManagers_whenKeywordIsNull() throws Exception {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(managerServiceMock.getAll()).thenReturn(getEmptyManagersListForTest());
        when(managerServiceMock.findManagers(pageable, null)).thenReturn(getEmptyManagersPageForTest());

        mockMvc.perform(get("/ui/v1/managers"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("managers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(view().name("managers/all-managers"));

        verify(managerServiceMock, times(1)).findManagers(pageable, null);
        verify(managerServiceMock, times(1)).getAll();
        verify(managerServiceMock, times(1)).extractManagerEmails(any());
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_READ"})
    void getPageWithManagers_shouldReturnPageWithAllManagers_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(managerServiceMock.getAll()).thenReturn(getEmptyManagersListForTest());
        when(managerServiceMock.findManagers(pageable, null)).thenReturn(getEmptyManagersPageForTest());

        mockMvc.perform(get("/ui/v1/managers")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("managers"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(view().name("managers/all-managers"));

        verify(managerServiceMock, times(1)).findManagers(pageable, null);
        verify(managerServiceMock, times(1)).getAll();
        verify(managerServiceMock, times(1)).extractManagerEmails(any());
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_READ"})
    void getPageWithParticularManager_shouldPageWithParticularManager_whenManagerWithGivenIdExists() throws Exception {
        long managerId = 1L;
        ManagerDto managerMock = mock(ManagerDto.class);
        when(managerServiceMock.getById(managerId)).thenReturn(managerMock);

        mockMvc.perform(get("/ui/v1/managers/{managerId}", managerId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("manager"))
            .andExpect(view().name("managers/particular-manager"));

        verify(managerServiceMock, times(1)).getById(managerId);
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_READ"})
    void getPageWithParticularManager_shouldReturnPageWithErrorMessage_whenManagerServiceThrowEntityNotFoundException() throws Exception {
        long managerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(managerServiceMock.getById(managerId)).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/managers/{managerId}", managerId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(managerServiceMock, times(1)).getById(managerId);
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_CREATE"})
    void getCreationForm_shouldPageWithFormToAddNewManager_whenRequestMadeForCorrectUrl() throws Exception {
        mockMvc.perform(get("/ui/v1/managers/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("manager"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(view().name("managers/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_CREATE"})
    void performManagerAdding_shouldAddNewManagerAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String password = "password";
        String locationZoneOffset = "+00:00";

        mockMvc.perform(post("/ui/v1/managers/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password)
                .param("locationZoneOffset", locationZoneOffset))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/managers"));

        verify(managerServiceMock, times(1)).save(any(ManagerCreationDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_CREATE"})
    void performManagerAdding_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String password = "password";

        mockMvc.perform(post("/ui/v1/managers/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("password", password))
            .andExpect(status().isOk())
            .andExpect(view().name("managers/creation-form"));

        verify(managerServiceMock, never()).save(any(ManagerCreationDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void getUpdateForm_shouldPageWithFormToUpdateExistentManager_whenManagerWithGivenIdExists() throws Exception {
        long managerId = 1L;
        ManagerDto managerMock = mock(ManagerDto.class);
        when(managerServiceMock.getById(managerId)).thenReturn(managerMock);

        mockMvc.perform(get("/ui/v1/managers/{managerId}/edit", managerId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("manager"))
            .andExpect(model().attributeExists("timeZones"))
            .andExpect(view().name("managers/update-form"));

        verify(managerServiceMock, times(1)).getById(managerId);
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void getUpdateForm_shouldPageWithErrorMessage_whenManagerServiceThrowEntityNotFoundException() throws Exception {
        long managerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(managerServiceMock.getById(managerId)).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/managers/{managerId}/edit", managerId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(managerServiceMock, times(1)).getById(managerId);
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void performManagerUpdate_shouldUpdateManagerAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        long managerId = 1L;
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String locationZoneOffset = "+00:00";

        mockMvc.perform(put("/ui/v1/managers/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(managerId))
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("locationZoneOffset", locationZoneOffset))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/managers/%s".formatted(managerId)));

        verify(managerServiceMock, times(1)).update(any(ManagerDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void performManagerUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        long managerId = 1L;
        String lastName = "LastName";
        String email = "test@email.com";

        mockMvc.perform(put("/ui/v1/managers/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(managerId))
                .param("lastName", lastName)
                .param("email", email))
            .andExpect(status().isOk())
            .andExpect(view().name("managers/update-form"));

        verify(managerServiceMock, never()).update(any(ManagerDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void performManagerUpdate_shouldReturnPageWithErrorMessage_whenManagerServiceThrowEntityNotFoundException() throws Exception {
        long managerId = 1;
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String locationZoneOffset = "+00:00";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(managerServiceMock.update(any(ManagerDto.class))).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(put("/ui/v1/managers/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(managerId))
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("locationZoneOffset", locationZoneOffset))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(managerServiceMock, times(1)).update(any(ManagerDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_DELETE"})
    void performManagerDeletion_shouldDeleteManagerAndRedirectToAnotherUrl_whenManagerWithGivenIdExist() throws Exception {
        long managerId = 1L;

        mockMvc.perform(delete("/ui/v1/managers/{managerId}/delete", managerId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/managers"));

        verify(managerServiceMock, times(1)).deleteById(managerId);
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_DELETE"})
    void performManagerDeletion_shouldReturnPageWithErrorMessage_whenManagerServiceThrowEntityNotFoundException() throws Exception {
        long managerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(managerServiceMock).deleteById(managerId);

        mockMvc.perform(delete("/ui/v1/managers/{managerId}/delete", managerId)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(managerServiceMock, times(1)).deleteById(managerId);
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void getChangePasswordForm_shouldPageWithFormToUpdatePassword_whenRequestMadeForCorrectUrl() throws Exception {
        long managerId = 1L;

        mockMvc.perform(get("/ui/v1/managers/{managerId}/change-pass", managerId))
            .andExpect(model().attributeExists("passwordUpdateRequest"))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void performPasswordUpdate_shouldUpdateManagerPasswordAndRedirectToAnotherUrl_whenPasswordValidityRulesNotViolated() throws Exception {
        long managerId = 1L;
        RoleName roleName = RoleName.MANAGER;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        Manager managerMock = mock(Manager.class);
        ManagerRepository managerRepositoryMock = context.getBean(ManagerRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(managerRepositoryMock.findById(managerId)).thenReturn(Optional.of(managerMock));
        when(managerMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));

        mockMvc.perform(patch("/ui/v1/managers/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(managerId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/managers/%s".formatted(managerId)));

        verify(managerServiceMock, times(1)).updateManagerPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void performPasswordUpdate_shouldPageWithPasswordUpdateForm_whenPasswordValidationRulesAreViolated() throws Exception {
        long managerId = 1L;
        RoleName roleName = RoleName.MANAGER;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "           ";
        Manager managerMock = mock(Manager.class);
        ManagerRepository managerRepositoryMock = context.getBean(ManagerRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(managerRepositoryMock.findById(managerId)).thenReturn(Optional.of(managerMock));
        when(managerMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));

        mockMvc.perform(patch("/ui/v1/managers/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(managerId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));

        verify(managerServiceMock, never()).updateManagerPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGERS_UPDATE"})
    void performPasswordUpdate_shouldReturnPageWithErrorMessage_whenManagerServiceThrowUserNotFoundException() throws Exception {
        long managerId = 1L;
        RoleName roleName = RoleName.MANAGER;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        Manager managerMock = mock(Manager.class);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        UserNotFoundException userNotFoundExceptionMock = mock(UserNotFoundException.class);
        ManagerRepository managerRepositoryMock = context.getBean(ManagerRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(managerRepositoryMock.findById(managerId)).thenReturn(Optional.of(managerMock));
        when(managerMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));
        when(userNotFoundExceptionMock.getMessage()).thenReturn(ERROR_MESSAGE);
        when(userNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(userNotFoundExceptionMock).when(managerServiceMock).updateManagerPassword(any(PasswordUpdateRequestDto.class));

        mockMvc.perform(patch("/ui/v1/managers/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(managerId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("custom-error-page"));

        verify(managerServiceMock, times(1)).updateManagerPassword(any(PasswordUpdateRequestDto.class));
    }

    private List<ManagerDto> getEmptyManagersListForTest() {
        return new ArrayList<>();
    }

    private Page<ManagerDto> getEmptyManagersPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
