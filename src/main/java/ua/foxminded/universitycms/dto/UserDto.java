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
import java.time.LocalDateTime;

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
     * The user's password.
     */
    @NotBlank(message = "Password is mandatory")
    private String password;

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
     * The date and time the user was created.
     */
    private LocalDateTime createdAt;

    /**
     * The date and time the user information was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Constructs a new {@code UserDto} instance with the specified user details including the schedule ID.
     *
     * @param firstName  the user's first name
     * @param lastName   the user's last name
     * @param email      the user's email address
     * @param password   the user's password
     * @param isActive   indicates whether the user account is active
     * @param scheduleId the ID of the schedule associated with the user
     */
    protected UserDto(String firstName, String lastName, String email, String password, Boolean isActive, Long scheduleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.scheduleId = scheduleId;
    }

    /**
     * Constructs a new {@code UserDto} instance with the specified user details.
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email address
     * @param password  the user's password
     * @param isActive  indicates whether the user account is active
     */
    protected UserDto(String firstName, String lastName, String email, String password, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

}
