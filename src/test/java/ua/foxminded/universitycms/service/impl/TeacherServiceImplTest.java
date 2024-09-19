package ua.foxminded.universitycms.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import ua.foxminded.universitycms.TestConfiguration;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;

@ContextConfiguration(classes = {TestConfiguration.class})
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

    @Test
    void save_shouldSuccessfullySaveNewTeacherAndReturnDto_whenValidDtoAndPasswordAreProvided() {
        TeacherDto dto = mock(TeacherDto.class);
        Teacher entity = mock(Teacher.class);
        String password = "password";
        String encodedPassword = "encodedPassword";
        when(teacherMapperMock.toEntity(dto)).thenReturn(entity);
        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);
        when(teacherRepositoryMock.save(entity)).thenReturn(entity);
        when(teacherMapperMock.toDto(entity)).thenReturn(dto);

        TeacherDto savedTeacher = teacherService.save(dto, password);

        verify(scheduleRepositoryMock, times(1)).save(any(Schedule.class));
        verify(teacherMapperMock, times(1)).toEntity(dto);
        verify(entity, times(1)).setSchedule(any(Schedule.class));
        verify(passwordEncoderMock, times(1)).encode(password);
        verify(entity, times(1)).setPasswordHash(encodedPassword);
        verify(teacherRepositoryMock, times(1)).save(entity);
        verify(teacherMapperMock, times(1)).toDto(entity);
        assertEquals(dto, savedTeacher);
    }

}
