package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.Name;
import ua.foxminded.universitycms.model.Role;

/**
 * Interface defining mappings between {@link Manager} entities and {@link ManagerDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring")
public interface ManagerMapper extends ua.foxminded.universitycms.mapper.Mapper<Manager, ManagerDto> {

    /**
     * Converts a {@link Manager} entity to a corresponding {@link ManagerDto} object.
     * This method performs the following mappings:
     * <ul>
     *   <li>Maps the first name from the entity's {@link Name} object to the `firstName` field in the DTO.</li>
     *   <li>Maps the last name from the entity's {@link Name} object to the `lastName` field in the DTO.</li>
     *   <li>Maps the `id` field from the entity's {@link Role} to the `roleId` field in the DTO.</li>
     * </ul>
     * </p>
     *
     * @param entity the {@link Manager} entity to be converted
     * @return a new {@link ManagerDto} object representing the converted data
     */
    @Override
    @Mapping(source = "name.firstName", target = "firstName")
    @Mapping(source = "name.lastName", target = "lastName")
    @Mapping(source = "role.id", target = "roleId")
    ManagerDto toDto(Manager entity);

    /**
     * Converts a {@link ManagerDto} object to a corresponding {@link Manager} entity.
     * This method performs the following mappings:
     * <ul>
     *   <li>Maps the `firstName` field from the DTO to the first name within the entity's {@link Name} object.</li>
     *   <li>Maps the `lastName` field from the DTO to the last name within the entity's {@link Name} object.</li>
     *   <li>Maps the `roleId` field from the DTO to the associated role's ID in the entity.</li>
     * </ul>
     * </p>
     *
     * @param dto the {@link ManagerDto} object to be converted
     * @return a new {@link Manager} entity representing the converted data
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "roleId", target = "role.id")
    Manager toEntity(ManagerDto dto);

    /**
     * Performs a partial update of an existing {@link Manager} entity using data from a {@link ManagerDto}.
     * <p>
     * This method selectively updates the `Manager` entity with non-null values from the provided DTO.
     * It utilizes MapStruct's `NullValuePropertyMappingStrategy.IGNORE` to prevent null values in the DTO from
     * overwriting existing values in the entity.
     *
     * @param dto    The {@link ManagerDto} containing the data to update (null values are ignored).
     * @param entity The existing {@link Manager} entity to be updated.
     * @return The updated `Manager` entity.
     */
    @Override
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Manager partialUpdate(ManagerDto dto, @MappingTarget Manager entity);

}
