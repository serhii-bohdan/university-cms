package ua.foxminded.universitycms.service.impl;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Student;
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

    /**
     * The repository for managing {@link Student} entities.
     */
    private final StudentRepository studentRepository;

    /**
     * Constructs a new {@code ScheduleServiceImpl} instance with the given dependencies.
     *
     * @param repository        the repository for managing schedule entities
     * @param studentRepository the repository for managing student entities
     * @param mapper            the mapper for converting between schedule entities and DTOs
     */
    public ScheduleServiceImpl(JpaRepository<Schedule, Long> repository, StudentRepository studentRepository,
                               Mapper<Schedule, ScheduleDto> mapper) {
        super(repository, mapper);
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves the schedule for a given student.
     *
     * @param studentId the ID of the student whose schedule to retrieve
     * @return an {@link Optional} containing a {@link ScheduleDto} representing the student's schedule,
     * or an empty {@link Optional} if no schedule is found for the student
     */
    @Override
    public Optional<ScheduleDto> getScheduleForStudent(long studentId) {
        return studentRepository.findById(studentId).map(student -> mapper.toDto(student.getSchedule()));
    }

}
