package ua.foxminded.universitycms.service.impl;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;

/**
 * Implementation of {@link TeacherService} for managing {@link Teacher} entities in the university system.
 * <p>
 * Extends {@link AbstractService} for CRUD operations and adds teacher-specific features like creation from DTOs,
 * paginated retrieval, and updates to passwords and full names. Uses {@link TeacherRepository} for data access,
 * {@link TeacherMapper} for conversions, and {@link PasswordEncoder} for security. Marked with {@code @Service}
 * and {@code @Validated}.
 *
 * @author Serhii Bohdan
 * @see TeacherService
 * @see AbstractService
 * @see TeacherRepository
 * @see TeacherMapper
 * @see RoleRepository
 * @see ScheduleRepository
 * @see PasswordEncoder
 * @see EntityNotFoundException
 * @see UserNotFoundException
 */
@Service
@Validated
public class TeacherServiceImpl extends AbstractService<Teacher, TeacherDto> implements TeacherService {

    /**
     * Error message template for when a teacher with the specified ID is not found.
     * <p>
     * Formatted with the teacher's ID and used in a {@link UserNotFoundException} when retrieval fails.
     */
    private static final String TEACHER_NOT_FOUND_MESSAGE = "Teacher not found with id: %s.";

    /**
     * Error message template for when a role with the specified name is not found.
     * <p>
     * Formatted with the role name and used in an {@link EntityNotFoundException} when role lookup fails.
     */
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role with given role name is not found: %s.";

    /**
     * Repository for performing CRUD operations on {@link Teacher} entities.
     * <p>
     * This {@link TeacherRepository} provides data access specific to teachers, extending {@link JpaRepository}.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Mapper for converting between {@link Teacher} entities and {@link TeacherDto} DTOs.
     * <p>
     * This {@link TeacherMapper} handles transformations specific to teachers, including creation DTOs.
     */
    private final TeacherMapper teacherMapper;

    /**
     * Repository for accessing and querying {@link Role} entities.
     * <p>
     * Used to retrieve the teacher role during entity creation or updates.
     */
    private final RoleRepository roleRepository;

    /**
     * Repository for managing {@link Schedule} entities associated with teachers.
     * <p>
     * Used to create and assign schedules to teachers during creation or updates.
     */
    private final ScheduleRepository scheduleRepository;

    /**
     * Encoder for hashing passwords during creation and update operations.
     * <p>
     * This {@link PasswordEncoder} ensures secure storage of teacher passwords by encoding them.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code TeacherServiceImpl} with the required dependencies.
     * <p>
     * Initializes {@link AbstractService} with repository and mapper, and sets up specific dependencies for
     * teacher management, including role, schedule, and password encoding.
     *
     * @param repository         the {@link JpaRepository} for {@link Teacher} entities
     * @param mapper             the {@link Mapper} for {@link Teacher} and {@link TeacherDto} conversions
     * @param roleRepository     the {@link RoleRepository} for role data
     * @param scheduleRepository the {@link ScheduleRepository} for schedule data
     * @param passwordEncoder    the {@link PasswordEncoder} for password security
     */
    public TeacherServiceImpl(JpaRepository<Teacher, Long> repository, Mapper<Teacher, TeacherDto> mapper,
                              RoleRepository roleRepository, ScheduleRepository scheduleRepository,
                              PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.teacherRepository = (TeacherRepository) repository;
        this.teacherMapper = (TeacherMapper) mapper;
        this.roleRepository = roleRepository;
        this.scheduleRepository = scheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TeacherDto save(TeacherCreationDto dto) {
        Schedule teacherSchedule = new Schedule();
        scheduleRepository.save(teacherSchedule);

        Teacher teacher = teacherMapper.toEntity(dto);
        teacher.setSchedule(teacherSchedule);
        teacher.setRole(getTeacherRole());

        return teacherMapper.toDto(teacherRepository.save(teacher));
    }

    private Role getTeacherRole() {
        return roleRepository.findByRoleName(RoleName.TEACHER).orElseThrow(
            () -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ROLE_NOT_FOUND_MESSAGE.formatted(RoleName.TEACHER)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeacherDto> findTeachers(Pageable pageable, String email) {
        return StringUtils.isBlank(email)
            ? teacherRepository.findAll(pageable).map(mapper::toDto)
            : teacherRepository.findByEmail(email, pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateTeacherPassword(PasswordUpdateRequestDto passwordUpdateRequest) {
        long teacherId = passwordUpdateRequest.getUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                TEACHER_NOT_FOUND_MESSAGE.formatted(teacherId)));

        teacher.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        teacherRepository.save(teacher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> extractTeacherEmails(Collection<TeacherDto> teachers) {
        return teachers.stream()
            .map(UserDto::getEmail)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateTeacherFullName(long teacherId, FullName fullName) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
            () -> new UserNotFoundException(HttpStatus.NOT_FOUND, TEACHER_NOT_FOUND_MESSAGE.formatted(teacherId)));

        teacher.setFullName(fullName);
        teacherRepository.save(teacher);
    }

}
