package ua.foxminded.universitycms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for creating a teacher user in the university management system.
 * <p>
 * This class extends {@link EducatorCreationDto} to inherit common educator creation attributes, such as
 * personal details, email, password, and active status, without adding additional fields specific to
 * teachers at this time. It facilitates secure and efficient data transfer between application layers
 * during the creation of a teacher user, leveraging the validation constraints defined in the parent class.
 * Subclasses or future extensions may introduce teacher-specific fields or behaviors as needed.
 *
 * @author Serhii Bohdan
 * @see EducatorCreationDto
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class TeacherCreationDto extends EducatorCreationDto {
}
