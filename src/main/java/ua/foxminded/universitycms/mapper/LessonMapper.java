package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.model.StudyDay;
import ua.foxminded.universitycms.model.Course;

/**
 * Interface defining mappings between {@link Lesson} entities and {@link LessonDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface LessonMapper extends ua.foxminded.universitycms.mapper.Mapper<Lesson, LessonDto> {

    /**
     * Converts a {@link Lesson} entity to a corresponding {@link LessonDto} object.
     * This method performs the following mapping:
     * <ul>
     *   <li>Maps the ID of the associated {@link StudyDay} to the `studyDayId` field in the DTO.</li>
     * </ul>
     *
     * @param entity the {@link Lesson} entity to be converted
     * @return a new {@link LessonDto} object representing the converted data
     */
    @Override
    @Mapping(source = "studyDay.id", target = "studyDayId")
    LessonDto toDto(Lesson entity);

    /**
     * Converts a {@link LessonDto} object to a corresponding {@link Lesson} entity.
     * This method performs the following mapping (inverse of `toDto`):
     * <ul>
     *   <li>Maps the `studyDayId` field from the DTO to the ID of the associated {@link StudyDay} in the entity.</li>
     * </ul>
     *
     * @param dto the {@link LessonDto} object to be converted
     * @return a new {@link Lesson} entity representing the converted data
     */
    @Override
    @Mapping(source = "studyDayId", target = "studyDay.id")
    Lesson toEntity(LessonDto dto);

    /**
     * Partially updates a {@link Lesson} entity using non-null properties from a {@link LessonDto}.
     * <p>
     * This method allows for selective updates, where only the non-null fields in the {@link LessonDto}
     * are applied to the target {@link Lesson} entity. Fields in the DTO that are {@code null} will not override
     * the corresponding fields in the entity, preserving their original values.
     * <p>
     * The method specifically maps:
     * <ul>
     *   <li>The {@code studyDayId} field in the DTO to the {@code id} of the associated {@link StudyDay} in the entity.</li>
     *   <li>The {@code course} field in the DTO to the {@link Course} entity using the {@code courseDtoToCourseEntity} mapping.</li>
     * </ul>
     *
     * @param dto    the {@link LessonDto} containing data for the update
     * @param entity the target {@link Lesson} entity to be updated
     * @return the updated {@link Lesson} entity with applied changes
     */
    @Mapping(source = "studyDayId", target = "studyDay.id")
    @Mapping(source = "course", target = "course", qualifiedByName = "courseDtoToCourseEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Lesson partialUpdate(LessonDto dto, @MappingTarget Lesson entity);

    /**
     * Converts a {@link CourseDto} to a {@link Course} entity for use in mapping operations.
     * <p>
     * This method extracts the {@code id} field from the {@link CourseDto} and maps it to a new {@link Course} entity.
     * Other fields in the {@link Course} entity remain unset.
     *
     * @param dto the {@link CourseDto} to be converted
     * @return a {@link Course} entity containing only the {@code id} field from the {@link CourseDto}
     */
    @Named("courseDtoToCourseEntity")
    static Course courseDtoToCourseEntity(CourseDto dto) {
        return Course.builder()
            .id(dto.getId())
            .build();
    }

}
