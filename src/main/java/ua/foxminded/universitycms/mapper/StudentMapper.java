package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.*;
import ua.foxminded.universitycms.security.PasswordEncoderMapper;
import ua.foxminded.universitycms.util.annotation.PasswordEncoderMapping;

/**
 * Interface defining mappings between {@link Student} entities and {@link StudentDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 * <p>
 * It performs comprehensive mappings between student entities and their corresponding DTOs,
 * including nested objects like {@link Name} and references to associated {@link Schedule} and {@link Group}.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface StudentMapper extends ua.foxminded.universitycms.mapper.Mapper<Student, StudentDto> {

    /**
     * Converts a {@link Student} entity to a corresponding {@link StudentDto} object.
     * This method performs the following mappings:
     * <ul>
     *   <li>Maps the first name from the entity's {@link Name} object to the `firstName` field in the DTO.</li>
     *   <li>Maps the last name from the entity's {@link Name} object to the `lastName` field in the DTO.</li>
     *   <li>Maps the ID of the associated {@link Schedule} to the `scheduleId` field in the DTO.</li>
     *   <li>Maps the ID of the associated {@link Group} to the `groupId` field in the DTO.</li>
     *   <li>Maps the group name from the associated {@link Group} to the `groupName` field in the DTO.</li>
     *   <li>Maps the `id` field from the entity's {@link Role} to the `roleId` field in the DTO.</li>
     * </ul>
     * </p>
     *
     * @param entity the {@link Student} entity to be converted
     * @return a new {@link StudentDto} object representing the converted data
     */
    @Override
    @Mapping(source = "name.firstName", target = "firstName")
    @Mapping(source = "name.lastName", target = "lastName")
    @Mapping(source = "role.id", target = "roleId")
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
     *   <li>Maps the `scheduleId` field from the DTO to the ID of the associated {@link Schedule} in the entity.</li>
     *   <li>Maps the `groupId` field from the DTO to the ID of the associated {@link Group} in the entity.</li>
     *   <li>Maps the `groupName` field from the DTO to the group name within the associated {@link Group} in the entity.</li>
     *   <li>Maps the `roleId` field from the DTO to the ID of the associated {@link Role} in the entity.
     * </ul>
     * </p>
     *
     * @param dto the {@link StudentDto} object to be converted
     * @return a new {@link Student} entity representing the converted data
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "groupId", target = "group.id")
    @Mapping(source = "groupName", target = "group.groupName")
    Student toEntity(StudentDto dto);

    /**
     * Performs a partial update of an existing {@link Student} entity using data from a {@link StudentDto}.
     * <p>
     * This method selectively updates the `Student` entity with non-null values from the provided DTO.
     * It utilizes MapStruct's `NullValuePropertyMappingStrategy.IGNORE` to prevent null values in the DTO from
     * overwriting existing values in the entity.
     *
     * @param dto    The {@link StudentDto} containing the data to update (null values are ignored).
     * @param entity The existing {@link Student} entity to be updated.
     * @return The updated `Student` entity.
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(expression = "java(new Group(dto.getGroupId()))", target = "group")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Student partialUpdate(StudentDto dto, @MappingTarget Student entity);

    /**
     * Converts a {@link StudentCreationDto} to a {@link Student} entity.
     * <p>
     * This method maps fields from the {@link StudentCreationDto} to corresponding fields in the {@link Student} entity.
     * It also encodes the password using the {@link PasswordEncoderMapper}.
     *
     * <p>Key Mappings:
     * <ul>
     *   <li>Maps first and last names to the {@link Name} object in the entity.</li>
     *   <li>Encodes the password and sets it in the entity's password hash field.</li>
     *   <li>Maps the group ID to the associated {@link Group} entity.</li>
     * </ul>
     *
     * @param dto the {@link StudentCreationDto} containing the data for creating a new student
     * @return a {@link Student} entity with the mapped and encoded values
     */
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "password", target = "passwordHash", qualifiedBy = {PasswordEncoderMapping.class})
    @Mapping(source = "groupId", target = "group.id")
    Student toEntity(StudentCreationDto dto);

}
