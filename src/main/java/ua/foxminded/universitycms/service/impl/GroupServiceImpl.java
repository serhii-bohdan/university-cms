package ua.foxminded.universitycms.service.impl;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Collection;
import java.util.List;

/**
 * Implementation of the {@link GroupService} interface for managing {@link Group} entities in the university
 * management system.
 * <p>
 * This service class extends {@link AbstractService} to leverage common CRUD operations and provides concrete
 * implementations for group-specific operations such as paginated retrieval, filtering by name, retrieving groups
 * with unenrolled students, and extracting group names. It uses {@link GroupRepository} for data access and a
 * {@link Mapper} for entity-DTO conversions. The {@code @Service} annotation marks it as a Spring-managed bean, and
 * {@code @Validated} enables validation.
 *
 * @author Serhii Bohdan
 * @see GroupService
 * @see AbstractService
 * @see GroupRepository
 * @see Mapper
 */
@Service
@Validated
public class GroupServiceImpl extends AbstractService<Group, GroupDto> implements GroupService {

    /**
     * Repository for performing CRUD operations on {@link Group} entities.
     * <p>
     * This {@link GroupRepository} instance provides data access methods specific to groups,
     * extending {@link JpaRepository}.
     */
    private final GroupRepository groupRepository;

    /**
     * Constructs a new {@code GroupServiceImpl} instance with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up
     * the specific {@link GroupRepository} for group management.
     *
     * @param repository the {@link JpaRepository} for {@link Group} entities, providing basic CRUD operations
     * @param mapper     the {@link Mapper} instance for converting between {@link Group} and {@link GroupDto} objects
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
    public Page<GroupDto> findGroups(Pageable pageable, String groupName) {
        return StringUtils.isBlank(groupName)
            ? groupRepository.findAll(pageable).map(mapper::toDto)
            : groupRepository.findByGroupNameIgnoreCase(groupName.strip(), pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> getGroupsWithUnEnrolledStudents(long courseId) {
        return groupRepository.findAll().stream()
            .filter(group -> hasUnEnrolledStudents(group, courseId))
            .map(mapper::toDto)
            .toList();
    }

    private boolean hasUnEnrolledStudents(Group group, long courseId) {
        return !group.getStudents().stream()
            .allMatch(student -> isStudentEnrolledInCourse(student, courseId));
    }

    private boolean isStudentEnrolledInCourse(Student student, long courseId) {
        return student.getCourses().stream()
            .anyMatch(course -> course.getId().equals(courseId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GroupDto> filterGroupsByName(List<GroupDto> groups, String groupName) {
        return StringUtils.isBlank(groupName)
            ? groups
            : groups.stream()
            .filter(group -> group.getGroupName().equals(groupName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> extractGroupNames(Collection<GroupDto> groups) {
        return groups.stream()
            .map(GroupDto::getGroupName)
            .toList();
    }

}
