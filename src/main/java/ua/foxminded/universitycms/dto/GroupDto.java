package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.UniqueGroupName;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a student group entity in the university management system.
 * <p>
 * This class extends {@link AbstractDto} to inherit a unique identifier and encapsulates information
 * about a student group, including its name, creation and update timestamps, and associated students.
 * It facilitates secure and efficient data transfer between application layers, with validation
 * constraints ensuring data integrity. The {@link UniqueGroupName} annotation enforces uniqueness of
 * the group name, while {@link NotNull} and {@link Pattern} validate the format and presence of the
 * group name.
 *
 * @author Serhii Bohdan
 * @see AbstractDto
 * @see StudentDto
 * @see UniqueGroupName
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.validation.constraints.Pattern
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"students"})
@SuperBuilder
@UniqueGroupName
public class GroupDto extends AbstractDto {

    /**
     * The name of the group.
     * <p>
     * This field is mandatory and must follow the pattern "XX-00" (e.g., "CS-01"), where "XX" are two
     * uppercase letters and "00" are two digits, as enforced by the {@link NotNull} and {@link Pattern}
     * validation constraints. It uniquely identifies the group within the system due to the
     * {@link UniqueGroupName} constraint.
     */
    @NotNull(message = "Group name is mandatory")
    @Pattern(regexp = "^[A-Z]{2}-\\d{2}$", message = "The group name must match the following template XX-00")
    private String groupName;

    /**
     * The date and time when the group record was created, including time zone information.
     * <p>
     * This field tracks the creation timestamp of the group entity in the system.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time when the group record was last updated, including time zone information.
     * <p>
     * This field tracks the most recent update timestamp of the group entity in the system.
     */
    private ZonedDateTime updatedAt;

    /**
     * The set of students enrolled in the group.
     * <p>
     * This field contains a collection of {@link StudentDto} objects, representing the students assigned
     * to this group.
     */
    private Set<StudentDto> students;

}
