package ua.foxminded.universitycms.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base class for data transfer objects (DTOs) in the university management system.
 * <p>
 * This class provides a foundation for all DTOs, defining common attributes and behaviors to ensure
 * consistency, code reusability, and maintainability across the application. Subclasses inherit from
 * this class to represent specific entities in a format suitable for data transfer between layers.
 *
 * @author Serhii Bohdan
 * @see lombok.Getter
 * @see lombok.Setter
 * @see lombok.NoArgsConstructor
 * @see lombok.ToString
 * @see lombok.experimental.SuperBuilder
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
public abstract class AbstractDto {

    /**
     * The unique identifier of the DTO.
     * <p>
     * This field represents the primary key or unique identifier associated with the entity the DTO
     * represents, typically corresponding to an entity's ID in the persistence layer.
     */
    private Long id;

}
