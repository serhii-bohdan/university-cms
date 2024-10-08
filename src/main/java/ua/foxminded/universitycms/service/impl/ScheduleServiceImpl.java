package ua.foxminded.universitycms.service.impl;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.service.ScheduleService;

/**
 * The {@code ScheduleServiceImpl} class implements the {@link ScheduleService} interface, providing concrete
 * implementations for managing schedule entities. It extends the {@link AbstractService} class, inheriting common
 * service functionalities for basic CRUD operations and validation, and adds functionality specific to schedules.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see StudentRepository
 */
@Service
@Validated
@Transactional
public class ScheduleServiceImpl extends AbstractService<Schedule, ScheduleDto> implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    /**
     * Constructs a new {@code ScheduleServiceImpl} instance with the given dependencies.
     *
     * @param repository the repository for managing schedule entities
     * @param mapper     the mapper for converting between schedule entities and DTOs
     */
    public ScheduleServiceImpl(JpaRepository<Schedule, Long> repository, Mapper<Schedule, ScheduleDto> mapper) {
        super(repository, mapper);
        this.scheduleRepository = (ScheduleRepository) repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ScheduleDto> getScheduleForStudent(long studentId) {
        return scheduleRepository.findStudentScheduleByStudentId(studentId).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ScheduleDto> getScheduleForTeacher(long teacherId) {
        return scheduleRepository.findTeacherScheduleByTeacherId(teacherId).map(mapper::toDto);
    }

}
