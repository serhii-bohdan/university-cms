package ua.foxminded.universitycms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Name;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Group;

/**
 * Interface defining mappings between {@link Student} entities and {@link StudentDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 * <p>
 * It performs comprehensive mappings between student entities and their corresponding DTOs,
 * including nested objects like {@link Name} and references to associated {@link Schedule} and {@link Group}.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring")
public interface StudentMapper extends ua.foxminded.universitycms.mapper.Mapper<Student, StudentDto> {

    /**
     * Converts a {@link Student} entity to a corresponding {@link StudentDto} object.
     * This method performs the following mappings:
     * <ul>
     *   <li>Maps the first name from the entity's {@link Name} object to the `firstName` field in the DTO.</li>
     *   <li>Maps the last name from the entity's {@link Name} object to the `lastName` field in the DTO.</li>
     *   <li>Maps the password hash from the entity to the `password` field in the DTO.</li>
     *   <li>Maps the ID of the associated {@link Schedule} to the `scheduleId` field in the DTO.</li>
     *   <li>Maps the ID of the associated {@link Group} to the `groupId` field in the DTO.</li>
     *   <li>Maps the group name from the associated {@link Group} to the `groupName` field in the DTO.</li>
     * </ul>
     * </p>
     *
     * @param entity the {@link Student} entity to be converted
     * @return a new {@link StudentDto} object representing the converted data
     */
    @Override
    @Mapping(source = "name.firstName", target = "firstName")
    @Mapping(source = "name.lastName", target = "lastName")
    @Mapping(source = "passwordHash", target = "password")
    @Mapping(source = "schedule.id", target = "scheduleId")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.groupName", target = "groupName")
    StudentDto toDto(Student entity);

    /**
     * Converts a {@link StudentDto} object to a corresponding {@link Student} entity.
     * This method performs the following mappings (inverse of `toDto`):
     * <ul>
     *   <li>Maps the `firstName` field from the DTO to the first name within the entity's {@link Name} object.</li>
     *   <li>Maps the `lastName` field from the DTO to the last name within the entity's {@link Name} object.</li>
     *   <li>Maps the `password` field from the DTO to the password hash in the entity.</li>
     *   <li>Maps the `scheduleId` field from the DTO to the ID of the associated {@link Schedule} in the entity.</li>
     *   <li>Maps the `groupId` field from the DTO to the ID of the associated {@link Group} in the entity.</li>
     *   <li>Maps the `groupName` field from the DTO to the group name within the associated {@link Group} in the entity.</li>
     * </ul>
     * </p>
     *
     * @param dto the {@link StudentDto} object to be converted
     * @return a new {@link Student} entity representing the converted data
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "groupId", target = "group.id")
    @Mapping(source = "groupName", target = "group.groupName")
    Student toEntity(StudentDto dto);

}
