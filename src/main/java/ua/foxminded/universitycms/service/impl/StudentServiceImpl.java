package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.service.StudentService;

/**
 * The {@code StudentServiceImpl} class implements the {@link StudentService}
 * interface.
 * <p>
 * This class provides the functionality for managing students.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code StudentServiceImpl} with the specified student
     * repository, group repository, schedule repository, and model mapper.
     *
     * @param studentRepository  the student repository
     * @param groupRepository    the group repository
     * @param scheduleRepository the schedule repository
     * @param modelMapper        the model mapper
     */
    public StudentServiceImpl(StudentRepository studentRepository, GroupRepository groupRepository,
            ScheduleRepository scheduleRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.scheduleRepository = scheduleRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Sets up the model mapper after the bean has been initialized.
     */
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Student.class, StudentDto.class).addMapping(src -> src.getGroup().getGroupName(),
                StudentDto::setGroupName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addStudent(StudentDto studentDto) {
        boolean isAdded = false;

        if (Objects.nonNull(studentDto) && Objects.nonNull(studentDto.getFirstName())
                && Objects.nonNull(studentDto.getLastName()) && Objects.nonNull(studentDto.getEmail())
                && Objects.nonNull(studentDto.getPassword()) && Objects.nonNull(studentDto.getIsActive())
                && Objects.nonNull(studentDto.getGroupName())) {
            studentRepository.save(mapToEntity(studentDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StudentDto> getStudentById(Long studentId) {
        StudentDto findedStudent = null;

        if (Objects.nonNull(studentId)) {
            Optional<Student> optional = studentRepository.findById(studentId);

            if (optional.isPresent()) {
                findedStudent = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedStudent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteStudentById(Long studentId) {
        boolean isDeleted = false;

        if (Objects.nonNull(studentId)) {
            Optional<Student> optional = studentRepository.findById(studentId);

            if (optional.isPresent()) {
                studentRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private StudentDto mapToDto(Student entity) {
        return modelMapper.map(entity, StudentDto.class);
    }

    private Student mapToEntity(StudentDto dto) {
        Group group = groupRepository.findByGroupName(dto.getGroupName()).get();
        Student student = null;

        if (Objects.nonNull(dto.getUserId()) && dto.getUserId() >= 1L) {
            student = studentRepository.findById(dto.getUserId()).get();
            student.setFirstName(dto.getFirstName());
            student.setLastName(dto.getLastName());
            student.setEmail(dto.getEmail());
            student.setPassword(dto.getPassword());
            student.setIsActive(dto.getIsActive());
        } else {
            Schedule schedule = new Schedule();
            scheduleRepository.save(schedule);
            student = modelMapper.map(dto, Student.class);
            student.setSchedule(schedule);
        }

        student.setGroup(group);
        return student;
    }

}
