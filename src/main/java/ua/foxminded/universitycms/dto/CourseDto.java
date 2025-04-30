package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.UniqueTeacherCourse;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a course entity in the university management system.
 * <p>
 * This class extends {@link AbstractDto} to inherit a unique identifier and encapsulates detailed
 * information about an educational course, including its name, description, author, timestamps,
 * and associated topics and students. It facilitates secure and efficient data transfer between
 * application layers, with validation constraints ensuring data integrity. The {@link UniqueTeacherCourse}
 * annotation enforces uniqueness of the course for a given teacher, while other annotations like
 * {@link NotBlank}, {@link NotNull}, and {@link Size} validate key fields.
 *
 * @author Serhii Bohdan
 * @see AbstractDto
 * @see UniqueTeacherCourse
 * @see TopicDto
 * @see StudentDto
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.validation.constraints.Size
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"topics"})
@SuperBuilder
@UniqueTeacherCourse
public class CourseDto extends AbstractDto {

    /**
     * The name of the course.
     * <p>
     * This field is mandatory and must not exceed 255 characters, as enforced by the {@link NotBlank}
     * and {@link Size} validation constraints.
     */
    @NotBlank(message = "Course name is mandatory")
    @Size(max = 255, message = "Course name cannot be longer than 255 characters")
    private String courseName;

    /**
     * A description of the course content and objectives.
     * <p>
     * This field is mandatory, as enforced by the {@link NotBlank} validation constraint, and provides
     * details about the course's purpose and scope.
     */
    @NotBlank(message = "Description is mandatory")
    private String courseDescription;

    /**
     * The ID of the teacher who authored the course.
     * <p>
     * This field is mandatory, as enforced by the {@link NotNull} validation constraint, and identifies
     * the teacher responsible for creating and managing the course.
     */
    @NotNull
    private Long authorId;

    /**
     * The first name of the teacher who authored the course.
     * <p>
     * This field provides a human-readable component of the author's name, complementing the
     * {@code authorId}.
     */
    private String authorFirstName;

    /**
     * The last name of the teacher who authored the course.
     * <p>
     * This field provides a human-readable component of the author's name, complementing the
     * {@code authorId}.
     */
    private String authorLastName;

    /**
     * The date and time when the course record was created, including time zone information.
     * <p>
     * This field tracks the creation timestamp of the course entity in the system.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time when the course record was last updated, including time zone information.
     * <p>
     * This field tracks the most recent update timestamp of the course entity in the system.
     */
    private ZonedDateTime updatedAt;

    /**
     * The set of topics covered in the course.
     * <p>
     * This field contains a collection of {@link TopicDto} objects, representing the educational topics
     * associated with the course.
     */
    private Set<TopicDto> topics;

    /**
     * The set of students enrolled in the course.
     * <p>
     * This field contains a collection of {@link StudentDto} objects, representing the students
     * currently enrolled in the course.
     */
    private Set<StudentDto> students;

}
