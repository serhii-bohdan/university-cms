package ua.foxminded.universitycms.service.impl;

import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Group;
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
@Transactional
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
     * Retrieves a page of all groups from the database using pagination.
     *
     * @param pageable the pagination information specifying the page number
     * @return a page of group DTOs representing the requested page of groups with pagination information
     */
    @Override
    public Page<GroupDto> getGroupsPage(Pageable pageable) {
        return groupRepository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * Retrieves a page of groups filtered by name using pagination.
     *
     * @param name     the name of the group to filter by
     * @param pageable the pagination information specifying the page number, size
     * @return a page of group DTOs representing the requested page of filtered groups with pagination information
     */
    @Override
    public Page<GroupDto> getGroupInPageByName(String name, Pageable pageable) {
        return groupRepository.findByGroupNameIgnoreCase(name.strip(), pageable).map(mapper::toDto);
    }

    /**
     * Retrieves a list of all group names in the system.
     *
     * @return a list of group names
     */
    @Override
    public List<String> getAllNamesOfGroups() {
        return groupRepository.findAll().stream()
            .map(Group::getGroupName)
            .toList();
    }

}
