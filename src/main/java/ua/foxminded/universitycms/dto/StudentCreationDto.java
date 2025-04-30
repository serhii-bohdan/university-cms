package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for creating a student user in the university management system.
 * <p>
 * This class extends {@link EducatorCreationDto} to inherit common educator creation attributes, such as
 * personal details, email, password, and active status, and adds properties specific to a student, including
 * their group affiliation. It facilitates secure and efficient data transfer between application layers
 * during the creation of a student user, with the {@link NotNull} constraint ensuring the group ID is always
 * provided.
 *
 * @author Serhii Bohdan
 * @see EducatorCreationDto
 * @see jakarta.validation.constraints.NotNull
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class StudentCreationDto extends EducatorCreationDto {

    /**
     * The unique identifier of the group to which the student belongs.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and represents the ID of the
     * student's academic group within the system.
     */
    @NotNull(message = "Group is mandatory")
    private Long groupId;

    /**
     * The name of the group to which the student belongs.
     * <p>
     * This field provides a human-readable name for the student's academic group, complementing the
     * {@code groupId}.
     */
    private String groupName;

}
