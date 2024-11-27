package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The {@code MarkDto} class is a concrete DTO (Data Transfer Object) that extends the {@link AbstractDto} class.
 * It represents a mark (grade) assigned to a student for a specific topic within a course.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class MarkDto extends AbstractDto {

    /**
     * The numerical value of the mark assigned to the student.
     */
    @NotNull(message = "Mark value is mandatory")
    @Min(value = 1, message = "Mark value must be greater than zero")
    private Integer markValue;

    /**
     * An optional comment or feedback provided by the teacher regarding the student's performance.
     */
    private String comment;

    /**
     * The ID of the student who received the mark.
     */
    @NotNull
    private Long studentId;

    /**
     * The ID of the topic for which the mark was assigned.
     */
    @NotNull(message = "Topic is mandatory")
    private Long topicId;

    /**
     * The name of the topic for which the mark was assigned.
     */
    private String topicName;

}
