package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.ControllerTestConfig;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.AuthorizedUserService;

@WebMvcTest(controllers = {AuthorizedUserController.class})
@ContextConfiguration(classes = {ControllerTestConfig.class})
@Import({SecurityConfig.class})
class AuthorizedUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private AuthorizedUserService authorizedUserService;

    @Mock
    private CustomUserDetails customUserDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void getUserProfilePage_shouldPageWithUserProfile_whenRequestMadeForCorrectUrl() throws Exception {
        UserDto user = mock(UserDto.class);
        when(authorizedUserService.findUserByUserDetails(customUserDetails)).thenReturn(user);

        mockMvc.perform(get("/ui/v1/iam")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"))
            .andExpect(view().name("authorized/profile-page"));

        verify(authorizedUserService, times(1)).findUserByUserDetails(customUserDetails);
    }

    @Test
    @WithMockUser
    void getChangePasswordForm_shouldPageWithFormToUpdatePassword_whenRequestMadeForCorrectUrl() throws Exception {
        long userId = 1;
        when(customUserDetails.getId()).thenReturn(userId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);

        mockMvc.perform(get("/ui/v1/iam/change-pass")
                .with(user(customUserDetails)))
            .andExpect(model().attributeExists("passwordUpdateRequest"))
            .andExpect(status().isOk())
            .andExpect(view().name("authorized/password-update-form"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
    }

    @Test
    @WithMockUser
    void performPasswordUpdate_shouldUpdateUserPasswordAndRedirectToAnotherUrl_whenPasswordValidityRulesNotViolated() throws Exception {
        long studentId = 1L;
        RoleName roleName = RoleName.STUDENT;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        Student studentMock = mock(Student.class);
        StudentRepository studentRepositoryMock = context.getBean(StudentRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(studentMock));
        when(studentMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));

        mockMvc.perform(patch("/ui/v1/iam/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(studentId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/iam"));

        verify(authorizedUserService, times(1)).updateUserPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser
    void performPasswordUpdate_shouldPageWithPasswordUpdateForm_whenPasswordValidationRulesAreViolated() throws Exception {
        long studentId = 1L;
        RoleName roleName = RoleName.STUDENT;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "             ";
        Student studentMock = mock(Student.class);
        StudentRepository studentRepositoryMock = context.getBean(StudentRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(studentMock));
        when(studentMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));

        mockMvc.perform(patch("/ui/v1/iam/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(studentId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(view().name("authorized/password-update-form"));

        verify(authorizedUserService, never()).updateUserPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser
    void getNameUpdateForm_shouldPageWithFormToUpdateUserFullName_whenRequestMadeForCorrectUrl() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        when(customUserDetails.getFirstName()).thenReturn(firstName);
        when(customUserDetails.getLastName()).thenReturn(lastName);

        mockMvc.perform(get("/ui/v1/iam/edit-name")
                .with(user(customUserDetails)))
            .andExpect(model().attributeExists("userFullName"))
            .andExpect(status().isOk())
            .andExpect(view().name("authorized/name-update-form"));

        verify(customUserDetails, times(2)).getFirstName();
        verify(customUserDetails, times(2)).getLastName();
    }

    @Test
    @WithMockUser
    void performUserNameUpdate_shouldUpdateUserNameAndRedirectToAnotherUrl_whenFullNameValidityRulesNotViolated() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";

        mockMvc.perform(patch("/ui/v1/iam/name-update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .with(user(customUserDetails))
                .param("firstName", firstName)
                .param("lastName", lastName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/iam"));

        verify(authorizedUserService, times(1)).updateFullNameByUserDetails(eq(customUserDetails), any(FullName.class));
    }

    @Test
    @WithMockUser
    void performUserNameUpdate_shouldPageWithFullNameUpdateForm_whenFullNameValidationRulesAreViolated() throws Exception {
        String firstName = "FirstName";
        String lastName = "              ";

        mockMvc.perform(patch("/ui/v1/iam/name-update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .with(user(customUserDetails))
                .param("firstName", firstName)
                .param("lastName", lastName))
            .andExpect(status().isOk())
            .andExpect(view().name("authorized/name-update-form"));

        verify(authorizedUserService, never()).updateFullNameByUserDetails(eq(customUserDetails), any(FullName.class));
    }

}
