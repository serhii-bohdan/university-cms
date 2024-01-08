package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.service.GroupService;

/**
 * The {@code GroupServiceImpl} class implements the {@link GroupService}
 * interface.
 * <p>
 * This class provides the functionality for managing groups.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code GroupServiceImpl} with the specified group repository
     * and model mapper.
     *
     * @param groupRepository the group repository
     * @param modelMapper     the model mapper
     */
    public GroupServiceImpl(GroupRepository groupRepository, ModelMapper modelMapper) {
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addGroup(GroupDto groupDto) {
        boolean isAdded = false;

        if (Objects.nonNull(groupDto) && Objects.nonNull(groupDto.getGroupName())) {
            groupRepository.save(mapToEntity(groupDto));
            isAdded = true;
        }

        return isAdded;
    }

    @Override
    public Optional<GroupDto> getGroupById(Long groupId) {
        GroupDto findedGroup = null;

        if (Objects.nonNull(groupId)) {
            Optional<Group> optional = groupRepository.findById(groupId);

            if (optional.isPresent()) {
                findedGroup = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedGroup);
    }

    @Override
    public boolean deleteGroupById(Long groupId) {
        boolean isDeleted = false;

        if (Objects.nonNull(groupId)) {
            Optional<Group> optional = groupRepository.findById(groupId);

            if (optional.isPresent()) {
                groupRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private GroupDto mapToDto(Group entity) {
        return modelMapper.map(entity, GroupDto.class);
    }

    private Group mapToEntity(GroupDto dto) {
        Group group = null;

        if (Objects.nonNull(dto.getGroupId()) && dto.getGroupId() >= 1L) {
            group = groupRepository.findById(dto.getGroupId()).get();
            group.setGroupName(dto.getGroupName());
        } else {
            group = modelMapper.map(dto, Group.class);
        }

        return group;
    }

}
