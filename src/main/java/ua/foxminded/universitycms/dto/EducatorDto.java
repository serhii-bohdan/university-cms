package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Abstract Data Transfer Object (DTO) representing an educator entity in the university management system.
 * <p>
 * This class extends {@link UserDto} to inherit common user attributes and adds properties specific to
 * educators, such as their active status and schedule ID. It serves as a base for educator-related DTOs,
 * facilitating data transfer between application layers while enforcing validation constraints. The
 * {@link NotNull} annotation ensures that the active status is always provided.
 *
 * @author Serhii Bohdan
 * @see UserDto
 * @see jakarta.validation.constraints.NotNull
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public abstract class EducatorDto extends UserDto {

    /**
     * Indicates whether the educator's account is active.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and determines whether the
     * educator's account is currently operational (true) or inactive (false) within the system.
     */
    @NotNull(message = "Status is mandatory")
    private Boolean isActive;

    /**
     * The ID of the schedule associated with the educator.
     * <p>
     * This field represents the unique identifier of the schedule linked to the educator, referencing
     * their planned academic activities or timetable.
     */
    private Long scheduleId;

}
