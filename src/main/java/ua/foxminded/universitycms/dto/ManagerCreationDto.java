package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for creating a manager user in the university management system.
 * <p>
 * This class extends {@link UserCreationDto} to inherit common user creation attributes, such as personal
 * details, email, and password, without adding additional fields specific to managers. It facilitates secure
 * and efficient data transfer between application layers during the creation of a manager user, leveraging
 * the validation constraints defined in the parent class.
 *
 * @author Serhii Bohdan
 * @see UserCreationDto
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class ManagerCreationDto extends UserCreationDto {
}
