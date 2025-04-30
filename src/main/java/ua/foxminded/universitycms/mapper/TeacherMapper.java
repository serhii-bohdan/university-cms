package ua.foxminded.universitycms.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ua.foxminded.universitycms.dto.TeacherCreationDto;
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.security.PasswordEncoderMapper;
import ua.foxminded.universitycms.util.annotation.PasswordEncoderMapping;

/**
 * Mapper interface for converting between {@link Teacher} entities and {@link TeacherDto} or {@link TeacherCreationDto}
 * objects in the university management system.
 * <p>
 * This interface extends the generic {@link Mapper} contract to provide type-safe mappings specific to teacher users.
 * It leverages the MapStruct library for efficient conversions between persistence-layer entities and application-layer
 * DTOs, supporting bidirectional mapping, partial updates, bulk transformations, and teacher creation. The {@code @Mapper}
 * annotation integrates this interface with Spring and utilizes {@link CourseMapper} for nested course conversions and
 * {@link PasswordEncoderMapper} for secure password encoding during creation.
 *
 * @author Serhii Bohdan
 * @see Mapper
 * @see Teacher
 * @see TeacherDto
 * @see TeacherCreationDto
 * @see CourseMapper
 * @see PasswordEncoderMapper
 */
@Mapper(componentModel = "spring", uses = {CourseMapper.class, PasswordEncoderMapper.class})
public interface TeacherMapper extends ua.foxminded.universitycms.mapper.Mapper<Teacher, TeacherDto> {

    /**
     * Converts a {@link Teacher} entity to a {@link TeacherDto} object.
     * Maps the entity's fields to the DTO, including:
     * <ul>
     *   <li>{@code fullName.firstName} to {@code firstName}</li>
     *   <li>{@code fullName.lastName} to {@code lastName}</li>
     *   <li>{@code role.id} to {@code roleId}</li>
     *   <li>{@code role.roleName} to {@code roleName}</li>
     *   <li>{@code schedule.id} to {@code scheduleId}</li>
     * </ul>
     * Nested course data is processed by the {@link CourseMapper}.
     *
     * @param entity the {@link Teacher} entity to convert
     * @return the resulting {@link TeacherDto} object
     */
    @Override
    @Mapping(source = "fullName.firstName", target = "firstName")
    @Mapping(source = "fullName.lastName", target = "lastName")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "schedule.id", target = "scheduleId")
    TeacherDto toDto(Teacher entity);

    /**
     * Converts a {@link TeacherDto} object to a {@link Teacher} entity.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     *   <li>{@code scheduleId} to {@code schedule.id}</li>
     * </ul>
     * Nested course data is processed by the {@link CourseMapper}.
     *
     * @param dto the {@link TeacherDto} object to convert
     * @return the resulting {@link Teacher} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    Teacher toEntity(TeacherDto dto);

    /**
     * Partially updates an existing {@link Teacher} entity with data from a {@link TeacherDto} object.
     * <p>
     * Updates only the non-null fields from the DTO into the target entity, preserving existing entity data for null
     * DTO fields. The {@link BeanMapping} annotation with {@code NullValuePropertyMappingStrategy.IGNORE} ensures this
     * selective update behavior. Mappings include:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code roleId} to {@code role.id}</li>
     *   <li>{@code scheduleId} to {@code schedule.id}</li>
     * </ul>
     * Nested course updates are managed via the {@link CourseMapper}.
     *
     * @param dto    the {@link TeacherDto} object containing updated data
     * @param entity the existing {@link Teacher} entity to update
     * @return the updated {@link Teacher} entity
     */
    @Override
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Teacher partialUpdate(TeacherDto dto, @MappingTarget Teacher entity);

    /**
     * Converts a {@link TeacherCreationDto} object to a {@link Teacher} entity for user creation.
     * Maps the DTO's fields to the entity, including:
     * <ul>
     *   <li>{@code firstName} to {@code fullName.firstName}</li>
     *   <li>{@code lastName} to {@code fullName.lastName}</li>
     *   <li>{@code password} to {@code passwordHash}, encoded via {@link PasswordEncoderMapper}</li>
     * </ul>
     * The {@link PasswordEncoderMapping} annotation qualifies the password encoding process.
     *
     * @param dto the {@link TeacherCreationDto} object to convert
     * @return the resulting {@link Teacher} entity
     */
    @Mapping(source = "firstName", target = "fullName.firstName")
    @Mapping(source = "lastName", target = "fullName.lastName")
    @Mapping(source = "password", target = "passwordHash", qualifiedBy = {PasswordEncoderMapping.class})
    Teacher toEntity(TeacherCreationDto dto);

}
