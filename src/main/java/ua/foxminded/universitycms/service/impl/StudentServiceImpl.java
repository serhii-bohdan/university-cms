package ua.foxminded.universitycms.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.model.AbstractEntity;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Student;
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
 * @see PasswordEncoder
 */
@Service
@Validated
@Transactional
public class StudentServiceImpl extends AbstractService<Student, StudentDto> implements StudentService {

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
    protected final ScheduleRepository scheduleRepository;

    /**
     * The {@link PasswordEncoder} used for securely encoding user passwords.
     */
    protected final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code StudentServiceImpl} instance with the given dependencies.
     *
     * @param repository         the repository for managing student entities
     * @param mapper             the mapper for converting between student entities and DTOs
     * @param scheduleRepository the repository for managing schedules
     * @param passwordEncoder    the password encoder for securely encoding student passwords
     */
    public StudentServiceImpl(JpaRepository<Student, Long> repository, Mapper<Student, StudentDto> mapper,
                              ScheduleRepository scheduleRepository, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.studentRepository = (StudentRepository) repository;
        this.studentMapper = (StudentMapper) mapper;
        this.scheduleRepository = scheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentDto save(StudentDto dto, String password) {
        Schedule studentSchedule = new Schedule();
        scheduleRepository.save(studentSchedule);

        Student student = studentMapper.toEntity(dto);
        student.setSchedule(studentSchedule);
        student.setPasswordHash(passwordEncoder.encode(password));

        return studentMapper.toDto(studentRepository.save(student));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentDto update(StudentDto dto) {
        Student existingStudent = studentRepository.findById(dto.getId())
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                String.format("Student not found with id: %d", dto.getId())));

        Student updatedStudent = studentMapper.partialUpdate(dto, existingStudent);
        return studentMapper.toDto(studentRepository.save(updatedStudent));
    }

    /**
     * Retrieves a page of student data containing all students (if pageable is not null).
     * This method retrieves a paginated list of all students from the underlying data store.
     * It utilizes the provided `Pageable` object to specify the page number, size.
     *
     * @param pageable the Pageable object containing pagination information (size, page number)
     *                 (must not be null)
     * @return a Page object containing a list of StudentDto objects representing the requested page of students
     */
    @Override
    public Page<StudentDto> getStudentsPage(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toDto);
    }

    /**
     * Retrieves a page of student data filtered by full name (if fullName and pageable are not null).
     * This method retrieves a paginated list of students whose full names contain the provided keyword.
     * It utilizes the `Pageable` object to specify the page number, size.
     *
     * @param fullName the keyword to filter students by full name (can be blank) (must not be null)
     * @param pageable the Pageable object containing pagination information (size, page number) (must not be null)
     * @return a Page object containing a list of StudentDto objects representing the requested page of filtered students
     */
    @Override
    public Page<StudentDto> getStudentInPageByName(String fullName, Pageable pageable) {
        List<String> names = getSeparateFirstNameAndLastName(fullName.strip());
        return studentRepository.findByName_FirstNameAndName_LastNameIgnoreCase(names.get(0), names.get(1), pageable).map(studentMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllNamesOfStudents() {
        return studentRepository.findAll().stream()
            .map(s -> s.getName().getFirstName() + " " + s.getName().getLastName())
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StudentDto> getListOfStudentsNotEnrolledInCourse(long courseId) {
        return studentRepository.findAll().stream()
            .filter(s -> s.getCourses().stream()
                .map(AbstractEntity::getId)
                .noneMatch(i -> i == courseId))
            .map(studentMapper::toDto)
            .toList();
    }

    private List<String> getSeparateFirstNameAndLastName(String fullName) {
        String[] firstNameAndLastName = fullName.split(" ");

        if (firstNameAndLastName.length >= 2) {
            return Arrays.asList(firstNameAndLastName);
        }

        throw new InvalidFullNameFormatException(HttpStatus.BAD_REQUEST, "Invalid full name");
    }

}
