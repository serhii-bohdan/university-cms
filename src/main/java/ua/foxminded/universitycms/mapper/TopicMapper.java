package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.model.Course;

/**
 * Interface defining mappings between {@link Topic} entities and {@link TopicDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion. It specifically handles
 * topic entity conversions, including mapping relationships with associated {@link Course} entities.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = {MarkMapper.class})
public interface TopicMapper extends ua.foxminded.universitycms.mapper.Mapper<Topic, TopicDto> {

    /**
     * Converts a {@link Topic} entity to a corresponding {@link TopicDto} object.
     * This method performs the following mapping:
     * <ul>
     *   <li>Maps the ID of the associated {@link Course} to the `courseId` field in the DTO.</li>
     * </ul>
     *
     * @param entity the {@link Topic} entity to be converted
     * @return a new {@link TopicDto} object representing the converted data
     */
    @Override
    @Mapping(source = "course.id", target = "courseId")
    TopicDto toDto(Topic entity);

    /**
     * Converts a {@link TopicDto} object to a corresponding {@link Topic} entity.
     * This method performs the following mapping (inverse of `toDto`):
     * <ul>
     *   <li>Maps the `courseId` field from the DTO to the ID of the associated {@link Course} in the entity.</li>
     * </ul>
     *
     * @param dto the {@link TopicDto} object to be converted
     * @return a new {@link Topic} entity representing the converted data
     */
    @Override
    @Mapping(source = "courseId", target = "course.id")
    Topic toEntity(TopicDto dto);

    /**
     * Partially updates an existing {@link Topic} entity with the data from a {@link TopicDto} object.
     * Only the fields in the DTO that are not null will be updated in the entity. This method utilizes the
     * `@BeanMapping` annotation with the `NullValuePropertyMappingStrategy.IGNORE` strategy to ensure that
     * null values in the DTO are not used to overwrite existing values in the entity.
     *
     * @param dto    the {@link TopicDto} object containing the updated data (may contain null values)
     * @param entity the existing {@link Topic} entity to be partially updated
     * @return the updated {@link Topic} entity
     */
    @Override
    @Mapping(source = "courseId", target = "course.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Topic partialUpdate(TopicDto dto, @MappingTarget Topic entity);

}
