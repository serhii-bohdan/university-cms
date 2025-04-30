package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for creating an administrator user in the university management system.
 * <p>
 * This class extends {@link UserCreationDto} to inherit common user creation attributes, such as personal
 * details, email, and password, without adding additional fields specific to administrators. It facilitates
 * secure and efficient data transfer between application layers during the creation of an administrator user,
 * leveraging the validation constraints defined in the parent class.
 *
 * @author Serhii Bohdan
 * @see UserCreationDto
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class AdminCreationDto extends UserCreationDto {
}
