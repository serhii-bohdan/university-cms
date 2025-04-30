package ua.foxminded.universitycms.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.universitycms.dto.AdminCreationDto;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.FullName;
import java.util.Collection;
import java.util.List;

/**
 * Service interface for managing {@link Admin} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Admin} entities
 * mapped to {@link AdminDto} DTOs. It provides methods for creating, retrieving, updating, and deleting admin users,
 * along with additional functionality such as paginated admin retrieval, email extraction, password updates, and full
 * name updates. Implementations of this interface handle business logic related to admin management, leveraging
 * validation constraints for data integrity.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Admin
 * @see AdminDto
 * @see AdminCreationDto
 * @see PasswordUpdateRequestDto
 * @see FullName
 */
public interface AdminService extends Service<Admin, AdminDto> {

    /**
     * Saves a new admin entity based on the provided creation DTO.
     * <p>
     * Creates a new admin in the system using the data from the {@link AdminCreationDto}, applying validation rules
     * ({@link Valid}) to ensure data integrity and the {@link NotNull} constraint to ensure the DTO is provided.
     *
     * @param dto the {@link AdminCreationDto} containing the data for the new admin, must be non-null and valid
     * @return an {@link AdminDto} representing the saved admin, including its generated ID
     */
    AdminDto save(@NotNull @Valid AdminCreationDto dto);

    /**
     * Retrieves a paginated list of admins, optionally filtered by email.
     * <p>
     * Fetches admins from the system based on the provided {@link Pageable} paging parameters and an optional email
     * filter. The {@link NotNull} constraint ensures that the paging configuration is provided.
     *
     * @param pageable the paging and sorting configuration for the query, must be non-null
     * @param email    an optional email filter; if null or empty, all admins are retrieved
     * @return a {@link Page} of {@link AdminDto} objects representing the filtered and paginated admins
     */
    Page<AdminDto> findAdmins(@NotNull Pageable pageable, String email);

    /**
     * Extracts email addresses from a collection of admins.
     * <p>
     * Converts the provided collection of {@link AdminDto} objects into a list of their email addresses. The
     * {@link NotNull} constraint ensures that the input collection is not null.
     *
     * @param admins the collection of {@link AdminDto} objects from which to extract emails, must be non-null
     * @return a {@link List} of email addresses as strings
     */
    List<String> extractAdminEmails(@NotNull Collection<AdminDto> admins);

    /**
     * Updates an admin's password based on the provided request.
     * <p>
     * Modifies the password of an admin identified in the {@link PasswordUpdateRequestDto}, ensuring the request
     * meets validation criteria. The {@link NotNull} constraint ensures that the request DTO is provided.
     *
     * @param passwordUpdateRequest the {@link PasswordUpdateRequestDto} containing the password update details,
     *                              must be non-null
     */
    void updateAdminPassword(@NotNull PasswordUpdateRequestDto passwordUpdateRequest);

    /**
     * Updates an admin's full name.
     * Changes the full name of the admin identified by {@code adminId} to the provided {@link FullName} object.
     *
     * @param adminId  the ID of the admin whose full name is to be updated
     * @param fullName the new {@link FullName} object containing the updated first and last names
     */
    void updateAdminFullName(long adminId, FullName fullName);

}
