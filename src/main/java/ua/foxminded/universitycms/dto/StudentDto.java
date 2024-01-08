package ua.foxminded.universitycms.dto;

import java.util.Set;

/**
 * The {@code StudentDto} class is a data transfer object (DTO) for student
 * entities.
 * <p>
 * This class extends {@link UserDto} and inherits all its fields and methods.
 * It represents a specific type of user, namely a student. It includes
 * additional fields for the group name and a set of marks.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the student DTO.
 *
 * @author Serhii Bohdan
 */
public class StudentDto extends UserDto {

    private String groupName;
    private Set<MarkDto> marks;

    /**
     * Constructs a new {@code StudentDto} with the specified first name, last name,
     * email, password, active status, and group name.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param email     the email of the student
     * @param password  the password of the student
     * @param isActive  the active status of the student
     * @param groupName the group name of the student
     */
    public StudentDto(String firstName, String lastName, String email, String password, Boolean isActive,
            String groupName) {
        super(firstName, lastName, email, password, isActive);
        this.groupName = groupName;
    }

    /**
     * Constructs a new {@code StudentDto} with no initial values.
     */
    public StudentDto() {
        super();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String group) {
        this.groupName = group;
    }

    public Set<MarkDto> getMarks() {
        return marks;
    }

    public void setMarks(Set<MarkDto> marks) {
        this.marks = marks;
    }

    /**
     * Returns a string representation of the student DTO.
     *
     * @return a string representation of the student DTO
     */
    @Override
    public String toString() {
        return "StudentDto [userId=" + super.getUserId() + ", firstName=" + super.getFirstName() + ", lastName="
                + super.getLastName() + ", email=" + super.getEmail() + ", password=" + super.getPassword()
                + ", isActive=" + super.getIsActive() + ", groupName=" + groupName + ", marks=" + marks + "]";
    }

}
