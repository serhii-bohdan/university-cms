package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.StudentCreationDto;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.model.*;
import ua.foxminded.universitycms.security.PasswordEncoderMapper;
import ua.foxminded.universitycms.util.annotation.PasswordEncoderMapping;

/**
 * Mapper interface for converting between {@link Student} entities and {@link StudentDto} or {@link StudentCreationDto}
 * objects in the university management system.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to student users.
 * It leverages the MapStruct library for efficient conversions between persistence-layer entities and application-layer
 * DTOs, supporting bidirectional mapping, partial updates, bulk transformations, and student creation. The {@code @Mapper}
 * annotation integrates this interface with Spring and utilizes {@link PasswordEncoderMapper} for secure password
 * encoding during creation.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Student
 * @see StudentDto
 * @see StudentCreationDto
 * @see PasswordEncoderMapper
 */
@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface StudentMapper extends ua.foxminded.universitycms.mapper.Mapper<Student, StudentDto> {

    /**
     * Converts a {@link Student} entity to a {@link StudentDto} object.
     * Maps the entity's fields to the DTO, including:
     * <ul>
     *   <li>{@code fullName.firstName} to {@code firstName}</li>
     *   <li>{@code fullName.lastName} to {@code lastName}</li>
     *   <li>{@code role.id} to {@code roleId}</li>
     *   <li>{@code role.roleName} to {@code roleName}</li>
     *   <li>{@code schedule.id} to {@code scheduleId}</li>
     *   <li>{@code group.id} to {@code groupId}</li>
     *   <li>{@code group.groupName} to {@code groupName}</li>
     * </ul>
     *
     * @param entity the {@link Student} entity to convert
     * @return the resulting {@link StudentDto} object
     */
    @Override
    @Mapping(source = "fullName.firstName", target = "firstName")
    @Mapping(source = "fullName.lastName", target = "lastName")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "schedule.id", target = "scheduleId")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.groupName", target = "groupName")
    @Mapping(source = "role.roleName", target = "roleName")
    StudentDto toDto(Student entity);

    /**
     * Converts a {@link StudentDto} object to a {@link Student} entity.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     *   <li>{@code scheduleId} to {@code schedule.id}</li>
     *   <li>{@code groupId} to {@code group.id}</li>
     *   <li>{@code groupName} to {@code group.groupName}</li>
     * </ul>
     *
     * @param dto the {@link StudentDto} object to convert
     * @return the resulting {@link Student} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "groupId", target = "group.id")
    @Mapping(source = "groupName", target = "group.groupName")
    Student toEntity(StudentDto dto);

    /**
     * Partially updates an existing {@link Student} entity with data from a {@link StudentDto} object.
     * <p>
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data for null
     * DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE} ensures this
     * selective update behavior. Mappings include:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     *   <li>{@code scheduleId} to {@code schedule.id}</li>
     *   <li>{@code groupId} to {@code group}, using the {@code buildGroupEntityWithId} method</li>
     * </ul>
     *
     * @param dto    the {@link StudentDto} object containing updated data
     * @param entity the existing {@link Student} entity to update
     * @return the updated {@link Student} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "groupId", target = "group", qualifiedByName = "buildGroupEntityWithId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Student partialUpdate(StudentDto dto, @MappingTarget Student entity);

    /**
     * Helper method to create a minimal {@link Group} entity with the specified ID for partial updates.
     * <p>
     * This method, marked with {@code @Named("buildGroupEntityWithId")}, constructs a {@link Group} entity containing
     * only the {@code id} field, used during partial updates to reference the group without fully reconstructing it.
     *
     * @param groupId the ID of the group to set in the entity
     * @return a {@link Group} entity with only the {@code id} field set
     */
    @Named("buildGroupEntityWithId")
    static Group buildGroupEntityWithId(Long groupId) {
        return Group.builder()
            .id(groupId)
            .build();
    }

    /**
     * Converts a {@link StudentCreationDto} object to a {@link Student} entity for user creation.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code password} to {@code passwordHash}, encoded via {@link PasswordEncoderMapper}</li>
     *   <li>{@code groupId} to {@code group.id}</li>
     * </ul>
     * The {@link PasswordEncoderMapping} annotation qualifies the password encoding process.
     *
     * @param dto the {@link StudentCreationDto} object to convert
     * @return the resulting {@link Student} entity
     */
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "password", target = "passwordHash", qualifiedBy = {PasswordEncoderMapping.class})
    @Mapping(source = "groupId", target = "group.id")
    Student toEntity(StudentCreationDto dto);

}
