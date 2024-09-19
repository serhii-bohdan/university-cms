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
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * The {@code CourseDto} class is a concrete DTO (Data Transfer Object) that extends the {@link AbstractDto} class.
 * It represents a course entity in the system and provides information about educational courses.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "topics")
@SuperBuilder
public class CourseDto extends AbstractDto {

    /**
     * The name of the course.
     */
    @NotBlank(message = "Course name is mandatory")
    @Size(max = 255)
    private String courseName;

    /**
     * A description of the course content.
     */
    @NotBlank(message = "Description is mandatory")
    private String courseDescription;

    /**
     * The ID of the teacher who created.
     */
    @NotNull
    @Min(1)
    private Long authorId;

    /**
     * The first name of the teacher who authored the course.
     */
    private String authorFirstName;

    /**
     * The last name of the teacher who authored the course.
     */
    private String authorLastName;

    /**
     * The date and time (including time zone) when the course record was created.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time (including time zone) when the course record was last updated.
     */
    private ZonedDateTime updatedAt;

    /**
     * A collection of {@link TopicDto} objects representing the topics covered in the course.
     */
    private Set<TopicDto> topics;

}
