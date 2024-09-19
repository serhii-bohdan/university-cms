package ua.foxminded.universitycms.service;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.model.Admin;
import java.util.List;

/**
 * The {@code AdminService} interface extends the {@link Service} interface and defines methods for managing {@link Admin} entities.
 * It provides functionality for creating, retrieving, updating, and deleting admin users, likely using DTOs (Data Transfer Objects)
 * for data transfer between the service layer and other application layers.
 *
 * @author Serhii Bohdan
 */
public interface AdminService extends Service<Admin, AdminDto> {

    /**
     * Creates a new admin user.
     * <p>
     * This method takes an {@link AdminDto} object and a plain-text `password`, encodes the password using the
     * {@link PasswordEncoder}, and then saves the new admin entity to the database.
     *
     * @param dto      the {@link AdminDto} object representing the new admin
     * @param password the plain-text password for the new admin
     * @return the saved {@link AdminDto} object, with the password hashed
     */
    AdminDto save(@NotNull @Valid AdminDto dto, @NotBlank String password);

    /**
     * Retrieves a page of admin user data.
     *
     * @param pageable the pagination information (page number, page size, sorting)
     * @return a Page object containing a list of AdminDto objects representing the requested page of admins
     */
    Page<AdminDto> getAdminsPage(@NotNull Pageable pageable);

    /**
     * Retrieves a page of admin users filtered by name.
     *
     * @param fullName the full name (or part of it) to filter admins by
     * @param pageable the pagination information (page number, page size, sorting)
     * @return a Page object containing a list of AdminDto objects representing the requested page of filtered admins
     */
    Page<AdminDto> getAdminInPageByName(@NotNull String fullName, @NotNull Pageable pageable);

    /**
     * Retrieves a list of all admin names.
     *
     * @return a list of strings representing the full names of all admins
     */
    List<String> getAllNamesOfAdmins();

}
