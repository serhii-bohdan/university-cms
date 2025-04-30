package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) representing a student user in the university management system.
 * <p>
 * This class extends {@link EducatorDto} to inherit common educator attributes and encapsulates
 * additional properties specific to a student, such as their group affiliation. It facilitates secure
 * and efficient data transfer between application layers, with the {@link NotNull} constraint ensuring
 * that the group ID is always provided.
 *
 * @author Serhii Bohdan
 * @see EducatorDto
 * @see jakarta.validation.constraints.NotNull
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class StudentDto extends EducatorDto {

    /**
     * The unique identifier of the group to which the student belongs.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and represents the ID of
     * the student's academic group within the system.
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
