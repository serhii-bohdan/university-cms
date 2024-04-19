package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The {@code StudentDto} class is a concrete DTO (Data Transfer Object) that
 * extends the {@link UserDto} class. It represents a student user entity in
 * the system and inherits all properties from {@link UserDto}. Additionally, it
 * provides information specific to students, such as group name, and group ID.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class StudentDto extends UserDto {

    /**
     * The unique identifier of the group to which the student belongs.
     */
    private Long groupId;

    /**
     * The name of the group the student belongs to.
     */
    @NotNull(message = "Group is mandatory")
    @Pattern(regexp = "^[A-Z]{2}-[0-9]{2}$")
    private String groupName;

    /**
     * Constructs a new {@code StudentDto} instance with the specified student details
     * including group name and schedule ID.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param email      the email address of the student
     * @param password   the password of the student
     * @param isActive   indicates whether the student account is active
     * @param groupName  the name of the group the student belongs to
     * @param scheduleId the ID of the schedule associated with the student
     */
    public StudentDto(String firstName, String lastName, String email, String password, Boolean isActive,
                      String groupName, Long scheduleId) {
        super(firstName, lastName, email, password, isActive, scheduleId);
        this.groupName = groupName;
    }

    /**
     * Constructs a new {@code StudentDto} instance with the specified student details,
     * omitting the schedule ID.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param email     the email address of the student
     * @param password  the password of the student
     * @param isActive  indicates whether the student account is active
     * @param groupName the name of the group the student belongs to
     */
    public StudentDto(String firstName, String lastName, String email, String password, Boolean isActive,
                      String groupName) {
        super(firstName, lastName, email, password, isActive);
        this.groupName = groupName;
    }

}
