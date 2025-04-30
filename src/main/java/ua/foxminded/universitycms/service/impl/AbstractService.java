package ua.foxminded.universitycms.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.universitycms.dto.AbstractDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.AbstractEntity;
import ua.foxminded.universitycms.service.Service;

/**
 * Abstract base class for implementing service-layer components that manage CRUD operations in the university
 * management system.
 * <p>
 * This class provides a generic foundation for service implementations handling entities of type {@code E} and their
 * DTOs of type {@code D}. It encapsulates common CRUD logic (Create, Read, Update, Delete) using a {@link JpaRepository}
 * for persistence and a {@link Mapper} for entity-DTO conversions, promoting consistency and reducing boilerplate code
 * across service classes. The {@code @RequiredArgsConstructor} annotation ensures dependency injection of the
 * repository and mapper.
 *
 * @param <E> the entity type being managed, extending {@link AbstractEntity}
 * @param <D> the DTO type representing the entity, extending {@link AbstractDto}
 * @author Serhii Bohdan
 * @see Service
 * @see JpaRepository
 * @see Mapper
 * @see EntityNotFoundException
 */
@RequiredArgsConstructor
public abstract class AbstractService<E extends AbstractEntity, D extends AbstractDto> implements Service<E, D> {

    /**
     * Error message template used when an entity with the specified ID cannot be found.
     * <p>
     * This message is formatted with the entity's ID and included in an {@link EntityNotFoundException} when
     * retrieval or update
     * operations fail due to a missing entity.
     */
    private static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found with id: %s.";

    /**
     * Error message template used when an entity deletion fails due to the entity not existing.
     * <p>
     * This message is formatted with the entity's ID and included in an {@link EntityNotFoundException} when a
     * deletion attempt
     * is made on a non-existent entity.
     */
    private static final String ENTITY_DELETION_ERROR_MESSAGE = """
        Error deleting entity. Entity with the passed ID does not exist: %s.
        """;

    /**
     * Repository for performing CRUD operations on entities of type {@code E}.
     * <p>
     * This {@link JpaRepository} instance provides data access methods for persisting, retrieving, updating, and
     * deleting entities
     * in the underlying database.
     */
    protected final JpaRepository<E, Long> repository;

    /**
     * Mapper for converting between entities of type {@code E} and DTOs of type {@code D}.
     * <p>
     * This {@link Mapper} instance handles the transformation of entities to DTOs and vice versa, facilitating
     * data transfer between the service layer and other application layers.
     */
    protected final Mapper<E, D> mapper;

    /**
     * Saves a new entity based on the provided DTO representation.
     * <p>
     * Converts the DTO to an entity using the configured mapper, persists it via the repository, and returns the
     * saved entity as a DTO. This method ensures the entity is created with a generated ID, which is reflected in
     * the returned DTO.
     *
     * @param dto the DTO containing the data for the new entity, must be non-null and valid per service requirements
     * @return a {@link D} DTO representing the saved entity, including its generated ID
     */
    @Override
    public D save(D dto) {
        E entity = repository.save(mapper.toEntity(dto));
        return mapper.toDto(entity);
    }

    /**
     * Retrieves an entity by its ID and returns its DTO representation.
     * <p>
     * Queries the repository for an entity with the specified ID and maps it to a DTO if found. If no entity exists,
     * throws an {@link EntityNotFoundException} with a {@link HttpStatus#NOT_FOUND} status and a formatted error
     * message. The {@link Transactional} annotation ensures this operation is read-only for performance optimization.
     *
     * @param id the unique identifier of the entity to retrieve
     * @return the {@link D} DTO representation of the entity if found
     * @throws EntityNotFoundException if no entity with the given ID exists
     */
    @Override
    @Transactional(readOnly = true)
    public D getById(long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ENTITY_NOT_FOUND_MESSAGE.formatted(id)));
    }

    /**
     * Retrieves a list of all entities and returns their DTO representations.
     * <p>
     * Fetches all entities from the repository and converts them to a list of DTOs using the mapper. The
     * {@link Transactional} annotation ensures this operation is read-only for performance optimization.
     *
     * @return a {@link List} of {@link D} DTOs representing all entities in the system
     */
    @Override
    @Transactional(readOnly = true)
    public List<D> getAll() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .toList();
    }

    /**
     * Updates an existing entity based on the provided DTO and returns its updated DTO representation.
     * <p>
     * Retrieves the existing entity by ID, applies partial updates from the DTO using the mapper, saves the updated
     * entity to the repository, and returns the updated DTO. Throws an {@link EntityNotFoundException} if the entity
     * is not found. The {@link Transactional} annotation ensures atomicity of the update operation.
     *
     * @param dto the DTO containing the updated data for the entity, must be non-null and valid per service
     *            requirements
     * @return a {@link D} DTO representing the updated entity
     * @throws EntityNotFoundException if no entity with the DTO's ID exists
     */
    @Override
    @Transactional
    public D update(D dto) {
        E existingEntity = repository.findById(dto.getId())
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ENTITY_NOT_FOUND_MESSAGE.formatted(dto.getId())));

        E updatedEntity = mapper.partialUpdate(dto, existingEntity);
        return mapper.toDto(repository.save(updatedEntity));
    }

    /**
     * Deletes an entity by its ID.
     * <p>
     * Attempts to find and delete the entity with the specified ID from the repository. If the entity exists, it
     * is removed; otherwise, an {@link EntityNotFoundException} is thrown with a {@link HttpStatus#NOT_FOUND} status
     * and a formatted error message.
     *
     * @param id the ID of the entity to delete
     * @throws EntityNotFoundException if no entity with the given ID exists
     */
    @Override
    public void deleteById(long id) {
        Optional<E> optional = repository.findById(id);

        if (optional.isPresent()) {
            repository.delete(optional.get());
            return;
        }

        throw new EntityNotFoundException(HttpStatus.NOT_FOUND,
            ENTITY_DELETION_ERROR_MESSAGE.formatted(id));
    }

}
