package ua.foxminded.universitycms.util.validator;

import java.util.Optional;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.UserCreationDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.AdminRepository;
import ua.foxminded.universitycms.repository.ManagerRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.util.annotation.UniqueUserEmail;

/**
 * Validator for ensuring the uniqueness of a user's email across all user roles in the university management system.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to enforce the {@link UniqueUserEmail} annotation.
 * It checks whether an email provided in a {@link UserDto} or {@link UserCreationDto} object is unique across all user
 * roles (Admin, Manager, Teacher, Student) by querying the respective repositories. For updates, it allows the email to
 * remain the same for the existing user while ensuring no other user has it. The {@code @Component} annotation registers
 * this class as a Spring-managed bean, and {@code @RequiredArgsConstructor} ensures dependency injection of the
 * repositories.
 *
 * @author Serhii Bohdan
 * @see ConstraintValidator
 * @see UniqueUserEmail
 * @see UserDto
 * @see UserCreationDto
 * @see AdminRepository
 * @see ManagerRepository
 * @see TeacherRepository
 * @see StudentRepository
 */
@Component
@RequiredArgsConstructor
public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, Object> {

    /**
     * Repository for accessing admin user data.
     */
    private final AdminRepository adminRepository;

    /**
     * Repository for accessing manager user data.
     */
    private final ManagerRepository managerRepository;

    /**
     * Repository for accessing teacher user data.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Repository for accessing student user data.
     */
    private final StudentRepository studentRepository;

    /**
     * Initializes the validator with the {@link UniqueUserEmail} annotation.
     * <p>
     * This method is invoked once during validator instantiation to perform any necessary setup based on the
     * annotation's configuration. Currently, it delegates to the default implementation without additional logic.
     *
     * @param constraintAnnotation the {@link UniqueUserEmail} annotation instance being validated
     */
    @Override
    public void initialize(UniqueUserEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the uniqueness of a user's email across all user roles.
     * <p>
     * For a {@link UserCreationDto}, it checks that the email does not exist in any user repository. For a
     * {@link UserDto}, it ensures the email is unique except for the current user (matching ID and role). Returns
     * {@code true} if the email is unique or validly retained, and {@code false} otherwise, updating the validation
     * context with an error message if necessary.
     *
     * @param value   the object to validate, either a {@link UserDto} or {@link UserCreationDto}
     * @param context the {@link ConstraintValidatorContext} for reporting validation errors
     * @return {@code true} if the email is unique or valid for the user; {@code false} if it conflicts with another user
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value instanceof UserDto userDto) {
            return validateEmailForUpdate(userDto);
        } else if (value instanceof UserCreationDto userCreationDto) {
            return validateEmailForSave(userCreationDto.getEmail());
        }

        return false;
    }

    private boolean validateEmailForSave(String email) {
        return studentRepository.findByEmail(email).isEmpty() &&
            teacherRepository.findByEmail(email).isEmpty() &&
            managerRepository.findByEmail(email).isEmpty() &&
            adminRepository.findByEmail(email).isEmpty();
    }

    private boolean validateEmailForUpdate(UserDto userDto) {
        Long userId = userDto.getId();
        String email = userDto.getEmail();
        Long roleId = userDto.getRoleId();

        Optional<Student> student = studentRepository.findByEmail(email);
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        Optional<Manager> manager = managerRepository.findByEmail(email);
        Optional<Admin> admin = adminRepository.findByEmail(email);

        return student.map(s -> (s.getId().equals(userId) && s.getRole().getId().equals(roleId))).orElse(true) &&
            teacher.map(t -> (t.getId().equals(userId) && t.getRole().getId().equals(roleId))).orElse(true) &&
            manager.map(m -> (m.getId().equals(userId) && m.getRole().getId().equals(roleId))).orElse(true) &&
            admin.map(a -> (a.getId().equals(userId) && a.getRole().getId().equals(roleId))).orElse(true);
    }

}
