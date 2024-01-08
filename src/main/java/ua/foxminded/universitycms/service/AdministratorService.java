package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.AdministratorDto;

/**
 * The {@code AdministratorService} interface provides methods for managing
 * administrators.
 * <p>
 * This interface includes methods for adding an administrator, getting an
 * administrator by ID, and deleting an administrator by ID.
 *
 * @author Serhii Bohdan
 */
public interface AdministratorService {

    /**
     * Adds a new administrator.
     *
     * @param administratorDto the administrator DTO to add
     * @return true if the administrator was added successfully, false otherwise
     */
    boolean addAdministrator(AdministratorDto administratorDto);

    /**
     * Gets an administrator by ID.
     *
     * @param administratorId the ID of the administrator to get
     * @return an Optional containing the administrator DTO if found, an empty
     *         Optional otherwise
     */
    Optional<AdministratorDto> getAdministratorById(Long administratorId);

    /**
     * Deletes an administrator by ID.
     *
     * @param administratorId the ID of the administrator to delete
     * @return true if the administrator was deleted successfully, false otherwise
     */
    boolean deleteAdministratorById(Long administratorId);

}
