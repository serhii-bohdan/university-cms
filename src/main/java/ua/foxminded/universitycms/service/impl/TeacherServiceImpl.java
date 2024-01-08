package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;

/**
 * The {@code TeacherServiceImpl} class implements the {@link TeacherService}
 * interface.
 * <p>
 * This class provides the functionality for managing teachers.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code TeacherServiceImpl} with the specified teacher
     * repository, schedule repository, and model mapper.
     *
     * @param teacherRepository  the teacher repository
     * @param scheduleRepository the schedule repository
     * @param modelMapper        the model mapper
     */
    public TeacherServiceImpl(TeacherRepository teacherRepository, ScheduleRepository scheduleRepository,
            ModelMapper modelMapper) {
        this.teacherRepository = teacherRepository;
        this.scheduleRepository = scheduleRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTeacher(TeacherDto teacherDto) {
        boolean isAdded = false;

        if (Objects.nonNull(teacherDto) && Objects.nonNull(teacherDto.getFirstName())
                && Objects.nonNull(teacherDto.getLastName()) && Objects.nonNull(teacherDto.getEmail())
                && Objects.nonNull(teacherDto.getPassword()) && Objects.nonNull(teacherDto.getIsActive())) {
            teacherRepository.save(mapToEntity(teacherDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TeacherDto> getTeacherById(Long teacherId) {
        TeacherDto findedTeacher = null;

        if (Objects.nonNull(teacherId)) {
            Optional<Teacher> optional = teacherRepository.findById(teacherId);

            if (optional.isPresent()) {
                findedTeacher = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedTeacher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteTeacherById(Long teacherId) {
        boolean isDeleted = false;

        if (Objects.nonNull(teacherId)) {
            Optional<Teacher> optional = teacherRepository.findById(teacherId);

            if (optional.isPresent()) {
                teacherRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private TeacherDto mapToDto(Teacher entity) {
        return modelMapper.map(entity, TeacherDto.class);
    }

    private Teacher mapToEntity(TeacherDto dto) {
        Teacher teacher = null;

        if (Objects.nonNull(dto.getUserId()) && dto.getUserId() >= 1L) {
            teacher = teacherRepository.findById(dto.getUserId()).get();
            teacher.setFirstName(dto.getFirstName());
            teacher.setLastName(dto.getLastName());
            teacher.setEmail(dto.getEmail());
            teacher.setPassword(dto.getPassword());
            teacher.setIsActive(dto.getIsActive());
        } else {
            Schedule schedule = new Schedule();
            scheduleRepository.save(schedule);
            teacher = modelMapper.map(dto, Teacher.class);
            teacher.setSchedule(schedule);
        }

        return teacher;
    }

}
