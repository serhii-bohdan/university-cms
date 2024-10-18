package ua.foxminded.universitycms.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.TeacherService;
import java.util.Arrays;
import java.util.List;

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
     * The {@link TeacherRepository} used for managing teacher entities.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Mapper for converting between {@link Teacher} entities and {@link TeacherDto} objects.
     */
    private final TeacherMapper teacherMapper;

    /**
     * The {@link ScheduleRepository} used for managing user schedules.
     */
    protected final ScheduleRepository scheduleRepository;

    /**
     * The {@link PasswordEncoder} used for securely encoding user passwords.
     */
    protected final PasswordEncoder passwordEncoder;

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
        super(repository, mapper);
        this.teacherRepository = (TeacherRepository) repository;
        this.teacherMapper = (TeacherMapper) mapper;
        this.scheduleRepository = scheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDto save(TeacherDto dto, String password) {
        Schedule teacherSchedule = new Schedule();
        scheduleRepository.save(teacherSchedule);

        Teacher teacher = teacherMapper.toEntity(dto);
        teacher.setSchedule(teacherSchedule);
        teacher.setPasswordHash(passwordEncoder.encode(password));

        return teacherMapper.toDto(teacherRepository.save(teacher));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDto update(TeacherDto dto) {
        Teacher existingTeacher = teacherRepository.findById(dto.getId())
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                String.format("Teacher not found with id: %d", dto.getId())));

        Teacher updatedTeacher = teacherMapper.partialUpdate(dto, existingTeacher);
        return teacherMapper.toDto(teacherRepository.save(updatedTeacher));
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

        throw new InvalidFullNameFormatException(HttpStatus.BAD_REQUEST,
            "Full name must contain at least two words");
    }

}
