package ua.foxminded.universitycms.dto;

import java.util.Set;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.UniqueCourseTopic;

/**
 * Data Transfer Object (DTO) representing a topic entity within a course in the university management system.
 * <p>
 * This class extends {@link AbstractDto} to inherit a unique identifier and encapsulates details about a
 * learning topic, including its name, description, order within the course, parent course ID, and associated
 * student marks. It facilitates secure and efficient data transfer between application layers, with validation
 * constraints ensuring data integrity. The {@link UniqueCourseTopic} annotation enforces uniqueness of the topic
 * within its course, while {@link NotBlank}, {@link NotNull}, {@link Size}, and {@link Min} annotations validate
 * key fields.
 *
 * @author Serhii Bohdan
 * @see AbstractDto
 * @see MarkDto
 * @see UniqueCourseTopic
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.validation.constraints.Size
 * @see jakarta.validation.constraints.Min
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@UniqueCourseTopic
public class TopicDto extends AbstractDto {

    /**
     * The name of the topic.
     * <p>
     * This field is mandatory and must not exceed 255 characters, as enforced by the {@link NotBlank} and
     * {@link Size} validation constraints. It identifies the topic within the course.
     */
    @NotBlank(message = "Topic name is mandatory")
    @Size(max = 255, message = "Topic name cannot be longer than 255 characters")
    private String topicName;

    /**
     * A description of the topic's content and objectives.
     * <p>
     * This field is mandatory, as enforced by the {@link NotBlank} constraint, and provides details about
     * the topic's scope and purpose within the course.
     */
    @NotBlank(message = "Description is mandatory")
    private String topicDescription;

    /**
     * The order of the topic within the course curriculum.
     * <p>
     * This field is mandatory and must be greater than zero, as enforced by the {@link NotNull} and
     * {@link Min} constraints. It specifies the topic's position in the sequence of the course's content.
     */
    @NotNull(message = "Topic order is mandatory")
    @Min(value = 1, message = "Topic order must be greater than zero")
    private Integer topicOrder;

    /**
     * The ID of the course to which this topic belongs.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} constraint, and identifies the parent
     * course containing this topic.
     */
    @NotNull
    private Long courseId;

    /**
     * The set of marks assigned to students for this topic.
     * <p>
     * This field contains a collection of {@link MarkDto} objects, representing the grades or evaluations
     * given to students for their performance on this topic.
     */
    private Set<MarkDto> marks;

}
