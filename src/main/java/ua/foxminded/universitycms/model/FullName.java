package ua.foxminded.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Represents a person's full name, consisting of a first name and a last name.
 * <p>
 * This class is designed to be embedded within other entities (e.g., {@link User}) using JPA's
 * {@code @Embeddable} annotation. It enforces validation constraints to ensure that both the first
 * and last names are non-empty and do not exceed a maximum length of 255 characters.
 *
 * @author Serhii Bohdan
 * @see jakarta.persistence.Embeddable
 * @see User
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class FullName {

    /**
     * The person's first name.
     * <p>
     * This field is mandatory and must not exceed 255 characters. It is mapped to the
     * {@code first_name} column in the database.
     *
     * @see jakarta.validation.constraints.NotBlank
     * @see jakarta.validation.constraints.Size
     */
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255, message = "First name must be 255 characters or less")
    @Column(name = "first_name")
    private String firstName;

    /**
     * The person's last name.
     * <p>
     * This field is mandatory and must not exceed 255 characters. It is mapped to the
     * {@code last_name} column in the database.
     *
     * @see jakarta.validation.constraints.NotBlank
     * @see jakarta.validation.constraints.Size
     */
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255, message = "Last name must be 255 characters or less")
    @Column(name = "last_name")
    private String lastName;

}
