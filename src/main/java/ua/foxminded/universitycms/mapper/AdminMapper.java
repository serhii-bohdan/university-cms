package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.Name;
import ua.foxminded.universitycms.model.Role;

/**
 * Interface defining mappings between {@link Admin} entities and {@link AdminDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring")
public interface AdminMapper extends ua.foxminded.universitycms.mapper.Mapper<Admin, AdminDto> {

    /**
     * Converts an {@link Admin} entity to a corresponding {@link AdminDto} object.
     * This method performs the following mappings:
     * <ul>
     *   <li>Maps the first name from the entity's {@link Name} object to the `firstName` field in the DTO.</li>
     *   <li>Maps the last name from the entity's {@link Name} object to the `lastName` field in the DTO.</li>
     *   <li>Maps the `id` field from the entity's {@link Role} to the `roleId` field in the DTO.</li>
     * </ul>
     *
     * @param entity the {@link Admin} entity to be converted
     * @return a new {@link AdminDto} object representing the converted data
     */
    @Override
    @Mapping(source = "name.firstName", target = "firstName")
    @Mapping(source = "name.lastName", target = "lastName")
    @Mapping(source = "role.id", target = "roleId")
    AdminDto toDto(Admin entity);

    /**
     * Converts an {@link AdminDto} object to a corresponding {@link Admin} entity.
     * This method performs the following mappings:
     * <ul>
     *   <li>Maps the `firstName` field from the DTO to the first name within the entity's {@link Name} object.</li>
     *   <li>Maps the `lastName` field from the DTO to the last name within the entity's {@link Name} object.</li>
     *   <li>Maps the `roleId` field from the DTO to the associated role's ID in the entity.</li>
     * </ul>
     *
     * @param dto the {@link AdminDto} object to be converted
     * @return a new {@link Admin} entity representing the converted data
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "roleId", target = "role.id")
    Admin toEntity(AdminDto dto);

    /**
     * Performs a partial update of an existing {@link Admin} entity based on the data provided in the {@link AdminDto}.
     * <p>
     * This method intelligently merges the non-null properties from the DTO into the target entity, leaving any null
     * properties in the DTO untouched in the entity. It achieves this behavior using the
     * `NullValuePropertyMappingStrategy.IGNORE` strategy in the `@BeanMapping` annotation.
     *
     * @param dto   the {@link AdminDto} object containing the properties to update
     * @param entity the existing {@link Admin} entity to be updated
     * @return the updated {@link Admin} entity
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Admin partialUpdate(AdminDto dto, @MappingTarget Admin entity);

}
