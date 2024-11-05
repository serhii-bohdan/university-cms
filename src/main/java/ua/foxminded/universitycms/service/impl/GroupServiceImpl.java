package ua.foxminded.universitycms.service.impl;

import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.service.GroupService;

/**
 * The {@code GroupServiceImpl} class implements the {@link GroupService} interface, providing concrete
 * implementations for managing group entities. It extends the {@link AbstractService} class to inherit common
 * service functionalities and adds group-specific operations.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see GroupRepository
 */
@Service
@Validated
public class GroupServiceImpl extends AbstractService<Group, GroupDto> implements GroupService {

    /**
     * The {@link GroupRepository} used for managing group entities.
     */
    private final GroupRepository groupRepository;

    /**
     * Constructs a new {@code GroupServiceImpl} instance with the given dependencies.
     *
     * @param repository the repository for managing group entities
     * @param mapper     the mapper for converting between group entities and DTOs
     */
    public GroupServiceImpl(JpaRepository<Group, Long> repository, Mapper<Group, GroupDto> mapper) {
        super(repository, mapper);
        this.groupRepository = (GroupRepository) repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupDto> getGroupsPage(Pageable pageable) {
        return groupRepository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupDto> getGroupInPageByName(String name, Pageable pageable) {
        return groupRepository.findByGroupNameIgnoreCase(name.strip(), pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> getListOfGroupsWhoseStudentsNotEnrolledInCourse(long courseId) {
        return groupRepository.findAll().stream()
            .filter(g -> isNotAllStudentsFromGroupEnrolledInCourse(g, courseId))
            .map(mapper::toDto)
            .toList();
    }

    private boolean isNotAllStudentsFromGroupEnrolledInCourse(Group group, long courseId) {
        return !group.getStudents().stream()
            .allMatch(s -> isStudentEnrolledInCourse(s, courseId));
    }

    private boolean isStudentEnrolledInCourse(Student student, long courseId) {
        return student.getCourses().stream()
            .anyMatch(c -> c.getId().equals(courseId));
    }

}
