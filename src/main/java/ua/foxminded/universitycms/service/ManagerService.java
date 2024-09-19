package ua.foxminded.universitycms.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.model.Manager;
import java.util.List;

/**
 * The {@code ManagerService} interface extends the {@link Service} interface and defines methods for managing {@link Manager} entities.
 * It provides functionality for creating, retrieving, updating, and deleting managers, likely using DTOs (Data Transfer Objects)
 * for data transfer between the service layer and other application layers.
 *
 * @author Serhii Bohdan
 */
public interface ManagerService extends Service<Manager, ManagerDto> {

    /**
     * Creates a new manager user.
     * <p>
     * This method takes a {@link ManagerDto} object and a plain-text `password`, encodes the password using the
     * {@link PasswordEncoder}, and then saves the new manager entity to the database. The provided `dto` must
     * be valid according to its validation constraints, and the `password` must not be blank.
     *
     * @param dto      the {@link ManagerDto} object representing the new manager
     * @param password the plain-text password for the new manager
     * @return the saved {@link ManagerDto} object, with the password hashed
     */
    ManagerDto save(@NotNull @Valid ManagerDto dto, @NotBlank String password);

    /**
     * Retrieves a page of manager data.
     *
     * @param pageable the pagination information (page number, page size, sorting)
     * @return a Page object containing a list of ManagerDto objects representing the requested page of managers
     */
    Page<ManagerDto> getManagersPage(@NotNull Pageable pageable);

    /**
     * Retrieves a page of managers filtered by name.
     *
     * @param fullName the full name (or part of it) to filter managers by
     * @param pageable the pagination information (page number, page size, sorting)
     * @return a Page object containing a list of ManagerDto objects representing the requested page of filtered managers
     */
    Page<ManagerDto> getManagerInPageByName(@NotNull String fullName, @NotNull Pageable pageable);

    /**
     * Retrieves a list of all manager names.
     *
     * @return a list of strings representing the full names of all managers
     */
    List<String> getAllNamesOfManagers();

}
