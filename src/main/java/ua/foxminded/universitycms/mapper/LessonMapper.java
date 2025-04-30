package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Lesson;

/**
 * Mapper interface for converting between {@link Lesson} entities and {@link LessonDto} objects.
 * Extends {@link Mapper} to define type-safe mappings using MapStruct. Integrates with Spring via
 * {@code @Mapper} and supports bidirectional conversion and partial updates between lesson entities
 * and DTOs.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Lesson
 * @see LessonDto
 */
@Mapper(componentModel = "spring")
public interface LessonMapper extends ua.foxminded.universitycms.mapper.Mapper<Lesson, LessonDto> {

    /**
     * Converts a {@link Lesson} entity to a {@link LessonDto} object.
     * Maps entity fields to DTO, including:
     * <ul>
     *   <li>{@code course.id} to {@code courseId}</li>
     *   <li>{@code course.courseName} to {@code courseName}</li>
     *   <li>{@code schedule.id} to {@code scheduleId}</li>
     * </ul>
     *
     * @param entity the {@link Lesson} entity to convert
     * @return the resulting {@link LessonDto} object
     */
    @Override
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.courseName", target = "courseName")
    @Mapping(source = "schedule.id", target = "scheduleId")
    LessonDto toDto(Lesson entity);

    /**
     * Converts a {@link LessonDto} object to a {@link Lesson} entity.
     * Maps DTO fields to entity, including:
     * <ul>
     *   <li>{@code courseId} to {@code course.id}</li>
     *   <li>{@code courseName} to {@code course.courseName}</li>
     *   <li>{@code scheduleId} to {@code schedule.id}</li>
     * </ul>
     *
     * @param dto the {@link LessonDto} object to convert
     * @return the resulting {@link Lesson} entity
     */
    @Override
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "courseName", target = "course.courseName")
    @Mapping(source = "scheduleId", target = "schedule.id")
    Lesson toEntity(LessonDto dto);

    /**
     * Updates an existing {@link Lesson} entity with non-null fields from a {@link LessonDto}.
     * <p>
     * Performs a partial update by mapping non-null fields from the DTO to the entity, retaining existing
     * entity data for null DTO fields via {@link NullValuePropertyMappingStrategy#IGNORE}. The following mappings
     * are applied:
     * <ul>
     *   <li>{@code courseId} to {@code course}, using the {@link #buildCourseEntityWithId} method</li>
     *   <li>{@code scheduleId} to {@code schedule.id}</li>
     * </ul>
     *
     * @param dto    the {@link LessonDto} containing the updated data
     * @param entity the {@link Lesson} entity to update
     * @return the updated {@link Lesson} entity
     */
    @Mapping(source = "courseId", target = "course", qualifiedByName = "buildCourseEntityWithId")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Lesson partialUpdate(LessonDto dto, @MappingTarget Lesson entity);

    /**
     * Constructs a {@link Course} entity with the specified ID for mapping purposes.
     * <p>
     * This utility method creates a minimal {@link Course} object with only the ID field set, designed for
     * use in mappings such as {@link #partialUpdate}. It provides a lightweight course reference when full course
     * data is not required.
     *
     * @param courseId the ID to assign to the {@link Course} entity
     * @return a new {@link Course} instance with the specified ID
     */
    @Named("buildCourseEntityWithId")
    static Course buildCourseEntityWithId(Long courseId) {
        return Course.builder()
            .id(courseId)
            .build();
    }

}
