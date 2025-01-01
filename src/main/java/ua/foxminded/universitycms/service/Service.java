package ua.foxminded.universitycms.service;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ua.foxminded.universitycms.dto.AbstractDto;
import ua.foxminded.universitycms.model.AbstractEntity;

/**
 * The {@code Service} interface defines a set of generic CRUD (Create, Read, Update, Delete) operations for managing
 * entities and their corresponding DTOs. It promotes a consistent approach to interacting with domain objects within
 * service-layer components.
 *
 * @param <E> the type of entity being managed
 * @param <D> the type of DTO representing the entity
 * @author Serhii Bohdan
 */
public interface Service<E extends AbstractEntity, D extends AbstractDto> {

    /**
     * Saves a new entity based on the provided DTO representation.
     *
     * @param dto the DTO containing the data for the new entity
     * @return a new DTO representing the saved entity with its generated ID
     */
    D save(@Valid @NotNull D dto);

    /**
     * Retrieves an entity by its ID and converts it to a {@link D} DTO representation.
     *
     * @param id the unique identifier of the entity to retrieve
     * @return an {@link Optional} containing the DTO representation of the entity if found,
     * or an empty {@link Optional} if the entity does not exist
     */
    D getById(long id);

    /**
     * Retrieves a list of all entities and returns a list of their DTO representations.
     *
     * @return a list of DTOs representing all entities
     */
    List<D> getAll();

    /**
     * Updates an existing entity based on the provided DTO and returns a DTO representing the updated entity.
     *
     * @param dto the DTO containing the updated data for the entity
     * @return a DTO representing the updated entity
     */
    D update(@Valid @NotNull D dto);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void deleteById(long id);

}
