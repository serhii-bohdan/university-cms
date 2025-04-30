package ua.foxminded.universitycms.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
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
 * Implementation of the {@link StudentService} interface for managing {@link Student} entities in the university
 * management system.
 * <p>
 * This service class extends {@link AbstractService} to leverage common CRUD operations and provides concrete
 * implementations for student-specific operations such as creation from DTOs, paginated retrieval, unenrolled student
 * retrieval, group mapping, and updates to passwords and full names. It uses {@link StudentRepository},
 * {@link ScheduleRepository}, {@link GroupRepository}, and {@link RoleRepository} for data access, {@link StudentMapper}
 * for entity-DTO conversions, and {@link PasswordEncoder} for secure password handling. The {@code @Service}
 * annotation marks it as a Spring-managed bean, and {@code @Validated} enables validation.
 *
 * @author Serhii Bohdan
 * @see StudentService
 * @see AbstractService
 * @see StudentRepository
 * @see ScheduleRepository
 * @see GroupRepository
 * @see RoleRepository
 * @see StudentMapper
 * @see PasswordEncoder
 * @see EntityNotFoundException
 * @see UserNotFoundException
 */
@Service
@Validated
public class StudentServiceImpl extends AbstractService<Student, StudentDto> implements StudentService {

    /**
     * Error message template used when a student with the specified ID cannot be found.
     * <p>
     * This message is formatted with the student's ID and included in a {@link UserNotFoundException} when
     * retrieval or update operations fail due to a missing student entity.
     */
    private static final String STUDENT_NOT_FOUND_MESSAGE = "Student not found with id: %s.";

    /**
     * Error message template used when a role with the specified name cannot be found.
     * <p>
     * This message is formatted with the role name and included in an {@link EntityNotFoundException} when
     * the student role lookup fails during entity creation or updates.
     */
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role with given role name is not found: %s.";

    /**
     * Repository for performing CRUD operations on {@link Student} entities.
     * <p>
     * This {@link StudentRepository} instance provides data access methods specific to students,
     * extending {@link JpaRepository}.
     */
    private final StudentRepository studentRepository;

    /**
     * Mapper for converting between {@link Student} entities and {@link StudentDto} DTOs.
     * <p>
     * This {@link StudentMapper} instance handles transformations specific to student entities, including conversions
     * from creation DTOs.
     */
    private final StudentMapper studentMapper;

    /**
     * Repository for managing {@link Schedule} entities associated with students.
     * <p>
     * Used to create and assign schedules to students during creation or updates.
     */
    private final ScheduleRepository scheduleRepository;

    /**
     * Repository for accessing and managing {@link Group} entities.
     * <p>
     * Used to retrieve group information for mapping group names to IDs.
     */
    private final GroupRepository groupRepository;

    /**
     * Repository for accessing and querying {@link Role} entities.
     * <p>
     * Used to retrieve the student role during entity creation or updates.
     */
    private final RoleRepository roleRepository;

    /**
     * Encoder for hashing passwords during creation and update operations.
     * <p>
     * This {@link PasswordEncoder} instance ensures secure storage of student passwords by encoding them
     * before persistence.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code StudentServiceImpl} instance with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up specific
     * dependencies for student management, including repositories for groups, schedules, roles, and a password encoder.
     *
     * @param repository         the {@link JpaRepository} for {@link Student} entities, providing basic CRUD operations
     * @param mapper             the {@link Mapper} instance for converting between {@link Student} and
     *                           {@link StudentDto} objects
     * @param groupRepository    the {@link GroupRepository} for managing group entities
     * @param scheduleRepository the {@link ScheduleRepository} for managing schedule entities
     * @param roleRepository     the {@link RoleRepository} for retrieving role information
     * @param passwordEncoder    the {@link PasswordEncoder} for securing passwords
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

    private Role getStudentRole() {
        return roleRepository.findByRoleName(RoleName.STUDENT).orElseThrow(
            () -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ROLE_NOT_FOUND_MESSAGE.formatted(RoleName.STUDENT)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentDto> findStudents(Pageable pageable, String email) {
        return StringUtils.isBlank(email)
            ? studentRepository.findAll(pageable).map(studentMapper::toDto)
            : studentRepository.findByEmail(email, pageable).map(studentMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getUnEnrolledStudents(long courseId, String email) {
        return StringUtils.isBlank(email)
            ? getUnEnrolledStudents(courseId)
            : filterStudentsByEmail(getUnEnrolledStudents(courseId), email);
    }

    private List<StudentDto> getUnEnrolledStudents(long courseId) {
        return studentRepository.findAll().stream()
            .filter(student -> isNotEnrolledInCourse(student.getCourses(), courseId))
            .map(studentMapper::toDto)
            .toList();
    }

    private boolean isNotEnrolledInCourse(Set<Course> studentCourses, long courseId) {
        return studentCourses.stream()
            .map(AbstractEntity::getId)
            .noneMatch(i -> i == courseId);
    }

    private List<StudentDto> filterStudentsByEmail(Collection<StudentDto> students, String email) {
        return students.stream()
            .filter(student -> student.getEmail().equals(email))
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
        long studentId = passwordUpdateRequest.getUserId();
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                STUDENT_NOT_FOUND_MESSAGE.formatted(studentId)));

        student.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        studentRepository.save(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> extractStudentEmails(Collection<StudentDto> students) {
        return students.stream()
            .map(UserDto::getEmail)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateStudentFullName(long studentId, FullName fullName) {
        Student student = studentRepository.findById(studentId).orElseThrow(
            () -> new UserNotFoundException(HttpStatus.NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE.formatted(studentId)));

        student.setFullName(fullName);
        studentRepository.save(student);
    }

}
