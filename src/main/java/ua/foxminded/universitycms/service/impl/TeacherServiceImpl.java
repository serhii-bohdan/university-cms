package ua.foxminded.universitycms.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code TeacherServiceImpl} class implements the {@link TeacherService} interface, providing concrete
 * implementations for managing teacher entities. It extends the {@link UserService} class, inheriting core user
 * management functionality and adding teacher-specific operations like name-based retrieval.
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
@Transactional
public class TeacherServiceImpl extends UserService<Teacher, TeacherDto> implements TeacherService {

    /**
     * The {@link TeacherRepository} used for managing teacher entities.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Constructs a new {@code TeacherServiceImpl} instance with the given dependencies.
     *
     * @param repository         the repository for managing teacher entities
     * @param mapper             the mapper for converting between teacher entities and DTOs
     * @param scheduleRepository the repository for managing schedules (potentially associated with teachers)
     * @param passwordEncoder    the password encoder for securely encoding teacher passwords
     */
    public TeacherServiceImpl(JpaRepository<Teacher, Long> repository, Mapper<Teacher, TeacherDto> mapper,
                              ScheduleRepository scheduleRepository, PasswordEncoder passwordEncoder) {
        super(repository, mapper, scheduleRepository, passwordEncoder);
        this.teacherRepository = (TeacherRepository) repository;
    }

    /**
     * Retrieves a page of teacher data containing all teachers. This method retrieves a
     * paginated list of all teachers from the underlying data store. It utilizes the provided
     * `Pageable` object to specify the page number, size, and sorting criteria (if applicable).
     *
     * @param pageable the Pageable object containing pagination information (size, page)
     *                 (must not be null)
     * @return a Page object containing a list of TeacherDto objects representing the requested page of teachers
     */
    @Override
    public Page<TeacherDto> getTeachersPage(Pageable pageable) {
        return teacherRepository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * Retrieves a page of teacher data filtered by full name. This method retrieves a paginated
     * list of teachers whose full names contain the provided keyword. It utilizes the `Pageable`
     * object to specify the page number, size.
     *
     * @param fullName the keyword to filter teachers by full name (can be blank)
     *                 (must not be null)
     * @param pageable the Pageable object containing pagination information (size, page)
     *                 (must not be null)
     * @return a Page object containing a list of TeacherDto objects representing the requested page of filtered teachers
     */
    @Override
    public Page<TeacherDto> getTeacherInPageByName(String fullName, Pageable pageable) {
        List<String> names = getSeparateFirstNameAndLastName(fullName.strip());
        return teacherRepository.findByName_FirstNameAndName_LastNameIgnoreCase(names.get(0), names.get(1), pageable).map(mapper::toDto);
    }

    /**
     * Retrieves a list of all teacher names in the system.
     *
     * @return a list of teacher names
     */
    @Override
    public List<String> getAllNamesOfTeachers() {
        return teacherRepository.findAll().stream()
            .map(t -> t.getName().getFirstName() + " " + t.getName().getLastName())
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
