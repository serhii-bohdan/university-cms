package ua.foxminded.universitycms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.model.Group;

/**
 * Mapper interface for converting between {@link Group} entities and {@link GroupDto} objects in the university
 * management system.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to group entities.
 * It leverages the MapStruct library for efficient conversions between persistence-layer entities and application-layer
 * DTOs, supporting bidirectional mapping, partial updates, and bulk transformations. The {@code @Mapper} annotation
 * integrates this interface with Spring and utilizes {@link StudentMapper} to handle nested student object conversions
 * within the group.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Group
 * @see GroupDto
 * @see StudentMapper
 */
@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface GroupMapper extends ua.foxminded.universitycms.mapper.Mapper<Group, GroupDto> {

    /**
     * Converts a {@link Group} entity to a {@link GroupDto} object.
     * <p>
     * Maps the entity's fields to the DTO, including any nested student data, which is handled by the
     * {@link StudentMapper}. This method ensures a complete transformation of the group entity into its DTO
     * representation.
     *
     * @param entity the {@link Group} entity to convert
     * @return the resulting {@link GroupDto} object
     */
    @Override
    GroupDto toDto(Group entity);

    /**
     * Converts a {@link GroupDto} object to a {@link Group} entity.
     * <p>
     * Maps the DTO's fields to the entity, including any nested student data, which is handled by the
     * {@link StudentMapper}. This method ensures a complete transformation of the DTO into its entity representation.
     *
     * @param dto the {@link GroupDto} object to convert
     * @return the resulting {@link Group} entity
     */
    @Override
    Group toEntity(GroupDto dto);

    /**
     * Partially updates an existing {@link Group} entity with data from a {@link GroupDto} object.
     * <p>
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data for
     * null DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE}
     * ensures this selective update behavior. Nested student data is managed via the {@link StudentMapper}.
     *
     * @param dto    the {@link GroupDto} object containing updated data
     * @param entity the existing {@link Group} entity to update
     * @return the updated {@link Group} entity
     */
    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(GroupDto dto, @MappingTarget Group entity);

}
