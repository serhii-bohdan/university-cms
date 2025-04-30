package ua.foxminded.universitycms.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.AdminRepository;
import ua.foxminded.universitycms.repository.ManagerRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.util.annotation.ValidPasswordUpdateRequest;

/**
 * Validator for ensuring the integrity of password update requests in the university management system.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to enforce the {@link ValidPasswordUpdateRequest}
 * annotation. It validates a {@link PasswordUpdateRequestDto} object by checking that the current password is correct,
 * the new password matches its confirmation, and the new password differs from the current one. It uses repositories
 * ({@link AdminRepository}, {@link ManagerRepository}, {@link StudentRepository}, {@link TeacherRepository}) to fetch
 * user data and {@link PasswordEncoder} to verify the current password. The {@code @Component} annotation registers
 * this class as a Spring-managed bean, and {@code @RequiredArgsConstructor} ensures dependency injection of the
 * required dependencies.
 *
 * @author Serhii Bohdan
 * @see ConstraintValidator
 * @see ValidPasswordUpdateRequest
 * @see PasswordUpdateRequestDto
 * @see AdminRepository
 * @see ManagerRepository
 * @see StudentRepository
 * @see TeacherRepository
 * @see PasswordEncoder
 * @see UserNotFoundException
 */
@Component
@RequiredArgsConstructor
public class PasswordUpdateRequestValidator implements ConstraintValidator<ValidPasswordUpdateRequest, PasswordUpdateRequestDto> {

    /**
     * Error message template used when a student with the specified ID cannot be found in the system.
     */
    private static final String STUDENT_NOT_FOUND_MESSAGE = "Student not found with id: %s.";

    /**
     * Error message template used when a teacher with the specified ID cannot be found in the system.
     */
    private static final String TEACHER_NOT_FOUND_MESSAGE = "Teacher not found with id: %s.";

    /**
     * Error message template used when a manager with the specified ID cannot be found in the system.
     */
    private static final String MANAGER_NOT_FOUND_MESSAGE = "Manager not found with id: %s.";

    /**
     * Error message template used when an admin with the specified ID cannot be found in the system.
     */
    private static final String ADMIN_NOT_FOUND_MESSAGE = "Admin not found with id: %s.";

    /**
     * Error message indicating that the provided current password does not match the stored password for the user.
     */
    private static final String INCORRECT_CURRENT_PASSWORD_MESSAGE = "Current password is incorrect.";

    /**
     * Error message indicating that the new password and its confirmation do not match.
     */
    private static final String NEW_PASSWORD_NOT_MATCH_MESSAGE = "New password and confirmation do not match.";

    /**
     * Error message indicating that the new password must differ from the current password.
     */
    private static final String CURRENT_PASSWORD_EQUAL_NEW_PASSWORD_MESSAGE = """
        The current and new passwords are the same. Enter a different new password.
        """;

    /**
     * Repository for accessing admin user data to validate password updates for admin roles.
     */
    private final AdminRepository adminRepository;

    /**
     * Repository for accessing manager user data to validate password updates for manager roles.
     */
    private final ManagerRepository managerRepository;

    /**
     * Repository for accessing student user data to validate password updates for student roles.
     */
    private final StudentRepository studentRepository;

    /**
     * Repository for accessing teacher user data to validate password updates for teacher roles.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Encoder for hashing and verifying passwords during password update validation.
     * <p>
     * Used to check if the provided current password matches the stored hashed password for the user.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The {@link PasswordUpdateRequestDto} object currently being validated.
     * <p>
     * Stores the data of the password update request being checked against the validation rules.
     */
    private PasswordUpdateRequestDto value;

    /**
     * The validation context used to report constraint violations.
     * <p>
     * Provides the mechanism to add custom error messages when validation fails.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link ValidPasswordUpdateRequest} annotation.
     * <p>
     * This method is invoked once during validator instantiation to perform any necessary setup based on the
     * annotation's configuration. Currently, it delegates to the default implementation without additional logic.
     *
     * @param constraintAnnotation the {@link ValidPasswordUpdateRequest} annotation instance being validated
     */
    @Override
    public void initialize(ValidPasswordUpdateRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the password update request.
     * <p>
     * Ensures that the {@link PasswordUpdateRequestDto} meets the following criteria: the current password is correct
     * (verified via {@link PasswordEncoder}), the new password matches its confirmation, and the new password differs
     * from the current one. Returns {@code true} if all conditions are met, and {@code false} otherwise, adding error
     * messages to the {@link ConstraintValidatorContext} if validation fails. Throws a {@link UserNotFoundException}
     * if the user is not found in any repository based on the provided ID and role.
     *
     * @param value   the {@link PasswordUpdateRequestDto} object containing the password update request data
     * @param context the {@link ConstraintValidatorContext} for reporting validation errors
     * @return {@code true} if the password update request is valid; {@code false} otherwise
     */
    @Override
    public boolean isValid(PasswordUpdateRequestDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        this.value = value;
        this.context = context;

        boolean isCurrentPasswordCorrect = validateCurrentPassword();
        boolean isNewPasswordConfirmed = validateNewPasswordMatchesConfirmation();
        boolean areCurrentAndNewPasswordsDifferent = true;

        if (isCurrentPasswordCorrect) {
            areCurrentAndNewPasswordsDifferent = validateCurrentAndNewPasswordsAreDifferent();
        }

        return isCurrentPasswordCorrect && areCurrentAndNewPasswordsDifferent && isNewPasswordConfirmed;
    }

    private boolean validateCurrentPassword() {
        long userId = value.getUserId();
        RoleName userRole = value.getRoleName();

        String passwordHash = switch (userRole) {
            case STUDENT -> studentRepository.findById(userId)
                .map(User::getPasswordHash)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                    STUDENT_NOT_FOUND_MESSAGE.formatted(userId)));
            case TEACHER -> teacherRepository.findById(userId)
                .map(User::getPasswordHash)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                    TEACHER_NOT_FOUND_MESSAGE.formatted(userId)));
            case MANAGER -> managerRepository.findById(userId)
                .map(Manager::getPasswordHash)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                    MANAGER_NOT_FOUND_MESSAGE.formatted(userId)));
            case ADMIN -> adminRepository.findById(value.getUserId())
                .map(Admin::getPasswordHash)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                    ADMIN_NOT_FOUND_MESSAGE.formatted(userId)));
        };

        return isPasswordMatching(passwordHash);
    }

    private boolean isPasswordMatching(String passwordHash) {
        boolean isMatch = passwordEncoder.matches(value.getCurrentPassword(), passwordHash);
        addConstraintViolationMessageIfInvalid(!isMatch, INCORRECT_CURRENT_PASSWORD_MESSAGE);
        return isMatch;
    }

    private boolean validateNewPasswordMatchesConfirmation() {
        boolean isConfirmed = value.getNewPassword().equals(value.getConfirmNewPassword());
        addConstraintViolationMessageIfInvalid(!isConfirmed, NEW_PASSWORD_NOT_MATCH_MESSAGE);
        return isConfirmed;
    }

    private boolean validateCurrentAndNewPasswordsAreDifferent() {
        boolean areDifferent = !value.getCurrentPassword().equals(value.getNewPassword());
        addConstraintViolationMessageIfInvalid(!areDifferent, CURRENT_PASSWORD_EQUAL_NEW_PASSWORD_MESSAGE);
        return areDifferent;
    }

    private void addConstraintViolationMessageIfInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
