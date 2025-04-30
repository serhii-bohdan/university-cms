package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a teacher user in the university management system.
 * <p>
 * This class extends {@link EducatorDto} to inherit common educator attributes and encapsulates
 * additional information specific to a teacher, including the set of courses they teach. It facilitates
 * secure and efficient data transfer between application layers, representing a teacher entity with
 * its associated {@link CourseDto} objects.
 *
 * @author Serhii Bohdan
 * @see EducatorDto
 * @see CourseDto
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"courses"})
@SuperBuilder
public class TeacherDto extends EducatorDto {

    /**
     * The set of courses taught by the teacher.
     * <p>
     * This field contains a collection of {@link CourseDto} objects, representing the courses
     * associated with the teacher in the system.
     */
    private Set<CourseDto> courses;

}
