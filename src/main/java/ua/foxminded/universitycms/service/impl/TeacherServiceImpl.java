package ua.foxminded.universitycms.service.impl;

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
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;

/**
 * The {@code TeacherServiceImpl} class provides the concrete implementation for managing {@link Teacher} entities
 * within the application. It extends the {@link AbstractService} class, inheriting common entity management
 * capabilities, and adds specialized operations tailored for teachers.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see TeacherRepository
 * @see ScheduleRepository
 * @see PasswordEncoder
 */
@Service
@Validated
public class TeacherServiceImpl extends AbstractService<Teacher, TeacherDto> implements TeacherService {

    /**
     * Message template used when a teacher with the specified ID is not found.
     * This message includes the ID of the missing teacher when formatted.
     */
    private static final String TEACHER_NOT_FOUND_MESSAGE = "Teacher not found with id: %s";

    /**
     * Error message used when a role is not found by the specified role name.
     * This message includes the role name that was searched for.
     */
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role with given role name is not found: ";

    /**
     * The {@link TeacherRepository} used for managing teacher entities.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Mapper for converting between {@link Teacher} entities and {@link TeacherDto} objects.
     */
    private final TeacherMapper teacherMapper;

    /**
     * The {@link RoleRepository} used for managing teacher role.
     */
    private final RoleRepository roleRepository;

    /**
     * The {@link ScheduleRepository} used for managing user schedules.
     */
    private final ScheduleRepository scheduleRepository;

    /**
     * The {@link PasswordEncoder} used for securely encoding user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code TeacherServiceImpl} instance with the given dependencies.
     *
     * @param repository         the repository for managing teacher entities
     * @param mapper             the mapper for converting between teacher entities and DTOs
     * @param scheduleRepository the repository for managing schedules (potentially associated with teachers)
     * @param passwordEncoder    the password encoder for securely encoding teacher passwords
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeacherDto> getTeachersPage(Pageable pageable) {
        return teacherRepository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeacherDto> getTeacherInPageByEmail(String email, Pageable pageable) {
        return teacherRepository.findByEmail(email, pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if the teacher with the specified ID is not found.
     */
    @Override
    @Transactional
    public void updateTeacherPassword(PasswordUpdateRequestDto passwordUpdateRequest) {
        Teacher teacher = teacherRepository.findById(passwordUpdateRequest.getUserId())
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format(TEACHER_NOT_FOUND_MESSAGE, passwordUpdateRequest.getUserId())));

        teacher.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        teacherRepository.save(teacher);
    }

    private Role getTeacherRole() {
        return roleRepository.findByRoleName(RoleName.TEACHER).orElseThrow(
            () -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ROLE_NOT_FOUND_MESSAGE + RoleName.TEACHER));
    }

}
