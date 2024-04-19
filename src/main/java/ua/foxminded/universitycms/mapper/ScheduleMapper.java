package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.Schedule;

/**
 * Interface defining mappings between {@link Schedule} entities and {@link ScheduleDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = StudyDayMapper.class)
public interface ScheduleMapper extends ua.foxminded.universitycms.mapper.Mapper<Schedule, ScheduleDto> {

    /**
     * Converts a {@link Schedule} entity to a corresponding {@link ScheduleDto} object. This method
     * performs the conversion of schedule data.
     *
     * @param entity the {@link Schedule} entity to be converted
     * @return a new {@link ScheduleDto} object representing the converted data (with or without lessons)
     */
    @Override
    ScheduleDto toDto(Schedule entity);

    /**
     * Converts a {@link ScheduleDto} object to a corresponding {@link Schedule} entity.
     *
     * @param dto the {@link ScheduleDto} object to be converted
     * @return a new {@link Schedule} entity representing the converted data
     */
    @Override
    Schedule toEntity(ScheduleDto dto);

}
