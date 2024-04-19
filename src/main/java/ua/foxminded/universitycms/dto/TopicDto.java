package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
public class TopicDto extends AbstractDto {

    /**
     * The name of the topic.
     */
    @NotBlank(message = "Topic name is mandatory")
    @Size(max = 255)
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
    @Min(1)
    private Integer topicOrder;

    /**
     * The ID of the course that this topic belongs to.
     */
    @NotNull
    @Min(1)
    private Long courseId;

    /**
     * Constructs a new {@code TopicDto} instance with the specified topic details.
     *
     * @param topicName        the name of the topic
     * @param topicDescription the description of the topic
     * @param topicOrder       the order (position) of the topic within the course curriculum
     * @param courseId         the ID of the course that this topic belongs to
     */
    public TopicDto(String topicName, String topicDescription, Integer topicOrder, Long courseId) {
        this.topicName = topicName;
        this.topicDescription = topicDescription;
        this.topicOrder = topicOrder;
        this.courseId = courseId;
    }

}
