package ua.foxminded.universitycms.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.util.annotation.ValidPasswordUpdateRequest;

/**
 * Validator for password update requests.
 * <p>
 * Implements the {@link ConstraintValidator} for the {@link ValidPasswordUpdateRequest} annotation.
 * Ensures that the current password is valid, the new password matches the confirmation,
 * and that the current and new passwords are different.
 * <p>
 * Utilizes {@link StudentRepository} to check user existence and {@link PasswordEncoder}
 * to verify the current password.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class PasswordUpdateRequestValidator implements ConstraintValidator<ValidPasswordUpdateRequest, PasswordUpdateRequestDto> {

    /**
     * Error message indicating that the user associated with the password update
     * request could not be found.
     */
    private static final String USER_NOT_FOUND_MESSAGE = "User not found.";

    /**
     * Error message indicating that the provided current password does not match the stored
     * password for the user.
     */
    private static final String INCORRECT_CURRENT_PASSWORD_MESSAGE = "Current password is incorrect.";

    /**
     * Error message indicating that the new password and its confirmation do not match.
     */
    private static final String NEW_PASSWORD_NOT_MATCH_MESSAGE = "New password and confirmation do not match.";

    /**
     * Error message indicating that the new password must be different from the current password.
     */
    private static final String CURRENT_PASSWORD_EQUAL_NEW_PASSWORD_MESSAGE = """
        The current and new passwords are the same. Enter a different new password.
        """;

    /**
     * Repository for accessing student data.
     */
    private final StudentRepository studentRepository;

    /**
     * The repository for accessing teacher data.
     */
    private final TeacherRepository teacherRepository;

    /**
     * Encoder for hashing passwords, used to verify the current password provided during the update request.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The {@code PasswordUpdateRequestDto} object that is being validated.
     */
    private PasswordUpdateRequestDto value;

    /**
     * The context of the validation process.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator. This method is called before the validation starts.
     *
     * @param constraintAnnotation the annotation instance for the validation
     */
    @Override
    public void initialize(ValidPasswordUpdateRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the password update request.
     * Checks if the provided password update request is valid by ensuring the following:
     * <ul>
     *   <li>The current password is correct.</li>
     *   <li>The new password matches its confirmation.</li>
     *   <li>The current and new passwords are different.</li>
     * </ul>
     *
     * @param value   the {@link PasswordUpdateRequestDto} object containing the password update request data
     * @param context the context in which the constraint is evaluated
     * @return {@code true} if the password update request is valid, {@code false} otherwise
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
        String passwordHash = USER_NOT_FOUND_MESSAGE;

        if (RoleName.STUDENT.equals(value.getRoleName())) {
            passwordHash = studentRepository.findById(value.getUserId())
                .map(User::getPasswordHash)
                .orElse(passwordHash);
        } else if (RoleName.TEACHER.equals(value.getRoleName())) {
            passwordHash = teacherRepository.findById(value.getUserId())
                .map(User::getPasswordHash)
                .orElse(passwordHash);
        }

        if (USER_NOT_FOUND_MESSAGE.equals(passwordHash)) {
            addConstraintViolationMessageIfInvalid(true, USER_NOT_FOUND_MESSAGE);
            return false;
        }

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
