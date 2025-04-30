package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.model.Topic;

/**
 * Mapper interface for converting between {@link Topic} entities and {@link TopicDto} objects in the university
 * management system.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to topic entities.
 * It leverages the MapStruct library for efficient conversions between persistence-layer entities and application-layer
 * DTOs, supporting bidirectional mapping, partial updates, and bulk transformations. The {@code @Mapper} annotation
 * integrates this interface with Spring and utilizes {@link MarkMapper} to handle nested mark object conversions within
 * the topic.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Topic
 * @see TopicDto
 * @see MarkMapper
 */
@Mapper(componentModel = "spring", uses = {MarkMapper.class})
public interface TopicMapper extends ua.foxminded.universitycms.mapper.Mapper<Topic, TopicDto> {

    /**
     * Converts a {@link Topic} entity to a {@link TopicDto} object.
     * Maps the entity's fields to the DTO, including:
     * <ul>
     *   <li>{@code course.id} to {@code courseId}</li>
     * </ul>
     * Nested mark data is processed by the {@link MarkMapper}.
     *
     * @param entity the {@link Topic} entity to convert
     * @return the resulting {@link TopicDto} object
     */
    @Override
    @Mapping(source = "course.id", target = "courseId")
    TopicDto toDto(Topic entity);

    /**
     * Converts a {@link TopicDto} object to a {@link Topic} entity.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code courseId} to {@code course.id}</li>
     * </ul>
     * Nested mark data is processed by the {@link MarkMapper}.
     *
     * @param dto the {@link TopicDto} object to convert
     * @return the resulting {@link Topic} entity
     */
    @Override
    @Mapping(source = "courseId", target = "course.id")
    Topic toEntity(TopicDto dto);

    /**
     * Partially updates an existing {@link Topic} entity with data from a {@link TopicDto} object.
     * <p>
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data for null
     * DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE} ensures this
     * selective update behavior. Mappings include:
     * <ul>
     *   <li>{@code courseId} to {@code course.id}</li>
     * </ul>
     * Nested mark updates are managed via the {@link MarkMapper}.
     *
     * @param dto    the {@link TopicDto} object containing updated data
     * @param entity the existing {@link Topic} entity to update
     * @return the updated {@link Topic} entity
     */
    @Override
    @Mapping(source = "courseId", target = "course.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Topic partialUpdate(TopicDto dto, @MappingTarget Topic entity);

}
