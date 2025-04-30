package ua.foxminded.universitycms.service.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.InvalidUserRoleException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.ScheduleService;

/**
 * Implementation of the {@link ScheduleService} interface for managing {@link Schedule} entities in the university
 * management system.
 * <p>
 * This service class extends {@link AbstractService} to leverage common CRUD operations and provides a concrete
 * implementation for retrieving user-specific schedules based on their role. It uses {@link ScheduleRepository}
 * for data access and a {@link Mapper} for entity-DTO conversions. The {@code @Service} annotation marks it as a
 * Spring-managed bean, and {@code @Validated} enables validation.
 *
 * @author Serhii Bohdan
 * @see ScheduleService
 * @see AbstractService
 * @see ScheduleRepository
 * @see Mapper
 * @see EntityNotFoundException
 * @see InvalidUserRoleException
 */
@Service
@Validated
public class ScheduleServiceImpl extends AbstractService<Schedule, ScheduleDto> implements ScheduleService {

    /**
     * Error message template used when a schedule with the specified ID cannot be found.
     * This message is formatted with the user's ID and included in an {@link EntityNotFoundException} when
     * schedule retrieval fails.
     */
    private static final String SCHEDULE_NOT_FOUND_MESSAGE = "Schedule entity not found with ID: %s.";

    /**
     * Error message used when a user lacks permission to access schedule data due to an invalid role.
     * This message is included in an {@link InvalidUserRoleException} when an unauthorized role attempts
     * to retrieve a schedule.
     */
    private static final String INVALID_ROLE_MESSAGE = "You do not have permission to read schedule data.";

    /**
     * Repository for performing CRUD operations on {@link Schedule} entities.
     * <p>
     * This {@link ScheduleRepository} instance provides data access methods specific to schedules,
     * extending {@link JpaRepository}.
     */
    private final ScheduleRepository scheduleRepository;

    /**
     * Constructs a new {@code ScheduleServiceImpl} instance with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up the specific
     * {@link ScheduleRepository} for schedule management.
     *
     * @param repository the {@link JpaRepository} for {@link Schedule} entities, providing basic CRUD operations
     * @param mapper     the {@link Mapper} instance for converting between {@link Schedule} and {@link ScheduleDto} objects
     */
    public ScheduleServiceImpl(JpaRepository<Schedule, Long> repository, Mapper<Schedule, ScheduleDto> mapper) {
        super(repository, mapper);
        this.scheduleRepository = (ScheduleRepository) repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ScheduleDto getScheduleForUser(CustomUserDetails customUserDetails) {
        long userId = customUserDetails.getId();
        RoleName userRole = customUserDetails.getRoleName();

        return switch (userRole) {
            case TEACHER -> getScheduleForTeacher(userId);
            case STUDENT -> getScheduleForStudent(userId);
            default -> throw new InvalidUserRoleException(HttpStatus.FORBIDDEN, INVALID_ROLE_MESSAGE);
        };
    }

    private ScheduleDto getScheduleForTeacher(long teacherId) {
        return scheduleRepository.findTeacherScheduleByTeacherId(teacherId).map(mapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                SCHEDULE_NOT_FOUND_MESSAGE.formatted(teacherId)));
    }

    private ScheduleDto getScheduleForStudent(long studentId) {
        return scheduleRepository.findStudentScheduleByStudentId(studentId).map(mapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                SCHEDULE_NOT_FOUND_MESSAGE.formatted(studentId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getUserLocalDate(CustomUserDetails customUserDetails) {
        return LocalDate.now(ZoneOffset.of(customUserDetails.getLocationZoneOffset()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getUserLocalDate(String userLocationZoneOffset) {
        return LocalDate.now(ZoneOffset.of(userLocationZoneOffset));
    }

}
