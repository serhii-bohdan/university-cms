package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.Name;
import ua.foxminded.universitycms.model.Schedule;

/**
 * Interface defining mappings between {@link Teacher} entities and {@link TeacherDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 * <p>
 * It performs comprehensive mappings between teacher entities and their corresponding DTOs,
 * including nested objects like {@link Name} and references to an associated {@link Schedule}.
 * Additionally, it leverages the {@link CourseMapper} for handling potential course associations with teachers.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = CourseMapper.class)
public interface TeacherMapper extends ua.foxminded.universitycms.mapper.Mapper<Teacher, TeacherDto> {

    /**
     * Converts a {@link Teacher} entity to a corresponding {@link TeacherDto} object.
     * This method performs the following mappings:
     * <ul>
     *   <li>Maps the first name from the entity's {@link Name} object to the `firstName` field in the DTO.</li>
     *   <li>Maps the last name from the entity's {@link Name} object to the `lastName` field in the DTO.</li>
     *   <li>Maps the password hash from the entity to the `password` field in the DTO.</li>
     *   <li>Maps the ID of the associated {@link Schedule} to the `scheduleId` field in the DTO.</li>
     * </ul>
     * </p>
     *
     * @param entity the {@link Teacher} entity to be converted
     * @return a new {@link TeacherDto} object representing the converted data
     */
    @Override
    @Mapping(source = "name.firstName", target = "firstName")
    @Mapping(source = "name.lastName", target = "lastName")
    @Mapping(source = "passwordHash", target = "password")
    @Mapping(source = "schedule.id", target = "scheduleId")
    TeacherDto toDto(Teacher entity);

    /**
     * Converts a {@link TeacherDto} object to a corresponding {@link Teacher} entity.
     * This method performs the following mappings (inverse of `toDto`):
     * <ul>
     *   <li>Maps the `firstName` field from the DTO to the first name within the entity's {@link Name} object.</li>
     *   <li>Maps the `lastName` field from the DTO to the last name within the entity's {@link Name} object.</li>
     *   <li>Maps the `password` field from the DTO to the password hash in the entity.</li>
     *   <li>Maps the `scheduleId` field from the DTO to the ID of the associated {@link Schedule} in the entity.</li>
     * </ul>
     * </p>
     *
     * @param dto the {@link TeacherDto} object to be converted
     * @return a new {@link Teacher} entity representing the converted data
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(source = "scheduleId", target = "schedule.id")
    Teacher toEntity(TeacherDto dto);

}
