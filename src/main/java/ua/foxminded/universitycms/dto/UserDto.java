package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.time.ZonedDateTime;

/**
 * The {@code UserDto} class is a concrete DTO (Data Transfer Object)
 * that extends the {@link AbstractDto} class. It represents a user
 * entity in the system and provides methods for accessing and manipulating
 * user data.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public abstract class UserDto extends AbstractDto {

    /**
     * The user's first name.
     */
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255)
    private String firstName;

    /**
     * The user's last name.
     */
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255)
    private String lastName;

    /**
     * The user's email address.
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Email address is not valid")
    private String email;

    /**
     * The ID of the role associated with the user.
     */
    private Long roleId;

    /**
     * Indicates whether the user account is active.
     */
    @NotNull(message = "Status is mandatory")
    private Boolean isActive;

    /**
     * The ID of the schedule associated with the user.
     */
    private Long scheduleId;

    /**
     * The date and time (including time zone) when the user record was created.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time (including time zone) when the user record was last updated.
     */
    private ZonedDateTime updatedAt;

}
