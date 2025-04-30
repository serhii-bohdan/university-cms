package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) representing a mark (grade) assigned to a student for a specific topic
 * in the university management system.
 * <p>
 * This class extends {@link AbstractDto} to inherit a unique identifier and encapsulates details about
 * a student's mark, including its numerical value, optional comment, and associations with a student and
 * topic. It facilitates secure and efficient data transfer between application layers, with validation
 * constraints ensuring data integrity. The {@link NotNull} and {@link Min} annotations enforce required
 * fields and valid mark values.
 *
 * @author Serhii Bohdan
 * @see AbstractDto
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.validation.constraints.Min
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class MarkDto extends AbstractDto {

    /**
     * The numerical value of the mark assigned to the student.
     * <p>
     * This field is mandatory and must be greater than zero, as enforced by the {@link NotNull} and
     * {@link Min} validation constraints. It represents the student's performance score for the associated topic.
     */
    @NotNull(message = "Mark value is mandatory")
    @Min(value = 1, message = "Mark value must be greater than zero")
    private Integer markValue;

    /**
     * An optional comment providing feedback about the student's performance.
     * <p>
     * This field may contain additional context or notes from the teacher regarding the assigned mark.
     */
    private String comment;

    /**
     * The ID of the student who received the mark.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and identifies the student
     * associated with this mark.
     */
    @NotNull
    private Long studentId;

    /**
     * The ID of the topic for which the mark was assigned.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and identifies the topic
     * within a course to which this mark pertains.
     */
    @NotNull(message = "Topic is mandatory")
    private Long topicId;

    /**
     * The name of the topic for which the mark was assigned.
     * <p>
     * This field provides a human-readable name for the topic, complementing the {@code topicId}.
     */
    private String topicName;

}
