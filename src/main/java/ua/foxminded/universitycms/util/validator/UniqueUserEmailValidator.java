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
 * Validator for ensuring the uniqueness of a user's email across different user roles.
 * <p>
 * This class implements the {@link ConstraintValidator} interface for the {@link UniqueUserEmail} annotation.
 * It validates whether a user's email is unique across the system, considering multiple user roles such as
 * Admin, Manager, Teacher, and Student.
 * <p>
 * The validation logic checks whether a user with the provided email already exists in any of the repositories.
 * If the user exists, it compares the user ID and role ID to ensure that the current user's email is not duplicated
 * for the same user ID and role.
 * <p>
 * If the email is not unique, a validation error is triggered, and the provided error message is returned.
 *
 * @author Serhii Bohdan
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
     * Initializes the validator. This method is used to initialize any necessary resources or configurations.
     *
     * @param constraintAnnotation the {@link UniqueUserEmail} annotation that is being validated
     */
    @Override
    public void initialize(UniqueUserEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the email address for uniqueness across multiple user roles.
     * <p>
     * This method checks if the provided email already exists in the system for any user role. If the email
     * is found, it ensures that the existing user's ID and role ID match the current user's ID and role ID,
     * allowing updates without triggering the uniqueness constraint. If any conflict is found, the validation
     * fails and returns false.
     * <p>
     * If the value is an instance of {@link UserDto}, it performs a validation for an existing user update.
     * If the value is an instance of {@link UserCreationDto}, it checks if the email is unique for a new user.
     *
     * @param value   the {@link Object} that contains the email to validate, either a {@link UserDto} or {@link UserCreationDto}
     * @param context the {@link ConstraintValidatorContext} used to build the constraint violation message
     * @return {@code true} if the email is unique for the given user, {@code false} otherwise
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
