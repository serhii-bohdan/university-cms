package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) representing an administrator user in the university management system.
 * <p>
 * This class extends {@link UserDto} to inherit common user attributes and encapsulates essential
 * information specific to an administrator for secure and efficient data transfer between application
 * layers. Sensitive details, such as the password hash, are intentionally excluded to enhance security.
 *
 * @author Serhii Bohdan
 * @see UserDto
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class AdminDto extends UserDto {
}
