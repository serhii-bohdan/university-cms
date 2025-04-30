package ua.foxminded.universitycms.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.model.Mark;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Topic;

/**
 * Mapper interface for converting between {@link Mark} entities and {@link MarkDto} objects in the university
 * management system.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to mark entities.
 * It leverages the MapStruct library for efficient conversions between persistence-layer entities and application-layer
 * DTOs, supporting bidirectional mapping, partial updates, and bulk transformations. The {@code @Mapper} annotation
 * integrates this interface with Spring, enabling mappings for relationships with {@link Student} and {@link Topic}
 * entities.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Mark
 * @see MarkDto
 * @see Student
 * @see Topic
 */
@Mapper(componentModel = "spring")
public interface MarkMapper extends ua.foxminded.universitycms.mapper.Mapper<Mark, MarkDto> {

    /**
     * Converts a {@link Mark} entity to a {@link MarkDto} object.
     * Maps the entity's fields to the DTO, including:
     * <ul>
     *   <li>{@code student.id} to {@code studentId}</li>
     *   <li>{@code topic.id} to {@code topicId}</li>
     *   <li>{@code topic.topicName} to {@code topicName}</li>
     * </ul>
     *
     * @param entity the {@link Mark} entity to convert
     * @return the resulting {@link MarkDto} object
     */
    @Override
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "topic.topicName", target = "topicName")
    MarkDto toDto(Mark entity);

    /**
     * Converts a {@link MarkDto} object to a {@link Mark} entity.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code studentId} to {@code student.id}</li>
     *   <li>{@code topicId} to {@code topic.id}</li>
     * </ul>
     *
     * @param dto the {@link MarkDto} object to convert
     * @return the resulting {@link Mark} entity
     */
    @Override
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "topicId", target = "topic.id")
    Mark toEntity(MarkDto dto);

    /**
     * Partially updates an existing {@link Mark} entity with data from a {@link MarkDto} object.
     * <p>
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data for
     * null DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE}
     * ensures this selective update behavior. Mappings include:
     * <ul>
     *   <li>{@code studentId} to {@code student.id}</li>
     *   <li>{@code topicId} to {@code topic.id}</li>
     * </ul>
     *
     * @param dto    the {@link MarkDto} object containing updated data
     * @param entity the existing {@link Mark} entity to update
     * @return the updated {@link Mark} entity
     */
    @Override
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "topicId", target = "topic.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Mark partialUpdate(MarkDto dto, @MappingTarget Mark entity);

    /**
     * Converts a collection of {@link Mark} entities to a list of {@link MarkDto} objects.
     * Performs bulk mapping by applying the {@link #toDto(Mark)} method to each entity in the provided collection.
     *
     * @param entities the collection of {@link Mark} entities to convert
     * @return a {@link List} of {@link MarkDto} objects
     */
    @Override
    List<MarkDto> toDtoList(Collection<Mark> entities);

}
