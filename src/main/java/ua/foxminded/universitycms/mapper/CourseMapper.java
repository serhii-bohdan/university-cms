package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Teacher;

/**
 * Interface defining mappings between {@link Course} entities and {@link CourseDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion. It specifically handles
 * course entity conversions, including:
 * <ul>
 *   <li>Mapping relationships with {@link Teacher} entities (using teacher ID).</li>
 *   <li>Leveraging {@link TopicMapper} for potential topic associations with courses.</li>
 * </ul>
 * </p>
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = TopicMapper.class)
public interface CourseMapper extends ua.foxminded.universitycms.mapper.Mapper<Course, CourseDto> {

    /**
     * Converts a {@link Course} entity to a corresponding {@link CourseDto} object.
     * This method performs the following mapping:
     * <ul>
     *   <li>Maps the ID of the associated {@link Teacher} to the `authorId` field in the DTO.</li>
     * </ul>
     * Additional mappings for topics and potentially ordering them can be configured based on your specific requirements.
     *
     * @param entity the {@link Course} entity to be converted
     * @return a new {@link CourseDto} object representing the converted data
     */
    @Override
    @Mapping(source = "author.id", target = "authorId")
    CourseDto toDto(Course entity);

    /**
     * Converts a {@link CourseDto} object to a corresponding {@link Course} entity.
     * This method performs the following mapping (inverse of `toDto`):
     * <ul>
     *   <li>Maps the `authorId` field from the DTO to the ID of the associated {@link Teacher} in the entity.</li>
     * </ul>
     * Additional mappings for topics and their ordering can be configured based on your specific requirements.
     *
     * @param dto the {@link CourseDto} object to be converted
     * @return a new {@link Course} entity representing the converted data
     */
    @Override
    @Mapping(source = "authorId", target = "author.id")
    Course toEntity(CourseDto dto);

}
