package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.model.Schedule;

/**
 * Mapper interface for converting between {@link Schedule} entities and {@link ScheduleDto} objects.
 * Extends {@link Mapper} to define type-safe mappings using MapStruct. Integrates with Spring via
 * {@code @Mapper} and uses {@link LessonMapper} for nested {@link Lesson} conversions within the
 * schedule. Supports bidirectional mapping and bulk transformations.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Schedule
 * @see ScheduleDto
 * @see LessonMapper
 */
@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface ScheduleMapper extends ua.foxminded.universitycms.mapper.Mapper<Schedule, ScheduleDto> {

    /**
     * Converts a {@link Schedule} entity to a {@link ScheduleDto} object.
     * Maps entity fields to the DTO, including nested {@link Lesson} objects processed by
     * {@link LessonMapper}. Ensures full transformation of the schedule into its DTO form.
     *
     * @param entity the {@link Schedule} entity to convert
     * @return the resulting {@link ScheduleDto} object
     */
    @Override
    ScheduleDto toDto(Schedule entity);

    /**
     * Converts a {@link ScheduleDto} object to a {@link Schedule} entity.
     * Maps DTO fields to the entity, including nested {@link Lesson} objects processed by
     * {@link LessonMapper}. Ensures full transformation of the DTO into its entity form.
     *
     * @param dto the {@link ScheduleDto} object to convert
     * @return the resulting {@link Schedule} entity
     */
    @Override
    Schedule toEntity(ScheduleDto dto);

}
