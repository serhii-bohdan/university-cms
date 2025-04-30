package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.AdminCreationDto;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.security.PasswordEncoderMapper;
import ua.foxminded.universitycms.util.annotation.PasswordEncoderMapping;

/**
 * Mapper interface for converting between {@link Admin} entities and {@link AdminDto} or {@link AdminCreationDto} objects.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to admin users in
 * the university management system. It leverages the MapStruct library for efficient conversion between persistence-layer
 * entities and application-layer DTOs, supporting bidirectional mapping, partial updates, and creation-specific transformations.
 * The {@code @Mapper} annotation integrates this interface with Spring and utilizes {@link PasswordEncoderMapper} for
 * password encoding.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Admin
 * @see AdminDto
 * @see AdminCreationDto
 * @see PasswordEncoderMapper
 */
@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface AdminMapper extends ua.foxminded.universitycms.mapper.Mapper<Admin, AdminDto> {

    /**
     * Converts an {@link Admin} entity to an {@link AdminDto} object.
     * Maps the entity's fields to the DTO, including:
     * <ul>
     *   <li>{@code fullName.firstName} to {@code firstName}</li>
     *   <li>{@code fullName.lastName} to {@code lastName}</li>
     *   <li>{@code role.id} to {@code roleId}</li>
     *   <li>{@code role.roleName} to {@code roleName}</li>
     * </ul>
     *
     * @param entity the {@link Admin} entity to convert
     * @return the resulting {@link AdminDto} object
     */
    @Override
    @Mapping(source = "fullName.firstName", target = "firstName")
    @Mapping(source = "fullName.lastName", target = "lastName")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    AdminDto toDto(Admin entity);

    /**
     * Converts an {@link AdminDto} object to an {@link Admin} entity.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     * </ul>
     *
     * @param dto the {@link AdminDto} object to convert
     * @return the resulting {@link Admin} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    Admin toEntity(AdminDto dto);

    /**
     * Partially updates an existing {@link Admin} entity with data from an {@link AdminDto} object.
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data
     * for null DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE}
     * ensures this behavior. Mappings include:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     * </ul>
     *
     * @param dto    the {@link AdminDto} object containing updated data
     * @param entity the existing {@link Admin} entity to update
     * @return the updated {@link Admin} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Admin partialUpdate(AdminDto dto, @MappingTarget Admin entity);

    /**
     * Converts an {@link AdminCreationDto} object to an {@link Admin} entity for user creation.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code password} to {@code passwordHash}, encoded via {@link PasswordEncoderMapper}</li>
     * </ul>
     * The {@link PasswordEncoderMapping} annotation qualifies the password encoding process.
     *
     * @param dto the {@link AdminCreationDto} object to convert
     * @return the resulting {@link Admin} entity
     */
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "password", target = "passwordHash", qualifiedBy = {PasswordEncoderMapping.class})
    Admin toEntity(AdminCreationDto dto);

}
