package ua.foxminded.universitycms.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.service.StudentService;

/**
 * The {@code StudentServiceImpl} class implements the {@link StudentService} interface, providing concrete
 * implementations for managing student entities. It extends the {@link UserService} class, inheriting core user
 * management functionality and adding student-specific services like group assignment and name-based retrieval.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see StudentRepository
 * @see PasswordEncoder
 */
@Service
@Validated
@Transactional
public class StudentServiceImpl extends UserService<Student, StudentDto> implements StudentService {

    /**
     * The {@link StudentRepository} used for managing student entities.
     */
    private final StudentRepository studentRepository;

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
        super(repository, mapper, scheduleRepository, passwordEncoder);
        this.studentRepository = (StudentRepository) repository;
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
        return studentRepository.findAll(pageable).map(mapper::toDto);
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
        return studentRepository.findByName_FirstNameAndName_LastNameIgnoreCase(names.get(0), names.get(1), pageable).map(mapper::toDto);
    }

    /**
     * Retrieves a list of all student names in the system.
     *
     * @return a list of student names
     */
    @Override
    public List<String> getAllNamesOfStudents() {
        return studentRepository.findAll().stream()
            .map(s -> s.getName().getFirstName() + " " + s.getName().getLastName())
            .toList();
    }

    private List<String> getSeparateFirstNameAndLastName(String fullName) {
        String[] firstNameAndLastName = fullName.split(" ");

        if (firstNameAndLastName.length >= 2) {
            return Arrays.asList(firstNameAndLastName);
        }

        throw new ServiceException("Full name must contain at least two words");
    }

}
