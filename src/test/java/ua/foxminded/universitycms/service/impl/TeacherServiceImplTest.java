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
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Name;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;
import java.util.Optional;

@ContextConfiguration(classes = {ApplicationTestConfiguration.class})
@SpringBootTest(classes = {TeacherServiceImpl.class})
class TeacherServiceImplTest {

    @MockBean
    private TeacherRepository teacherRepositoryMock;

    @MockBean
    private ScheduleRepository scheduleRepositoryMock;

    @MockBean
    private TeacherMapper teacherMapperMock;

    @MockBean
    private PasswordEncoder passwordEncoderMock;

    @Autowired
    private TeacherService teacherService;

    private final String firstName = "FirstName";
    private final String lastName = "LastName";
    private final String email = "email";
    private final String password = "password";
    private final boolean isActive = true;
    private final Long scheduleId = 1L;

    @Test
    void save_shouldInvokeExpectedMethodsAndReturnDto_whenDtoWithValidFieldsIsProvided() {
        TeacherDto dto = TeacherDto.builder()
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(password)
            .isActive(isActive)
            .build();

        Schedule scheduleForTeacher = Schedule.builder()
            .id(scheduleId)
            .build();

        String encodedPassword = "encodedPassword";
        Teacher entity = Teacher.builder()
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(encodedPassword)
            .isActive(isActive)
            .schedule(scheduleForTeacher)
            .build();

        when(scheduleRepositoryMock.save(new Schedule())).thenReturn(scheduleForTeacher);
        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);
        when(teacherMapperMock.toEntity(dto)).thenReturn(entity);
        when(teacherRepositoryMock.save(entity)).thenReturn(entity);
        when(teacherMapperMock.toDto(entity)).thenReturn(dto);

        TeacherDto addedTeacher = teacherService.save(dto);

        verify(scheduleRepositoryMock, times(1)).save(any(Schedule.class));
        verify(passwordEncoderMock, times(1)).encode(password);
        verify(teacherMapperMock, times(1)).toEntity(dto);
        verify(teacherRepositoryMock, times(1)).save(entity);
        verify(teacherMapperMock, times(1)).toDto(entity);
        assertEquals(dto, addedTeacher);
    }

    @Test
    void update_shouldNotInvokedEncodeMethodAndReturnDto_whenPasswordIsNotChanged() {
        Long teacherId = 1L;
        String oldEncodedPassword = "oldEncodedPassword";

        TeacherDto dto = TeacherDto.builder()
            .id(teacherId)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(oldEncodedPassword)
            .isActive(isActive)
            .scheduleId(scheduleId)
            .build();

        Schedule scheduleForTeacher = Schedule.builder()
            .id(scheduleId)
            .build();

        Teacher entity = Teacher.builder()
            .id(teacherId)
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(oldEncodedPassword)
            .isActive(isActive)
            .schedule(scheduleForTeacher)
            .build();

        when(teacherRepositoryMock.findById(teacherId)).thenReturn(Optional.of(entity));
        when(teacherMapperMock.toEntity(dto)).thenReturn(entity);
        when(teacherRepositoryMock.save(entity)).thenReturn(entity);
        when(teacherMapperMock.toDto(entity)).thenReturn(dto);

        TeacherDto updatedTeacher = teacherService.update(dto);

        verify(teacherRepositoryMock, times(1)).findById(teacherId);
        verify(teacherMapperMock, times(1)).toEntity(dto);
        verify(teacherRepositoryMock, times(1)).save(entity);
        verify(teacherMapperMock, times(1)).toDto(entity);
        verify(passwordEncoderMock, never()).encode(oldEncodedPassword);
        assertEquals(dto, updatedTeacher);
    }

    @Test
    void update_shouldInvokedEncodeMethodAndReturnDto_whenPasswordIsChanged() {
        Long teacherId = 1L;
        String newPassword = "newPassword";
        String oldEncodedPassword = "oldEncodedPassword";
        String newEncodedPassword = "newEncodedPassword";

        TeacherDto dto = TeacherDto.builder()
            .id(teacherId)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(newPassword)
            .isActive(isActive)
            .scheduleId(scheduleId)
            .build();

        Schedule scheduleForTeacher = Schedule.builder()
            .id(scheduleId)
            .build();

        Teacher entity = Teacher.builder()
            .id(teacherId)
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(oldEncodedPassword)
            .isActive(isActive)
            .schedule(scheduleForTeacher)
            .build();

        Teacher entityWithNewEncodedPassword = Teacher.builder()
            .id(teacherId)
            .name(new Name(firstName, lastName))
            .email(email)
            .passwordHash(newEncodedPassword)
            .isActive(isActive)
            .schedule(scheduleForTeacher)
            .build();

        when(teacherRepositoryMock.findById(teacherId)).thenReturn(Optional.of(entity));
        when(passwordEncoderMock.encode(newPassword)).thenReturn(newEncodedPassword);
        when(teacherMapperMock.toEntity(dto)).thenReturn(entityWithNewEncodedPassword);
        when(teacherRepositoryMock.save(entityWithNewEncodedPassword)).thenReturn(entityWithNewEncodedPassword);
        when(teacherMapperMock.toDto(entityWithNewEncodedPassword)).thenReturn(dto);

        TeacherDto updatedTeacher = teacherService.update(dto);

        verify(teacherRepositoryMock, times(1)).findById(teacherId);
        verify(passwordEncoderMock, times(1)).encode(newPassword);
        verify(teacherMapperMock, times(1)).toEntity(dto);
        verify(teacherRepositoryMock, times(1)).save(entityWithNewEncodedPassword);
        verify(teacherMapperMock, times(1)).toDto(entityWithNewEncodedPassword);
        assertEquals(dto, updatedTeacher);
    }

}
