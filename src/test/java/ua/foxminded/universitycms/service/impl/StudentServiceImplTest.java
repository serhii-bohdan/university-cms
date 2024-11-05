package ua.foxminded.universitycms.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.service.StudentService;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {

    @MockBean
    private StudentRepository studentRepositoryMock;

    @MockBean
    private GroupRepository groupRepositoryMock;

    @MockBean
    private ScheduleRepository scheduleRepositoryMock;

    @MockBean
    private RoleRepository roleRepositoryMock;

    @MockBean
    private StudentMapper studentMapperMock;

    @MockBean
    private PasswordEncoder passwordEncoderMock;

    @Autowired
    private StudentService studentService;

    @Test
    void save_shouldSuccessfullySaveNewStudentAndReturnDto_whenValidDtoAndPasswordAreProvided() {
        StudentDto dto = mock(StudentDto.class);
        Student entity = mock(Student.class);
        Role role = mock(Role.class);
        String password = "password";
        String encodedPassword = "encodedPassword";
        when(studentMapperMock.toEntity(dto)).thenReturn(entity);
        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);
        when(roleRepositoryMock.findByRoleName(RoleName.STUDENT)).thenReturn(Optional.of(role));
        when(studentRepositoryMock.save(entity)).thenReturn(entity);
        when(studentMapperMock.toDto(entity)).thenReturn(dto);

        StudentDto savedStudent = studentService.save(dto, password);

        verify(scheduleRepositoryMock, times(1)).save(any(Schedule.class));
        verify(studentMapperMock, times(1)).toEntity(dto);
        verify(entity, times(1)).setSchedule(any(Schedule.class));
        verify(entity, times(1)).setRole(role);
        verify(passwordEncoderMock, times(1)).encode(password);
        verify(entity, times(1)).setPasswordHash(encodedPassword);
        verify(studentRepositoryMock, times(1)).save(entity);
        verify(studentMapperMock, times(1)).toDto(entity);
        assertEquals(dto, savedStudent);
    }

}
