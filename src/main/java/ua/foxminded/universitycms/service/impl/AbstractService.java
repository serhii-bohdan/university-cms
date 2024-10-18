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
 * The {@code AbstractService} class provides a foundation for implementing service-layer components that interact with
 * repositories and handle CRUD operations for entities and DTOs. It encapsulates common logic and promotes consistency
 * across service classes.
 *
 * @param <E> the type of entity being managed
 * @param <D> the type of DTO representing the entity
 * @author Serhii Bohdan
 */
@RequiredArgsConstructor
public abstract class AbstractService<E extends AbstractEntity, D extends AbstractDto> implements Service<E, D> {

    /**
     * The {@link JpaRepository} used for managing entities of type {@code E}.
     */
    protected final JpaRepository<E, Long> repository;

    /**
     * The {@link Mapper} used for converting between entities of type {@code E} and DTOs of type {@code D}.
     */
    protected final Mapper<E, D> mapper;

    /**
     * Saves a new entity based on the provided DTO representation.
     *
     * @param dto the DTO containing the data for the new entity
     * @return a new DTO representing the saved entity with its generated ID
     */
    @Override
    public D save(D dto) {
        E entity = repository.save(mapper.toEntity(dto));
        return mapper.toDto(entity);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves an entity by its ID and returns an Optional containing a DTO representation of the entity.
     *
     * @param id the ID of the entity to retrieve
     * @return an Optional containing the entity's DTO if found, or empty Optional if not found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<D> getById(long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a list of all entities and returns a list of their DTO representations.
     *
     * @return a list of DTOs representing all entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<D> getAll() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .toList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates an existing entity based on the provided DTO by:
     * <ol>
     *   <li>Converting the DTO to an entity using the mapper.</li>
     *   <li>Saving the entity to the repository (which performs an update).</li>
     *   <li>Converting the updated entity back to a DTO and returning it.</li>
     * </ol>
     *
     * @param dto the DTO containing the updated data for the entity
     * @return a DTO representing the updated entity
     */
    @Override
    @Transactional
    public D update(D dto) {
        E existingEntity = repository.findById(dto.getId())
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                String.format("Entity not found with id: %d", dto.getId())));

        E updatedEntity = mapper.partialUpdate(dto, existingEntity);
        return mapper.toDto(repository.save(updatedEntity));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws EntityNotFoundException if the entity with the given ID is not found
     */
    @Override
    public void deleteById(long id) {
        Optional<E> optional = repository.findById(id);

        if (optional.isPresent()) {
            repository.delete(optional.get());
            return;
        }

        throw new EntityNotFoundException(HttpStatus.NOT_FOUND,
            String.format("Error deleting entity. Entity with the passed ID does not exist: %d", id));
    }

}
