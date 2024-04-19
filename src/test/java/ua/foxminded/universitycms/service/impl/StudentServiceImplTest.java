package ua.foxminded.universitycms.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import ua.foxminded.universitycms.ApplicationTestConfiguration;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.model.Name;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.service.StudentService;
import java.util.Optional;

@ContextConfiguration(classes = {ApplicationTestConfiguration.class})
@SpringBootTest(classes = { StudentServiceImpl.class })
class StudentServiceImplTest {

    @MockBean
    private StudentRepository studentRepositoryMock;

    @MockBean
    private ScheduleRepository scheduleRepositoryMock;

    @MockBean
    private StudentMapper studentMapperMock;

    @MockBean
    private PasswordEncoder passwordEncoderMock;

    @Autowired
    private StudentService studentService;

    private final String firstName = "FirstName";
    private final String lastName = "LastName";
    private final String email = "email";
    private final String password = "password";
    private final boolean isActive = true;
    private final String groupName = "GroupName";
    private final Long scheduleId = 1L;

    @Test
    void save_shouldInvokeExpectedMethodsAndReturnDto_whenDtoWithValidFieldsIsProvided() {
        StudentDto dto = StudentDto.builder()
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(password)
            .isActive(isActive)
            .groupName(groupName)
            .build();

        Schedule scheduleForStudent = Schedule.builder()
            .id(scheduleId)
            .build();

        Group groupForStudent = new Group(groupName);
        String encodedPassword = "encodedPassword";
        Student entity = Student.builder()
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(encodedPassword)
            .isActive(isActive)
            .group(groupForStudent)
            .schedule(scheduleForStudent)
            .build();

        when(scheduleRepositoryMock.save(new Schedule())).thenReturn(scheduleForStudent);
        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);
        when(studentMapperMock.toEntity(dto)).thenReturn(entity);
        when(studentRepositoryMock.save(entity)).thenReturn(entity);
        when(studentMapperMock.toDto(entity)).thenReturn(dto);

        StudentDto addedStudent = studentService.save(dto);

        verify(scheduleRepositoryMock, times(1)).save(any(Schedule.class));
        verify(passwordEncoderMock, times(1)).encode(password);
        verify(studentMapperMock, times(1)).toEntity(dto);
        verify(studentRepositoryMock, times(1)).save(entity);
        verify(studentMapperMock, times(1)).toDto(entity);
        assertEquals(dto, addedStudent);
    }

    @Test
    void update_shouldNotInvokedEncodeMethodAndReturnDto_whenPasswordIsNotChanged() {
        Long studentId = 1L;
        String oldEncodedPassword = "oldEncodedPassword";

        StudentDto dto = StudentDto.builder()
            .id(studentId)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(oldEncodedPassword)
            .isActive(isActive)
            .groupName(groupName)
            .scheduleId(scheduleId)
            .build();

        Schedule scheduleForStudent = Schedule.builder()
            .id(scheduleId)
            .build();

        Group groupForStudent = new Group(groupName);
        Student entity = Student.builder()
            .id(studentId)
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(oldEncodedPassword)
            .isActive(isActive)
            .group(groupForStudent)
            .schedule(scheduleForStudent)
            .build();

        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(entity));
        when(studentMapperMock.toEntity(dto)).thenReturn(entity);
        when(studentRepositoryMock.save(entity)).thenReturn(entity);
        when(studentMapperMock.toDto(entity)).thenReturn(dto);

        StudentDto updatedStudent = studentService.update(dto);

        verify(studentRepositoryMock, times(1)).findById(studentId);
        verify(studentMapperMock, times(1)).toEntity(dto);
        verify(studentRepositoryMock, times(1)).save(entity);
        verify(studentMapperMock, times(1)).toDto(entity);
        verify(passwordEncoderMock, never()).encode(oldEncodedPassword);
        assertEquals(dto, updatedStudent);
    }

    @Test
    void update_shouldInvokedEncodeMethodAndReturnDto_whenPasswordIsChanged() {
        Long studentId = 1L;
        String newPassword = "newPassword";
        String oldEncodedPassword = "oldEncodedPassword";
        String newEncodedPassword = "newEncodedPassword";
        Group groupForStudent = new Group(groupName);

        StudentDto dto = StudentDto.builder()
            .id(studentId)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(newPassword)
            .isActive(isActive)
            .groupName(groupName)
            .scheduleId(scheduleId)
            .build();

        Schedule scheduleForStudent = Schedule.builder()
            .id(scheduleId)
            .build();

        Student entity = Student.builder()
            .id(studentId)
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(oldEncodedPassword)
            .isActive(isActive)
            .group(groupForStudent)
            .schedule(scheduleForStudent)
            .build();

        Student entityWithNewEncodedPassword = Student.builder()
            .id(studentId)
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(newEncodedPassword)
            .isActive(isActive)
            .group(groupForStudent)
            .schedule(scheduleForStudent)
            .build();

        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(entity));
        when(passwordEncoderMock.encode(newPassword)).thenReturn(newEncodedPassword);
        when(studentMapperMock.toEntity(dto)).thenReturn(entityWithNewEncodedPassword);
        when(studentRepositoryMock.save(entityWithNewEncodedPassword)).thenReturn(entityWithNewEncodedPassword);
        when(studentMapperMock.toDto(entityWithNewEncodedPassword)).thenReturn(dto);

        StudentDto updatedStudent = studentService.update(dto);

        verify(studentRepositoryMock, times(1)).findById(studentId);
        verify(passwordEncoderMock, times(1)).encode(newPassword);
        verify(studentMapperMock, times(1)).toEntity(dto);
        verify(studentRepositoryMock, times(1)).save(entityWithNewEncodedPassword);
        verify(studentMapperMock, times(1)).toDto(entityWithNewEncodedPassword);
        assertEquals(dto, updatedStudent);
    }

}
