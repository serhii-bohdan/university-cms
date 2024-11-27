package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Represents the Data Transfer Object (DTO) for creating a student.
 * <p>
 * This class extends {@link UserCreationDto} to inherit common user creation
 * fields and validation rules, while adding student-specific fields such as
 * group information.
 * <p>
 * This class is designed to represent the creation of student accounts
 * in a structured and validated manner, including their association with a group.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class StudentCreationDto extends UserCreationDto {

    /**
     * The unique identifier of the group to which the student belongs.
     */
    @NotNull(message = "Group is mandatory")
    private Long groupId;

    /**
     * The name of the group the student belongs to.
     */
    private String groupName;

}
