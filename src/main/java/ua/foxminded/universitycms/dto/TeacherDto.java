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

    /**
     * Constructs a new {@code TeacherDto} instance with the specified teacher details
     * including the schedule ID.
     *
     * @param firstName  the first name of the teacher
     * @param lastName   the last name of the teacher
     * @param email      the email address of the teacher
     * @param password   the password of the teacher
     * @param isActive   indicates whether the teacher account is active
     * @param scheduleId the ID of the schedule associated with the teacher
     */
    public TeacherDto(String firstName, String lastName, String email, String password, Boolean isActive,
                      Long scheduleId) {
        super(firstName, lastName, email, password, isActive, scheduleId);
    }

    /**
     * Constructs a new {@code TeacherDto} instance with the specified teacher details,
     * omitting the schedule ID.
     *
     * @param firstName the first name of the teacher
     * @param lastName  the last name of the teacher
     * @param email     the email address of the teacher
     * @param password  the password of the teacher
     * @param isActive  indicates whether the teacher account is active
     */
    public TeacherDto(String firstName, String lastName, String email, String password, Boolean isActive) {
        super(firstName, lastName, email, password, isActive);
    }

}
