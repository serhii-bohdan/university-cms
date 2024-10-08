package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.model.Mark;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Topic;

/**
 * Interface defining mappings between {@link Mark} entities and {@link MarkDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 * <p>
 * It specifically handles mark entity conversions, including mapping relationships with associated
 * {@link Student} and {@link Topic} entities.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring")
public interface MarkMapper extends ua.foxminded.universitycms.mapper.Mapper<Mark, MarkDto> {

    /**
     * Converts a {@link Mark} entity to a corresponding {@link MarkDto} object.
     * This method performs the following mapping:
     * <ul>
     *   <li>Maps the ID of the associated {@link Student} to the `studentId` field in the DTO.</li>
     * </ul>
     *
     * @param entity the {@link Mark} entity to be converted
     * @return a new {@link MarkDto} object representing the converted data
     */
    @Override
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "topic.topicName", target = "topicName")
    MarkDto toDto(Mark entity);

    /**
     * Converts a {@link MarkDto} object to a corresponding {@link Mark} entity.
     * This method performs the following mapping (inverse of `toDto`):
     * <ul>
     *   <li>Maps the `studentId` field from the DTO to the ID of the associated {@link Student} in the entity.</li>
     * </ul>
     *
     * @param dto the {@link MarkDto} object to be converted
     * @return a new {@link Mark} entity representing the converted data
     */
    @Override
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "topicId", target = "topic.id")
    Mark toEntity(MarkDto dto);

    /**
     * Partially updates an existing {@link Mark} entity with the data from a {@link MarkDto} object.
     * Only the fields in the DTO that are not null will be updated in the entity.
     * This method utilizes the `@BeanMapping` annotation with the `NullValuePropertyMappingStrategy.IGNORE` strategy
     * to ensure that null values in the DTO are not used to overwrite existing values in the entity.
     *
     * @param dto    the {@link MarkDto} object containing the updated data (may contain null values)
     * @param entity the existing {@link Mark} entity to be partially updated
     * @return the updated {@link Mark} entity
     */
    @Override
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "topicId", target = "topic.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Mark partialUpdate(MarkDto dto, @MappingTarget Mark entity);

}
