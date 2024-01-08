package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.GroupDto;

/**
 * The {@code GroupService} interface provides methods for managing groups.
 * <p>
 * This interface includes methods for adding a group, getting a group by ID,
 * and deleting a group by ID.
 *
 * @author Serhii Bohdan
 */
public interface GroupService {

    /**
     * Adds a new group.
     *
     * @param groupDto the group DTO to add
     * @return true if the group was added successfully, false otherwise
     */
    boolean addGroup(GroupDto groupDto);

    /**
     * Gets a group by ID.
     *
     * @param groupId the ID of the group to get
     * @return an Optional containing the group DTO if found, an empty Optional
     *         otherwise
     */
    Optional<GroupDto> getGroupById(Long groupId);

    /**
     * Deletes a group by ID.
     *
     * @param groupId the ID of the group to delete
     * @return true if the group was deleted successfully, false otherwise
     */
    boolean deleteGroupById(Long groupId);

}
