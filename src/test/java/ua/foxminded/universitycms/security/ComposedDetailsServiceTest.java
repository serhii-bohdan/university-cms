package ua.foxminded.universitycms.security;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.AdminRepository;
import ua.foxminded.universitycms.repository.ManagerRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import java.util.Optional;

@SpringBootTest(classes = {ComposedDetailsService.class})
class ComposedDetailsServiceTest {

    private static final String TEST_EMAIL = "test@gmail.com";

    @MockBean
    private AdminRepository adminRepositoryMock;

    @MockBean
    private ManagerRepository managerRepositoryMock;

    @MockBean
    private TeacherRepository teacherRepositoryMock;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @Autowired
    private ComposedDetailsService composedDetailsService;

    @Test
    void loadUserByUsername_shouldFindStudent_whenEmailExistsInStudentRepository() {
        Student studentMock = mock(Student.class);
        when(studentMock.getEmail()).thenReturn(TEST_EMAIL);
        when(adminRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(managerRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(teacherRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(studentRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(studentMock));

        CustomUserDetails customUserDetails = (CustomUserDetails) composedDetailsService.loadUserByUsername(TEST_EMAIL);

        verify(studentRepositoryMock, times(1)).findByEmail(TEST_EMAIL);
        assertEquals(TEST_EMAIL, customUserDetails.getUsername());
    }

    @Test
    void loadUserByUsername_shouldFindTeacher_whenEmailExistsInTeacherRepository() {
        Teacher teacherMock = mock(Teacher.class);
        when(teacherMock.getEmail()).thenReturn(TEST_EMAIL);
        when(adminRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(managerRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(teacherRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(teacherMock));
        when(studentRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        CustomUserDetails customUserDetails = (CustomUserDetails) composedDetailsService.loadUserByUsername(TEST_EMAIL);

        verify(teacherRepositoryMock, times(1)).findByEmail(TEST_EMAIL);
        assertEquals(TEST_EMAIL, customUserDetails.getUsername());
    }

    @Test
    void loadUserByUsername_shouldFindManager_whenEmailExistsInManagerRepository() {
        Manager managerMock = mock(Manager.class);
        when(managerMock.getEmail()).thenReturn(TEST_EMAIL);
        when(adminRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(managerRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(managerMock));
        when(teacherRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(studentRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        CustomUserDetails customUserDetails = (CustomUserDetails) composedDetailsService.loadUserByUsername(TEST_EMAIL);

        verify(managerRepositoryMock, times(1)).findByEmail(TEST_EMAIL);
        assertEquals(TEST_EMAIL, customUserDetails.getUsername());
    }

    @Test
    void loadUserByUsername_shouldFindAdmin_whenEmailExistsInAdminRepository() {
        Admin adminMock = mock(Admin.class);
        when(adminMock.getEmail()).thenReturn(TEST_EMAIL);
        when(adminRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(adminMock));
        when(managerRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(teacherRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(studentRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        CustomUserDetails customUserDetails = (CustomUserDetails) composedDetailsService.loadUserByUsername(TEST_EMAIL);

        verify(adminRepositoryMock, times(1)).findByEmail(TEST_EMAIL);
        assertEquals(TEST_EMAIL, customUserDetails.getUsername());
    }

    @Test
    void loadUserByUsername_shouldUsernameNotFoundException_whenUserIsNotFoundByHisEmail() {
        when(adminRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(managerRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(teacherRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(studentRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> composedDetailsService.loadUserByUsername(TEST_EMAIL));
    }

}
