package ua.foxminded.universitycms.service;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.model.Group;

/**
 * The {@code GroupService} interface defines a set of operations for managing {@link Group} entities and their
 * corresponding {@link GroupDto} representations. It extends the generic {@link Service} interface, offering
 * specialized services for managing groups within the system, including paginated retrieval, searching by name,
 * and retrieving all group names for efficient listing.
 *
 * @author Serhii Bohdan
 */
public interface GroupService extends Service<Group, GroupDto> {

    /**
     * Retrieves a page of group data containing all groups. This method retrieves a paginated
     * list of all groups from the underlying data store. It utilizes the provided `Pageable`
     * object to specify the page number, size.
     *
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of GroupDto objects representing the requested page of groups
     */
    Page<GroupDto> getGroupsPage(@NotNull Pageable pageable);

    /**
     * Retrieves a page of group data filtered by name. This method retrieves a paginated list of groups
     * whose names contain (case-insensitive) the provided keyword. It utilizes the `Pageable` object to
     * specify the page number, size.
     *
     * @param name     the keyword to filter groups by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return a Page object containing a list of GroupDto objects representing the requested page of filtered groups
     */
    Page<GroupDto> getGroupInPageByName(@NotNull String name, @NotNull Pageable pageable);

    /**
     * Retrieves a list of groups whose students are not all enrolled in a specified course.
     * <p>
     * This method fetches all groups from the `groupRepository` and filters them based on whether all students
     * in the group are enrolled in the course. The filtered groups are then converted to DTOs using the mapper.
     *
     * @param courseId the ID of the course to check for student enrollment.
     * @return a list of {@link GroupDto} objects representing the groups whose students are not fully enrolled in the course.
     */
    List<GroupDto> getListOfGroupsWhoseStudentsNotEnrolledInCourse(long courseId);

}
