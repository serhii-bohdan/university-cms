package ua.foxminded.universitycms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.model.Group;

/**
 * Interface defining mappings between {@link Group} entities and {@link GroupDto} data transfer objects.
 * This mapper utilizes the MapStruct library for efficient and type-safe conversion.
 * <p>
 * It leverages the {@link StudentMapper} to handle nested student object conversions within the group.
 *
 * @author Serhii Bohdan
 */
@Mapper(componentModel = "spring", uses = StudentMapper.class)
public interface GroupMapper extends ua.foxminded.universitycms.mapper.Mapper<Group, GroupDto> {

    /**
     * Converts a {@link Group} entity to a corresponding {@link GroupDto} object. This
     * method utilizes the configured mappings to convert the group entity and its
     * potentially associated students.
     *
     * @param entity the {@link Group} entity to be converted
     * @return a new {@link GroupDto} object representing the converted data
     */
    @Override
    GroupDto toDto(Group entity);

    /**
     * Converts a {@link GroupDto} object to a corresponding {@link Group} entity. This method
     * utilizes the configured mappings to convert the group DTO and its potentially associated
     * student data.
     *
     * @param dto the {@link GroupDto} object to be converted
     * @return a new {@link Group} entity representing the converted data
     */
    @Override
    Group toEntity(GroupDto dto);

    /**
     * Partially updates a {@link Group} entity based on the provided {@link GroupDto}.
     * <p>
     * This method merges the properties of the `dto` object with the existing `entity` object,
     * updating only the non-null properties in the `dto`. This allows for selective updates without
     * overwriting existing data.
     *
     * @param dto    the {@link GroupDto} object containing the updated data
     * @param entity the {@link Group} entity to be partially updated
     * @return the partially updated {@link Group} entity
     */
    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(GroupDto dto, @MappingTarget Group entity);

}
