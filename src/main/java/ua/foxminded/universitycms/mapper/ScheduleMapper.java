package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.StudyDay;

/**
 * Interface defining mappings between {@link Schedule} entities and {@link ScheduleDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 * <p>
 * It performs comprehensive mappings between schedule entities and their corresponding DTOs,
 * including nested objects like {@link StudyDay}. The mapper facilitates two-way conversion to ensure
 * consistency between entity and DTO layers.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = StudyDayMapper.class)
public interface ScheduleMapper extends ua.foxminded.universitycms.mapper.Mapper<Schedule, ScheduleDto> {

    /**
     * Converts a {@link Schedule} entity to a corresponding {@link ScheduleDto} object.
     * This method performs the conversion of schedule data, including the mapping of nested
     * objects and references to associated entities.
     *
     * @param entity the {@link Schedule} entity to be converted
     * @return a new {@link ScheduleDto} object representing the converted data
     */
    @Override
    ScheduleDto toDto(Schedule entity);

    /**
     * Converts a {@link ScheduleDto} object to a corresponding {@link Schedule} entity.
     * This method ensures that the DTO's data is properly mapped back to the entity,
     * enabling persistence or further operations in the entity layer.
     *
     * @param dto the {@link ScheduleDto} object to be converted
     * @return a new {@link Schedule} entity representing the converted data
     */
    @Override
    Schedule toEntity(ScheduleDto dto);

}
