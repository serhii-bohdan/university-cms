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
 * The {@code TopicDto} class is a concrete DTO (Data Transfer Object) that extends the {@link AbstractDto} class.
 * It represents a topic entity within a course and provides information about the learning subject.
 *
 * @author Serhii Bohdan
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
     */
    @NotBlank(message = "Topic name is mandatory")
    @Size(max = 255, message = "Topic name cannot be longer than 255 characters")
    private String topicName;

    /**
     * A description of the topic content.
     */
    @NotBlank(message = "Description is mandatory")
    private String topicDescription;

    /**
     * The order (position) of the topic within the course curriculum.
     */
    @NotNull(message = "Topic order is mandatory")
    @Min(value = 1, message = "Topic order must be greater than zero")
    private Integer topicOrder;

    /**
     * The ID of the course that this topic belongs to.
     */
    @NotNull
    private Long courseId;

    /**
     * A collection of {@link MarkDto} objects representing the marks assigned to students for this topic.
     */
    private Set<MarkDto> marks;

}
