package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import java.util.Collection;
import java.util.List;
import org.mapstruct.*;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.model.Course;

/**
 * Mapper interface for converting between {@link Course} entities and {@link CourseDto} objects in the university
 * management system.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to courses.
 * It leverages the MapStruct library for efficient conversions between persistence-layer entities and application-layer
 * DTOs, supporting bidirectional mapping, partial updates, and bulk transformations. The {@code @Mapper} annotation
 * integrates this interface with Spring and utilizes {@link TopicMapper} and {@link StudentMapper} for handling related
 * entities (topics and students).
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Course
 * @see CourseDto
 * @see TopicMapper
 * @see StudentMapper
 */
@Mapper(componentModel = "spring", uses = {TopicMapper.class, StudentMapper.class})
public interface CourseMapper extends ua.foxminded.universitycms.mapper.Mapper<Course, CourseDto> {

    /**
     * Converts a {@link Course} entity to a {@link CourseDto} object.
     * Maps the entity's fields to the DTO, including:
     * <ul>
     *   <li>{@code author.id} to {@code authorId}</li>
     *   <li>{@code author.fullName.firstName} to {@code authorFirstName}</li>
     *   <li>{@code author.fullName.lastName} to {@code authorLastName}</li>
     * </ul>
     * Additional mappings for related entities (e.g., topics and students) are handled by the used mappers
     * ({@link TopicMapper}, {@link StudentMapper}).
     *
     * @param entity the {@link Course} entity to convert
     * @return the resulting {@link CourseDto} object
     */
    @Override
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.fullName.firstName", target = "authorFirstName")
    @Mapping(source = "author.fullName.lastName", target = "authorLastName")
    CourseDto toDto(Course entity);

    /**
     * Converts a {@link CourseDto} object to a {@link Course} entity.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code authorId} to {@code author.id}</li>
     * </ul>
     * Additional mappings for related entities (e.g., topics and students) are handled by the used mappers
     * ({@link TopicMapper}, {@link StudentMapper}).
     *
     * @param dto the {@link CourseDto} object to convert
     * @return the resulting {@link Course} entity
     */
    @Override
    @Mapping(source = "authorId", target = "author.id")
    Course toEntity(CourseDto dto);

    /**
     * Partially updates an existing {@link Course} entity with data from a {@link CourseDto} object.
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data for
     * null DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE}
     * ensures this behavior. Mappings include:
     * <ul>
     *   <li>{@code authorId} to {@code author.id}</li>
     * </ul>
     * Related entities (e.g., topics and students) are updated via the used mappers if applicable.
     *
     * @param dto    the {@link CourseDto} object containing updated data
     * @param entity the existing {@link Course} entity to update
     * @return the updated {@link Course} entity
     */
    @Override
    @Mapping(source = "authorId", target = "author.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Course partialUpdate(CourseDto dto, @MappingTarget Course entity);

    /**
     * Converts a collection of {@link Course} entities to a list of {@link CourseDto} objects.
     * Performs bulk mapping by applying the {@link #toDto(Course)} method to each entity in the collection.
     *
     * @param entities the collection of {@link Course} entities to convert
     * @return a {@link List} of {@link CourseDto} objects
     */
    @Override
    List<CourseDto> toDtoList(Collection<Course> entities);

}
