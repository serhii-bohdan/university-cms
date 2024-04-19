package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.model.StudyDay;
import ua.foxminded.universitycms.model.Schedule;

/**
 * Interface defining mappings between {@link StudyDay} entities and {@link StudyDayDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface StudyDayMapper extends ua.foxminded.universitycms.mapper.Mapper<StudyDay, StudyDayDto> {

    /**
     * Converts a {@link StudyDay} entity to a corresponding {@link StudyDayDto} object.
     * This method performs the following mapping:
     * <ul>
     *   <li>Maps the ID of the associated {@link Schedule} to the `scheduleId` field in the DTO.</li>
     * </ul>
     * Additional mappings for lessons and potentially ordering them can be configured based on your
     * specific requirements.
     *
     * @param entity the {@link StudyDay} entity to be converted
     * @return a new {@link StudyDayDto} object representing the converted data
     */
    @Override
    @Mapping(source = "schedule.id", target = "scheduleId")
    StudyDayDto toDto(StudyDay entity);

    /**
     * Converts a {@link StudyDayDto} object to a corresponding {@link StudyDay} entity.
     * This method performs the following mapping (inverse of `toDto`):
     * <ul>
     *   <li>Maps the `scheduleId` field from the DTO to the ID of the associated {@link Schedule} in the entity.</li>
     * </ul>
     * Additional mappings for lessons and their ordering can be configured based on your specific requirements.
     *
     * @param dto the {@link StudyDayDto} object to be converted
     * @return a new {@link StudyDay} entity representing the converted data
     */
    @Override
    @Mapping(source = "scheduleId", target = "schedule.id")
    StudyDay toEntity(StudyDayDto dto);

}
