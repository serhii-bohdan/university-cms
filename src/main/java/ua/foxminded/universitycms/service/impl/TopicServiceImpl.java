package ua.foxminded.universitycms.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.service.TopicService;

/**
 * The {@code TopicServiceImpl} class implements the {@link TopicService} interface, providing concrete
 * implementations for managing topic entities. It extends the {@link AbstractService} class, inheriting common
 * service functionalities for basic CRUD operations and validation, and adds functionality specific to topics.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 */
@Service
@Validated
public class TopicServiceImpl extends AbstractService<Topic, TopicDto> implements TopicService {

    /**
     * Constructs a new {@code TopicServiceImpl} instance with the given dependencies.
     *
     * @param repository the repository for managing topic entities
     * @param mapper     the mapper for converting between topic entities and DTOs
     */
    public TopicServiceImpl(JpaRepository<Topic, Long> repository, Mapper<Topic, TopicDto> mapper) {
        super(repository, mapper);
    }

}
