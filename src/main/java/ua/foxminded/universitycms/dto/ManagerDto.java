package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.model.Manager;

/**
 * Data Transfer Object (DTO) representing a manager user in the university management system.
 * <p>
 * This class extends {@link UserDto} to inherit common user attributes and encapsulates essential
 * information specific to a {@link Manager} entity for secure and efficient data transfer between
 * application layers. Sensitive details, such as the hashed password, are intentionally excluded to
 * enhance security.
 *
 * @author Serhii Bohdan
 * @see UserDto
 * @see Manager
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class ManagerDto extends UserDto {
}
