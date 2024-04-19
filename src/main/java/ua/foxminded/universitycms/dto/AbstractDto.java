package ua.foxminded.universitycms.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The {@code AbstractDto} class serves as a base class for data transfer objects (DTOs) in the system.
 * It defines common attributes and behavior for all DTOs, promoting code clarity and maintainability.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
public abstract class AbstractDto {

    /**
     * The unique identifier of the DTO.
     */
    private Long id;

}
