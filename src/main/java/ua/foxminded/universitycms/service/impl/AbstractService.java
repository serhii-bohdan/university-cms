package ua.foxminded.universitycms.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.AbstractDto;
import ua.foxminded.universitycms.exception.ServiceException;
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
@Transactional
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
     * Constructs a new {@code AbstractService} instance with the given repository and mapper.
     *
     * @param repository the repository to use for entity management
     * @param mapper     the mapper to use for entity-DTO conversions
     */
    protected AbstractService(JpaRepository<E, Long> repository, Mapper<E, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Saves a new entity based on the provided DTO representation.
     *
     * @param dto the DTO containing the data for the new entity
     * @return a new DTO representing the saved entity with its generated ID
     * @throws ServiceException if an error occurs during the saving process
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
    public D update(D dto) {
        E entity = repository.save(mapper.toEntity(dto));
        return mapper.toDto(entity);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws ServiceException if the entity with the given ID is not found
     */
    @Override
    public void deleteById(long id) {
        Optional<E> optional = repository.findById(id);

        if (optional.isPresent()) {
            repository.delete(optional.get());
            return;
        }

        throw new ServiceException("Error deleting entity. Entity with the " +
            "passed ID does not exist.");
    }

}
