package ua.foxminded.universitycms.service;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ua.foxminded.universitycms.dto.AbstractDto;
import ua.foxminded.universitycms.model.AbstractEntity;

/**
 * Generic interface defining CRUD operations for managing entities and their DTOs in the university management system.
 * <p>
 * This interface provides a standardized contract for service-layer components to perform Create, Read, Update, and
 * Delete (CRUD) operations on domain entities and their corresponding Data Transfer Objects (DTOs). It ensures
 * consistency across different service implementations by enforcing type safety and validation constraints. The type
 * parameters {@code E} and {@code D} allow the interface to be used with any entity-DTO pair extending
 * {@link AbstractEntity} and {@link AbstractDto}, respectively.
 *
 * @param <E> the entity type being managed, extending {@link AbstractEntity}
 * @param <D> the DTO type representing the entity, extending {@link AbstractDto}
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see AbstractDto
 */
public interface Service<E extends AbstractEntity, D extends AbstractDto> {

    /**
     * Saves a new entity based on the provided DTO representation.
     * <p>
     * Creates a new entity in the system using the data from the DTO, applying validation rules
     * ({@link Valid}) to ensure data integrity. The {@link NotNull} constraint ensures the DTO is not null.
     *
     * @param dto the DTO containing the data for the new entity, must be non-null and valid
     * @return a new DTO representing the saved entity, including its generated ID
     */
    D save(@Valid @NotNull D dto);

    /**
     * Retrieves an entity by its ID and converts it to a DTO representation.
     * <p>
     * Queries the system for an entity with the specified ID and returns its DTO representation wrapped in an
     * {@link Optional}. If no entity exists with the given ID, an empty {@link Optional} is returned.
     *
     * @param id the unique identifier of the entity to retrieve
     * @return an {@link Optional} containing the DTO of the entity if found, or an empty {@link Optional} if not
     */
    D getById(long id);

    /**
     * Retrieves a list of all entities and returns their DTO representations.
     * <p>
     * Fetches all entities from the system and converts them into a list of DTOs for use in the application layer.
     *
     * @return a {@link List} of DTOs representing all entities in the system
     */
    List<D> getAll();

    /**
     * Updates an existing entity based on the provided DTO and returns its updated DTO representation.
     * <p>
     * Modifies an existing entity using the data from the DTO, applying validation rules ({@link Valid}) to ensure
     * data integrity. The {@link NotNull} constraint ensures the DTO is not null.
     *
     * @param dto the DTO containing the updated data for the entity, must be non-null and valid
     * @return a DTO representing the updated entity
     */
    D update(@Valid @NotNull D dto);

    /**
     * Deletes an entity by its ID.
     * <p>
     * Removes the entity with the specified ID from the system. If the entity does not exist, the implementation
     * may handle this gracefully or throw an exception, depending on the specific service logic.
     *
     * @param id the ID of the entity to delete
     */
    void deleteById(long id);

}
