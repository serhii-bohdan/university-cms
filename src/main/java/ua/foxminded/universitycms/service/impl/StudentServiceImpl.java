package ua.foxminded.universitycms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.model.*;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.service.StudentService;

/**
 * Implementation of the {@link StudentService} interface for managing student-related operations.
 * This service class extends the {@link AbstractService} to handle core CRUD operations and provides additional
 * functionality specific to student management, such as associating students with groups and schedules.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see StudentRepository
 * @see ScheduleRepository
 * @see GroupRepository
 * @see RoleRepository
 * @see PasswordEncoder
 */
@Service
@Validated
public class StudentServiceImpl extends AbstractService<Student, StudentDto> implements StudentService {

    /**
     * Error message used when a student is not found by the specified ID.
     * This message includes a placeholder for the student's ID.
     */
    private static final String STUDENT_NOT_FOUND_MESSAGE = "Student not found with id: %s";

    /**
     * Error message used when a role is not found by the specified role name.
     * This message includes the role name that was searched for.
     */
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role with given role name is not found: ";

    /**
     * The {@link StudentRepository} used for managing student entities.
     */
    private final StudentRepository studentRepository;

    /**
     * Mapper for converting between {@link Student} entities and {@link StudentDto} objects.
     */
    private final StudentMapper studentMapper;

    /**
     * The {@link ScheduleRepository} used for managing user schedules.
     */
    private final ScheduleRepository scheduleRepository;

    /**
     * The {@link GroupRepository} used for accessing and managing group data in the database.
     */
    private final GroupRepository groupRepository;

    /**
     * The {@link RoleRepository} used for accessing and managing role data in the database.
     */
    private final RoleRepository roleRepository;

    /**
     * The {@link PasswordEncoder} used for securely encoding user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code StudentServiceImpl} instance with the specified dependencies.
     *
     * @param repository         the repository for managing student entities
     * @param mapper             the mapper for converting between student entities and DTOs
     * @param groupRepository    the repository for managing group entities
     * @param scheduleRepository the repository for managing schedule entities
     * @param roleRepository     the repository for managing role entities
     * @param passwordEncoder    the password encoder for securely encoding student passwords
     */
    public StudentServiceImpl(JpaRepository<Student, Long> repository, Mapper<Student, StudentDto> mapper, GroupRepository groupRepository,
                              ScheduleRepository scheduleRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.studentRepository = (StudentRepository) repository;
        this.studentMapper = (StudentMapper) mapper;
        this.groupRepository = groupRepository;
        this.scheduleRepository = scheduleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public StudentDto save(StudentCreationDto dto) {
        Schedule studentSchedule = new Schedule();
        scheduleRepository.save(studentSchedule);

        Student student = studentMapper.toEntity(dto);
        student.setRole(getStudentRole());
        student.setSchedule(studentSchedule);

        return studentMapper.toDto(studentRepository.save(student));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentDto> getStudentsPage(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentDto> getStudentInPageByEmail(String email, Pageable pageable) {
        return studentRepository.findByEmail(email, pageable).map(studentMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getListOfStudentsNotEnrolledInCourse(long courseId) {
        return studentRepository.findAll().stream()
            .filter(s -> s.getCourses().stream()
                .map(AbstractEntity::getId)
                .noneMatch(i -> i == courseId))
            .map(studentMapper::toDto)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getAllExistingGroups() {
        return groupRepository.findAll().stream()
            .collect(Collectors.toMap(Group::getGroupName, Group::getId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateStudentPassword(PasswordUpdateRequestDto passwordUpdateRequest) {
        Student student = studentRepository.findById(passwordUpdateRequest.getUserId())
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format(STUDENT_NOT_FOUND_MESSAGE, passwordUpdateRequest.getUserId())));

        student.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        studentRepository.save(student);
    }

    private Role getStudentRole() {
        return roleRepository.findByRoleName(RoleName.STUDENT).orElseThrow(
            () -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ROLE_NOT_FOUND_MESSAGE + RoleName.STUDENT));
    }

}
