package ua.foxminded.universitycms.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.model.Manager;

/**
 * The {@code ManagerDto} class is a Data Transfer Object (DTO) that extends the
 * abstract {@link AbstractDto} class. It represents a simplified representation of a
 * {@link Manager} entity, suitable for data transfer between layers of the application.
 * This DTO excludes sensitive information like the hashed password for security reasons.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class ManagerDto extends AbstractDto {

    /**
     * The manager's first name.
     */
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255)
    private String firstName;

    /**
     * The manager's last name.
     */
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255)
    private String lastName;

    /**
     * The manager's email address.
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Email address is not valid")
    private String email;

    /**
     * The manager's password (typically hashed or encrypted).
     */
    @NotBlank(message = "Password is mandatory")
    private String password;

    /**
     * The date and time the manager record was created.
     */
    private LocalDateTime createdAt;

    /**
     * The date and time the manager record was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Constructs a new {@code ManagerDto} object with all fields provided.
     *
     * @param firstName the manager's first name
     * @param lastName  the manager's last name
     * @param email     the manager's email address
     * @param password  the manager's password (in plain text, use with caution)
     */
    public ManagerDto(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

}
