package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Abstract Data Transfer Object (DTO) for creating an educator user in the university management system.
 * <p>
 * This class extends {@link UserCreationDto} to inherit common user creation attributes and adds properties
 * specific to educators, such as their active status. It serves as a base for educator-specific creation DTOs,
 * facilitating secure and consistent data transfer between application layers during the creation process.
 * The {@link NotNull} annotation ensures that the active status is always specified.
 *
 * @author Serhii Bohdan
 * @see UserCreationDto
 * @see jakarta.validation.constraints.NotNull
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public abstract class EducatorCreationDto extends UserCreationDto {

    /**
     * Indicates whether the educator's account is active.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and determines whether the
     * educator's account is operational (true) or inactive (false) upon creation.
     */
    @NotNull(message = "Status is mandatory")
    private Boolean isActive;

}
