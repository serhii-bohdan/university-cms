package ua.foxminded.universitycms.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.MappingTarget;
import ua.foxminded.universitycms.dto.AbstractDto;
import ua.foxminded.universitycms.model.AbstractEntity;

/**
 * Generic interface defining a contract for mapping between entity and DTO (Data Transfer Object) types in the
 * university management system.
 * <p>
 * This interface provides methods to convert data between persistent storage entities (subclasses of
 * {@link AbstractEntity}) and application-layer DTOs (subclasses of {@link AbstractDto}). It supports bidirectional
 * mapping, partial updates, and bulk conversions, facilitating consistent data transformation across the application.
 * Implementations typically leverage libraries like MapStruct for efficient mapping.
 *
 * @param <E> the entity type, extending {@link AbstractEntity}
 * @param <D> the DTO type, extending {@link AbstractDto}
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see AbstractDto
 * @see org.mapstruct.MappingTarget
 */
public interface Mapper<E extends AbstractEntity, D extends AbstractDto> {

    /**
     * Converts a DTO object to its corresponding entity object.
     * <p>
     * This method maps the fields of the provided DTO to the fields of a new entity instance, enabling the
     * transformation of data from the application layer to the persistence layer.
     *
     * @param dto the DTO object to convert
     * @return the resulting entity object
     */
    E toEntity(D dto);

    /**
     * Converts an entity object to its corresponding DTO object.
     * <p>
     * This method maps the fields of the provided entity to the fields of a new DTO instance, enabling the
     * transformation of data from the persistence layer to the application layer.
     *
     * @param entity the entity object to convert
     * @return the resulting DTO object
     */
    D toDto(E entity);

    /**
     * Partially updates an existing entity object with data from a DTO object.
     * <p>
     * This method updates only the non-null fields from the DTO into the target entity, preserving other
     * existing entity data. The {@link MappingTarget} annotation indicates that the entity parameter is the
     * target of the update operation.
     *
     * @param dto    the DTO object containing the updated data
     * @param entity the existing entity object to update
     * @return the updated entity object
     */
    E partialUpdate(D dto, @MappingTarget E entity);

    /**
     * Converts a collection of entity objects to a list of corresponding DTO objects.
     * <p>
     * This method performs bulk mapping, transforming each entity in the provided collection into its
     * respective DTO representation.
     *
     * @param entities the collection of entity objects to convert
     * @return a {@link List} of DTO objects
     */
    List<D> toDtoList(Collection<E> entities);

}
