package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.util.Set;

/**
 * The {@code TeacherDto} class is a concrete DTO (Data Transfer Object) that
 * extends the {@link UserDto} class. It represents a teacher user entity in the
 * system and inherits all properties from {@link UserDto}. Additionally, it provides
 * information specific to teachers, such as associated courses.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "courses")
@SuperBuilder
public class TeacherDto extends UserDto {

    /**
     * A collection of {@link CourseDto} objects representing the courses taught by the teacher.
     */
    private Set<CourseDto> courses;

}
