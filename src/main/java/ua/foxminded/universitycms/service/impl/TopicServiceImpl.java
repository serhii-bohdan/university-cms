package ua.foxminded.universitycms.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.service.TopicService;

/**
 * Implementation of {@link TopicService} for managing {@link Topic} entities in the university system.
 * <p>
 * Extends {@link AbstractService} to leverage common CRUD operations for {@link Topic} entities mapped to
 * {@link TopicDto} DTOs. Uses {@link JpaRepository} for data access and {@link Mapper} for conversions.
 * Marked with {@code @Service} as a Spring bean and {@code @Validated} for validation support.
 *
 * @author Serhii Bohdan
 * @see TopicService
 * @see AbstractService
 * @see JpaRepository
 * @see Mapper
 */
@Service
@Validated
public class TopicServiceImpl extends AbstractService<Topic, TopicDto> implements TopicService {

    /**
     * Constructs a new {@code TopicServiceImpl} with the required dependencies.
     * Initializes {@link AbstractService} with the provided repository and mapper for managing topics.
     *
     * @param repository the {@link JpaRepository} for {@link Topic} entities, providing CRUD operations
     * @param mapper     the {@link Mapper} for converting between {@link Topic} and {@link TopicDto}
     */
    public TopicServiceImpl(JpaRepository<Topic, Long> repository, Mapper<Topic, TopicDto> mapper) {
        super(repository, mapper);
    }

}
