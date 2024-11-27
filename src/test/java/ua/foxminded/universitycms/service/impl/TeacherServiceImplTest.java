package ua.foxminded.universitycms.service.impl;

import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;

@SpringBootTest(classes = {TeacherServiceImpl.class})
class TeacherServiceImplTest {

    @MockBean
    private TeacherRepository teacherRepositoryMock;

    @MockBean
    private ScheduleRepository scheduleRepositoryMock;

    @MockBean
    private RoleRepository roleRepositoryMock;

    @MockBean
    private TeacherMapper teacherMapperMock;

    @MockBean
    private PasswordEncoder passwordEncoderMock;

    @Autowired
    private TeacherService teacherService;

    @Test
    void save_shouldSuccessfullySaveNewTeacherAndReturnDto_whenValidDtoAndPasswordAreProvided() {
        TeacherCreationDto dtoBeforeSaving = mock(TeacherCreationDto.class);
        TeacherDto dtoAfterSaving = mock(TeacherDto.class);
        Teacher entity = mock(Teacher.class);
        Role role = mock(Role.class);
        when(roleRepositoryMock.findByRoleName(RoleName.TEACHER)).thenReturn(Optional.of(role));
        when(teacherMapperMock.toEntity(dtoBeforeSaving)).thenReturn(entity);
        when(teacherRepositoryMock.save(entity)).thenReturn(entity);
        when(teacherMapperMock.toDto(entity)).thenReturn(dtoAfterSaving);

        teacherService.save(dtoBeforeSaving);

        verify(scheduleRepositoryMock, times(1)).save(any(Schedule.class));
        verify(teacherMapperMock, times(1)).toEntity(dtoBeforeSaving);
        verify(entity, times(1)).setSchedule(any(Schedule.class));
        verify(entity, times(1)).setRole(role);
        verify(teacherRepositoryMock, times(1)).save(entity);
        verify(teacherMapperMock, times(1)).toDto(entity);
    }

}
