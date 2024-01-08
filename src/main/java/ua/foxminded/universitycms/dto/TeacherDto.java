package ua.foxminded.universitycms.dto;

import java.util.Set;

/**
 * The {@code TeacherDto} class is a data transfer object (DTO) for teacher
 * entities.
 * <p>
 * This class extends {@link UserDto} and inherits all its fields and methods.
 * It represents a specific type of user, namely a teacher. It includes an
 * additional field for a set of courses.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the teacher DTO.
 *
 * @author Serhii Bohdan
 */
public class TeacherDto extends UserDto {

    private Set<CourseDto> courses;

    /**
     * Constructs a new {@code TeacherDto} with the specified first name, last name,
     * email, password, and active status.
     *
     * @param firstName the first name of the teacher
     * @param lastName  the last name of the teacher
     * @param email     the email of the teacher
     * @param password  the password of the teacher
     * @param isActive  the active status of the teacher
     */
    public TeacherDto(String firstName, String lastName, String email, String password, Boolean isActive) {
        super(firstName, lastName, email, password, isActive);
    }

    /**
     * Constructs a new {@code TeacherDto} with no initial values.
     */
    public TeacherDto() {
        super();
    }

    public Set<CourseDto> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseDto> courses) {
        this.courses = courses;
    }

    /**
     * Returns a string representation of the teacher DTO.
     *
     * @return a string representation of the teacher DTO
     */
    @Override
    public String toString() {
        return "TeacherDto [userId=" + super.getUserId() + ", firstName=" + super.getFirstName() + ", lastName="
                + super.getLastName() + ", email=" + super.getEmail() + ", password=" + super.getPassword()
                + ", isActive=" + super.getIsActive() + ", courses=" + courses + "]";
    }

}
