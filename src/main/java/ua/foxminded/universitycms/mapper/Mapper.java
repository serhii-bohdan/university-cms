package ua.foxminded.universitycms.mapper;

import ua.foxminded.universitycms.dto.AbstractDto;
import ua.foxminded.universitycms.model.AbstractEntity;

/**
 * The {@code Mapper} interface defines a contract for converting between entity and DTO (Data Transfer Object) objects.
 * It's typically used to map data between persistent storage (entities) and the application layer (DTOs).
 *
 * @param <E> the type of the entity object (must extend {@link AbstractEntity})
 * @param <D> the type of the DTO object (must extend {@link AbstractDto})
 * @author Serhii Bohdan
 */
public interface Mapper<E extends AbstractEntity, D extends AbstractDto> {

    /**
     * Converts a DTO object to its corresponding entity object. This method is responsible
     * for mapping the DTOs fields to the corresponding entity's fields.
     *
     * @param dto the DTO object to be converted
     * @return the converted entity object
     */
    E toEntity(D dto);

    /**
     * Converts an entity object to its corresponding DTO object.
     * This method is responsible for mapping the entity's fields to the corresponding DTO's fields.
     *
     * @param entity the entity object to be converted
     * @return the converted DTO object
     */
    D toDto(E entity);

}
