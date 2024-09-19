package ua.foxminded.universitycms.dto;

import java.time.ZonedDateTime;
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
     * The ID of the role associated with the manager.
     */
    private Long roleId;

    /**
     * The date and time (including time zone) when the manager record was created.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time (including time zone) when the manager record was last updated.
     */
    private ZonedDateTime updatedAt;

}
