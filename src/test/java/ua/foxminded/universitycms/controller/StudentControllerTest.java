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
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.service.StudentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebMvcTest(controllers = StudentController.class)
@ContextConfiguration(classes = ControllerTestConfig.class)
@Import(SecurityConfig.class)
class StudentControllerTest {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private StudentService studentServiceMock;

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithStudents_shouldReturnPageWithStudentsThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "test@gmail.com";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAll()).thenReturn(getAllStudentsForTest());
        when(studentServiceMock.getStudentInPageByEmail(keyword, pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getAll();
        verify(studentServiceMock, times(1)).getStudentInPageByEmail(keyword, pageable);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithStudents_shouldReturnPageWithAllStudents_whenKeywordIsNull() throws Exception {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAll()).thenReturn(getAllStudentsForTest());
        when(studentServiceMock.getStudentsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getAll();
        verify(studentServiceMock, times(1)).getStudentsPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithStudents_shouldReturnPageWithAllStudents_whenKeywordIsBlank() throws Exception {
        String keyword = "               ";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAll()).thenReturn(getAllStudentsForTest());
        when(studentServiceMock.getStudentsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", ""))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getAll();
        verify(studentServiceMock, times(1)).getStudentsPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithStudents_shouldReturnPageWithAllStudents_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAll()).thenReturn(getAllStudentsForTest());
        when(studentServiceMock.getStudentsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getAll();
        verify(studentServiceMock, times(1)).getStudentsPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithListOfStudentsNotEnrolledInCourse_shouldPageWithListOfStudentsWhoNotEnrolledInCourseAndFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        long courseId = 1L;
        String keyword = "test@email.com";
        when(studentServiceMock.getListOfStudentsNotEnrolledInCourse(courseId)).thenReturn(getAllStudentsForTest());

        mockMvc.perform(get("/ui/v1/students/not-enrolled")
                .param("cid", String.valueOf(courseId))
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("notEnrolledStudents"))
            .andExpect(model().attributeExists("studentEmails"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("students/not-enrolled-in-course"));

        verify(studentServiceMock, times(1)).getListOfStudentsNotEnrolledInCourse(courseId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithListOfStudentsNotEnrolledInCourse_shouldPageWithListOfAllStudentsWhoNotEnrolledInCourse_whenKeywordIsBlank() throws Exception {
        long courseId = 1L;
        String keyword = "              ";
        when(studentServiceMock.getListOfStudentsNotEnrolledInCourse(courseId)).thenReturn(getAllStudentsForTest());

        mockMvc.perform(get("/ui/v1/students/not-enrolled")
                .param("cid", String.valueOf(courseId))
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("notEnrolledStudents"))
            .andExpect(model().attributeExists("studentEmails"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attribute("keyword", ""))
            .andExpect(view().name("students/not-enrolled-in-course"));

        verify(studentServiceMock, times(1)).getListOfStudentsNotEnrolledInCourse(courseId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithListOfStudentsNotEnrolledInCourse_shouldPageWithListOfAllStudentsWhoNotEnrolledInCourse_whenKeywordIsNull() throws Exception {
        long courseId = 1L;
        when(studentServiceMock.getListOfStudentsNotEnrolledInCourse(courseId)).thenReturn(getAllStudentsForTest());

        mockMvc.perform(get("/ui/v1/students/not-enrolled")
                .param("cid", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("notEnrolledStudents"))
            .andExpect(model().attributeExists("studentEmails"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(view().name("students/not-enrolled-in-course"));

        verify(studentServiceMock, times(1)).getListOfStudentsNotEnrolledInCourse(courseId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithParticularStudent_shouldPageWithParticularStudent_whenStudentWithGivenIdExists() throws Exception {
        long studentId = 1L;
        StudentDto studentMock = mock(StudentDto.class);
        when(studentServiceMock.getById(studentId)).thenReturn(Optional.of(studentMock));

        mockMvc.perform(get("/ui/v1/students/{studentId}", studentId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("student"))
            .andExpect(view().name("students/particular-student"));

        verify(studentServiceMock, times(1)).getById(studentId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_READ"})
    void getPageWithParticularStudent_shouldPageWithErrorMessage_whenStudentWithGivenIdDoesNotExists() throws Exception {
        long studentId = 1L;
        when(studentServiceMock.getById(studentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/students/{studentId}", studentId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(studentServiceMock, times(1)).getById(studentId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_CREATE"})
    void getCreationForm_shouldPageWithFormToAddNewStudent_whenRequestMadeForCorrectUrl() throws Exception {
        when(studentServiceMock.getAllExistingGroups()).thenReturn(getAllGroupsForTest());

        mockMvc.perform(get("/ui/v1/students/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("student"))
            .andExpect(model().attributeExists("allExistingGroups"))
            .andExpect(view().name("students/creation-form"));

        verify(studentServiceMock, times(1)).getAllExistingGroups();
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_CREATE"})
    void performStudentAdding_shouldAddNewStudentAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        String password = "password";
        Boolean isActive = true;
        long groupId = 1L;

        mockMvc.perform(post("/ui/v1/students/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password)
                .param("isActive", String.valueOf(isActive))
                .param("groupId", String.valueOf(groupId)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/students"));

        verify(studentServiceMock, times(1)).save(any(StudentCreationDto.class));
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_CREATE"})
    void performStudentAdding_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String password = "password";
        Boolean isActive = true;
        long groupId = 1L;
        when(studentServiceMock.getAllExistingGroups()).thenReturn(getAllGroupsForTest());

        mockMvc.perform(post("/ui/v1/students/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("password", password)
                .param("isActive", String.valueOf(isActive))
                .param("groupId", String.valueOf(groupId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("allExistingGroups"))
            .andExpect(view().name("students/creation-form"));

        verify(studentServiceMock, never()).save(any(StudentCreationDto.class));
        verify(studentServiceMock, times(1)).getAllExistingGroups();
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void getUpdateForm_shouldPageWithFormToUpdateExistentStudent_whenStudentWithGivenIdExists() throws Exception {
        long studentId = 1L;
        StudentDto studentMock = mock(StudentDto.class);
        when(studentServiceMock.getById(studentId)).thenReturn(Optional.of(studentMock));

        mockMvc.perform(get("/ui/v1/students/{studentId}/edit", studentId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("student"))
            .andExpect(model().attributeExists("allExistingGroups"))
            .andExpect(view().name("students/update-form"));

        verify(studentServiceMock, times(1)).getById(studentId);
        verify(studentServiceMock, times(1)).getAllExistingGroups();
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void getUpdateForm_shouldPageWithErrorMessage_whenStudentWithGivenIdDoesNotExists() throws Exception {
        long studentId = 1L;
        when(studentServiceMock.getById(studentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/students/{studentId}/edit", studentId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(studentServiceMock, times(1)).getById(studentId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void performStudentUpdate_shouldUpdateStudentAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        long studentId = 1L;
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        Boolean isActive = true;
        long groupId = 1L;

        mockMvc.perform(put("/ui/v1/students/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("id", String.valueOf(studentId))
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("isActive", String.valueOf(isActive))
                .param("groupId", String.valueOf(groupId)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/students/%s", studentId)));

        verify(studentServiceMock, times(1)).update(any(StudentDto.class));
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void performStudentUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";

        mockMvc.perform(put("/ui/v1/students/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("allExistingGroups"))
            .andExpect(view().name("students/update-form"));

        verify(studentServiceMock, times(1)).getAllExistingGroups();
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void performStudentUpdate_shouldPageWithErrorMessage_whenStudentServiceThrowEntityNotFoundException() throws Exception {
        String firstName = "FirstName";
        String lastName = "LastName";
        String email = "test@email.com";
        Boolean isActive = true;
        long groupId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(studentServiceMock.update(any(StudentDto.class))).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(put("/ui/v1/students/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("isActive", String.valueOf(isActive))
                .param("groupId", String.valueOf(groupId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(studentServiceMock, times(1)).update(any(StudentDto.class));
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_DELETE"})
    void performStudentDeletion_shouldDeleteStudentAndRedirectToAnotherUrl_whenStudentWithGivenIdExist() throws Exception {
        long studentId = 1L;

        mockMvc.perform(delete("/ui/v1/students/{studentId}/delete", studentId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/students"));

        verify(studentServiceMock, times(1)).deleteById(studentId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_DELETE"})
    void performStudentDeletion_shouldPageWithErrorMessage_whenStudentServiceThrowEntityNotFoundException() throws Exception {
        long studentId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(studentServiceMock).deleteById(studentId);

        mockMvc.perform(delete("/ui/v1/students/{studentId}/delete", studentId)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(studentServiceMock, times(1)).deleteById(studentId);
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void getChangePasswordForm_shouldPageWithFormToUpdatePassword_whenRequestMadeForCorrectUrl() throws Exception {
        long studentId = 1L;

        mockMvc.perform(get("/ui/v1/students/{studentId}/change-pass", studentId))
            .andExpect(model().attributeExists("passwordUpdateRequest"))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void performPasswordUpdate_shouldUpdateStudentPasswordAndRedirectToAnotherUrl_whenPasswordValidityRulesNotViolated() throws Exception {
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

        mockMvc.perform(patch("/ui/v1/students/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(studentId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/students/%s", studentId)));

        verify(studentServiceMock, times(1)).updateStudentPassword(any(PasswordUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void performPasswordUpdate_shouldPageWithPasswordUpdateForm_whenPasswordValidationRulesAreViolated() throws Exception {
        long studentId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "           ";

        mockMvc.perform(patch("/ui/v1/students/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(studentId))
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(view().name("security/password-update-form"));
    }

    @Test
    @WithMockUser(authorities = {"STUDENTS_UPDATE"})
    void performPasswordUpdate_shouldPageWithErrorMessage_whenStudentServiceThrowEntityNotFoundException() throws Exception {
        long studentId = 1L;
        RoleName roleName = RoleName.STUDENT;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        Student studentMock = mock(Student.class);
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        StudentRepository studentRepositoryMock = context.getBean(StudentRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(studentMock));
        when(studentMock.getPasswordHash()).thenReturn(passwordEncoder.encode(currentPassword));
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(studentServiceMock).updateStudentPassword(any(PasswordUpdateRequestDto.class));

        mockMvc.perform(patch("/ui/v1/students/update-pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("userId", String.valueOf(studentId))
                .param("roleName", roleName.name())
                .param("currentPassword", currentPassword)
                .param("newPassword", newPassword)
                .param("confirmNewPassword", confirmNewPassword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(studentServiceMock, times(1)).updateStudentPassword(any(PasswordUpdateRequestDto.class));
    }

    private Map<String, Long> getAllGroupsForTest() {
        String groupName = "HK-89";
        Long groupId = 1L;
        return Map.of(groupName, groupId);
    }

    private List<StudentDto> getAllStudentsForTest() {
        return new ArrayList<>();
    }

    private Page<StudentDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
