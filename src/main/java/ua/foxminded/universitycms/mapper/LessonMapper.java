package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.model.StudyDay;

/**
 * Interface defining mappings between {@link Lesson} entities and {@link LessonDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = CourseMapper.class)
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

}
