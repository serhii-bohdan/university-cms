package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Represents the Data Transfer Object (DTO) for creating a teacher.
 * <p>
 * This class extends {@link UserCreationDto} to inherit common user creation
 * fields and validation rules, while allowing for additional teacher-specific
 * fields or behaviors in the future.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class TeacherCreationDto extends UserCreationDto {
}
