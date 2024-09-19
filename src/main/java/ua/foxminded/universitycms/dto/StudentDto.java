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

}
