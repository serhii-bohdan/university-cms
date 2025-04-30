package ua.foxminded.universitycms.service;

import java.util.Collection;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.model.Group;

/**
 * Service interface for managing {@link Group} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Group} entities
 * mapped to {@link GroupDto} DTOs. It provides methods for CRUD operations inherited from {@link Service}, as well as
 * additional functionality such as paginated group retrieval, filtering by name, retrieving groups with unenrolled
 * students for a course, and extracting group names. Implementations of this interface handle business logic related
 * to group management, leveraging pagination and validation constraints for efficient and reliable operations.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Group
 * @see GroupDto
 */
public interface GroupService extends Service<Group, GroupDto> {

    /**
     * Retrieves a paginated list of groups, optionally filtered by name.
     * <p>
     * Fetches groups from the system based on the provided {@link Pageable} paging parameters and an optional name filter.
     * The {@link NotNull} constraint ensures that the paging configuration is provided.
     *
     * @param pageable  the paging and sorting configuration for the query, must be non-null
     * @param groupName an optional name filter; if null or empty, all groups are retrieved
     * @return a {@link Page} of {@link GroupDto} objects representing the filtered and paginated groups
     */
    Page<GroupDto> findGroups(@NotNull Pageable pageable, String groupName);

    /**
     * Retrieves groups containing students not enrolled in a specific course.
     * <p>
     * Identifies and returns a list of groups whose students are not currently enrolled in the course specified by
     * {@code courseId}, useful for enrollment management or course assignment scenarios.
     *
     * @param courseId the ID of the course to check for unenrolled students
     * @return a {@link List} of {@link GroupDto} objects representing groups with unenrolled students
     */
    List<GroupDto> getGroupsWithUnEnrolledStudents(long courseId);

    /**
     * Filters a list of groups by name.
     * <p>
     * Returns a subset of the provided {@link GroupDto} list where the group names match or contain the specified
     * {@code groupName}. The {@link NotNull} constraint ensures that the input list is not null. If {@code groupName}
     * is null or empty, the original list may be returned unfiltered, depending on the implementation.
     *
     * @param groups    the list of {@link GroupDto} objects to filter, must be non-null
     * @param groupName the name or partial name to filter groups by; may be null or empty
     * @return a {@link List} of {@link GroupDto} objects matching the name filter
     */
    List<GroupDto> filterGroupsByName(@NotNull List<GroupDto> groups, String groupName);

    /**
     * Extracts group names from a collection of groups.
     * <p>
     * Converts the provided collection of {@link GroupDto} objects into a list of their names. The {@link NotNull}
     * constraint ensures that the input collection is not null.
     *
     * @param groups the collection of {@link GroupDto} objects from which to extract names, must be non-null
     * @return a {@link List} of group names as strings
     */
    List<String> extractGroupNames(@NotNull Collection<GroupDto> groups);

}
