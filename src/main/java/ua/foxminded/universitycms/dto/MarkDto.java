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
    @Min(1)
    private Integer markValue;

    /**
     * An optional comment or feedback provided by the teacher regarding the student's performance.
     */
    private String comment;

    /**
     * The ID of the student who received the mark.
     */
    @NotNull
    @Min(1)
    private Long studentId;

    /**
     * A {@link TopicDto} object representing the topic for which the mark was given.
     */
    @NotNull
    private TopicDto topic;

}
