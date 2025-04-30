package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.ManagerCreationDto;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.security.PasswordEncoderMapper;
import ua.foxminded.universitycms.util.annotation.PasswordEncoderMapping;

/**
 * Mapper interface for converting between {@link Manager} entities and {@link ManagerDto} or {@link ManagerCreationDto}
 * objects.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to manager users
 * in the university management system. It leverages the MapStruct library for efficient conversions between
 * persistence-layer entities and application-layer DTOs, supporting bidirectional mapping, partial updates, and
 * creation-specific transformations. The {@code @Mapper} annotation integrates this interface with Spring and utilizes
 * {@link PasswordEncoderMapper} for password encoding.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Manager
 * @see ManagerDto
 * @see ManagerCreationDto
 * @see PasswordEncoderMapper
 */
@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface ManagerMapper extends ua.foxminded.universitycms.mapper.Mapper<Manager, ManagerDto> {

    /**
     * Converts a {@link Manager} entity to a {@link ManagerDto} object.
     * Maps the entity's fields to the DTO, including:
     * <ul>
     *   <li>{@code fullName.firstName} to {@code firstName}</li>
     *   <li>{@code fullName.lastName} to {@code lastName}</li>
     *   <li>{@code role.id} to {@code roleId}</li>
     *   <li>{@code role.roleName} to {@code roleName}</li>
     * </ul>
     *
     * @param entity the {@link Manager} entity to convert
     * @return the resulting {@link ManagerDto} object
     */
    @Override
    @Mapping(source = "fullName.firstName", target = "firstName")
    @Mapping(source = "fullName.lastName", target = "lastName")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    ManagerDto toDto(Manager entity);

    /**
     * Converts a {@link ManagerDto} object to a {@link Manager} entity.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     * </ul>
     *
     * @param dto the {@link ManagerDto} object to convert
     * @return the resulting {@link Manager} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    Manager toEntity(ManagerDto dto);

    /**
     * Partially updates an existing {@link Manager} entity with data from a {@link ManagerDto} object.
     * <p>
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data for
     * null DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE}
     * ensures this selective update behavior. Mappings include:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     * </ul>
     *
     * @param dto    the {@link ManagerDto} object containing updated data
     * @param entity the existing {@link Manager} entity to update
     * @return the updated {@link Manager} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Manager partialUpdate(ManagerDto dto, @MappingTarget Manager entity);

    /**
     * Converts a {@link ManagerCreationDto} object to a {@link Manager} entity for user creation.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code password} to {@code passwordHash}, encoded via {@link PasswordEncoderMapper}</li>
     * </ul>
     * The {@link PasswordEncoderMapping} annotation qualifies the password encoding process.
     *
     * @param dto the {@link ManagerCreationDto} object to convert
     * @return the resulting {@link Manager} entity
     */
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "password", target = "passwordHash", qualifiedBy = {PasswordEncoderMapping.class})
    Manager toEntity(ManagerCreationDto dto);

}
