package ua.foxminded.universitycms.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.service.LessonService;

/**
 * The {@code LessonServiceImpl} class implements the {@link LessonService} interface, providing concrete
 * implementations for managing lesson entities. It extends the {@link AbstractService} class, inheriting common
 * service functionalities for basic CRUD operations and validation.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 */
@Service
@Validated
@Transactional
public class LessonServiceImpl extends AbstractService<Lesson, LessonDto> implements LessonService {

    /**
     * Constructs a new {@code LessonServiceImpl} instance with the given dependencies.
     *
     * @param repository the repository for managing lesson entities
     * @param mapper     the mapper for converting between lesson entities and DTOs
     */
    public LessonServiceImpl(JpaRepository<Lesson, Long> repository, Mapper<Lesson, LessonDto> mapper) {
        super(repository, mapper);
    }

}
