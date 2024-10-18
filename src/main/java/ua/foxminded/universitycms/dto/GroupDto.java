package ua.foxminded.universitycms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.util.annotation.UniqueGroup;
import java.time.ZonedDateTime;
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
@UniqueGroup
public class GroupDto extends AbstractDto {

    /**
     * The name of the group.
     */
    @NotNull(message = "Group name is mandatory")
    @Pattern(regexp = "^[A-Z]{2}-\\d{2}$", message = "The group name must match the following template XX-00")
    private String groupName;

    /**
     * The date and time (including time zone) when the group record was created.
     */
    private ZonedDateTime createdAt;

    /**
     * The date and time (including time zone) when the group record was last updated.
     */
    private ZonedDateTime updatedAt;

    /**
     * A collection of {@link StudentDto} objects representing the students enrolled in the group.
     */
    private Set<StudentDto> students;

}
