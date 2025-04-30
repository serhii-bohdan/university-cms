package ua.foxminded.universitycms.service;

import java.util.Collection;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.ManagerCreationDto;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Manager;

/**
 * Service interface for managing {@link Manager} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Manager}
 * entities mapped to {@link ManagerDto} DTOs. It provides methods for CRUD operations inherited from {@link Service},
 * along with additional functionality such as creating managers from creation DTOs, paginated retrieval with email
 * filtering, email extraction, password updates, and full name updates. Implementations of this interface handle
 * business logic related to manager management, leveraging validation constraints for data integrity.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Manager
 * @see ManagerDto
 * @see ManagerCreationDto
 * @see PasswordUpdateRequestDto
 * @see FullName
 */
public interface ManagerService extends Service<Manager, ManagerDto> {

    /**
     * Saves a new manager entity based on the provided creation DTO.
     * <p>
     * Creates a new manager in the system using the data from the {@link ManagerCreationDto}, applying validation
     * rules ({@link Valid}) to ensure data integrity and the {@link NotNull} constraint to ensure the DTO is provided.
     *
     * @param dto the {@link ManagerCreationDto} containing the data for the new manager, must be non-null and valid
     * @return a {@link ManagerDto} representing the saved manager, including its generated ID
     */
    ManagerDto save(@NotNull @Valid ManagerCreationDto dto);

    /**
     * Retrieves a paginated list of managers, optionally filtered by email.
     * <p>
     * Fetches managers from the system based on the provided {@link Pageable} paging parameters and an optional
     * email filter. The {@link NotNull} constraint ensures that the paging configuration is provided.
     *
     * @param pageable the paging and sorting configuration for the query, must be non-null
     * @param email    an optional email filter; if null or empty, all managers are retrieved
     * @return a {@link Page} of {@link ManagerDto} objects representing the filtered and paginated managers
     */
    Page<ManagerDto> findManagers(@NotNull Pageable pageable, String email);

    /**
     * Extracts email addresses from a collection of managers.
     * <p>
     * Converts the provided collection of {@link ManagerDto} objects into a list of their email addresses.
     * The {@link NotNull} constraint ensures that the input collection is not null.
     *
     * @param managers the collection of {@link ManagerDto} objects from which to extract emails, must be non-null
     * @return a {@link List} of email addresses as strings
     */
    List<String> extractManagerEmails(@NotNull Collection<ManagerDto> managers);

    /**
     * Updates a manager's password based on the provided request.
     * <p>
     * Modifies the password of a manager identified in the {@link PasswordUpdateRequestDto}, ensuring the request
     * meets validation criteria. The {@link NotNull} constraint ensures that the request DTO is provided.
     *
     * @param passwordUpdateRequest the {@link PasswordUpdateRequestDto} containing the password update details,
     *                              must be non-null
     */
    void updateManagerPassword(@NotNull PasswordUpdateRequestDto passwordUpdateRequest);

    /**
     * Updates a manager's full name.
     * Changes the full name of the manager identified by {@code managerId} to the provided {@link FullName} object.
     *
     * @param managerId the ID of the manager whose full name is to be updated
     * @param fullName  the new {@link FullName} object containing the updated first and last names
     */
    void updateManagerFullName(long managerId, FullName fullName);

}
