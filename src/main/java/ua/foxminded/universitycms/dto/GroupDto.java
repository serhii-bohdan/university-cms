package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * The {@code GroupDto} class is a concrete DTO (Data Transfer Object) that extends the
 * {@link AbstractDto} class. It represents a group entity in the system and provides
 * information about student groups.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "students")
@SuperBuilder
public class GroupDto extends AbstractDto {

    /**
     * The name of the group.
     */
    @NotNull(message = "Group name is mandatory")
    @Pattern(regexp = "^[A-Z]{2}-[0-9]{2}$")
    private String groupName;

    /**
     * The date and time the group was created.
     */
    private LocalDateTime createdAt;

    /**
     * The date and time the group information was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * A collection of {@link StudentDto} objects representing the students enrolled in the group.
     */
    private Set<StudentDto> students;

    /**
     * Constructs a new {@code GroupDto} instance with the specified group name.
     *
     * @param groupName the name of the group
     */
    public GroupDto(String groupName) {
        this.groupName = groupName;
    }

}
