package ua.foxminded.universitycms.util.validator;

import java.util.Optional;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.repository.StudentRepository;
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
     * Error message indicating that the user associated with the password update request could not be found.
     */
    private static final String USER_NOT_FOUND_MESSAGE = "User not found.";

    /**
     * Error message indicating that the provided current password does not match the stored password for the user.
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
     * Encoder for hashing passwords, used to verify the current password provided during the update request.
     */
    private final PasswordEncoder passwordEncoder;

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

        boolean isCurrentPasswordCorrect = validateCurrentPassword(value, context);
        boolean isNewPasswordConfirmed = validateNewPasswordMatchesConfirmation(value, context);
        boolean areCurrentAndNewPasswordsDifferent = true;

        if (isCurrentPasswordCorrect) {
            areCurrentAndNewPasswordsDifferent = validateCurrentAndNewPasswordsAreDifferent(value, context);
        }

        return isCurrentPasswordCorrect && areCurrentAndNewPasswordsDifferent && isNewPasswordConfirmed;
    }

    private boolean validateCurrentPassword(PasswordUpdateRequestDto passwordUpdateRequest, ConstraintValidatorContext context) {
        Optional<Student> optional = studentRepository.findById(passwordUpdateRequest.getUserId());

        if (optional.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(USER_NOT_FOUND_MESSAGE)
                .addConstraintViolation();
            return false;
        }

        boolean isMatch = passwordEncoder.matches(passwordUpdateRequest.getCurrentPassword(), optional.get().getPasswordHash());
        addConstraintViolationMessageIfInvalid(!isMatch, INCORRECT_CURRENT_PASSWORD_MESSAGE, context);
        return isMatch;
    }

    private boolean validateNewPasswordMatchesConfirmation(PasswordUpdateRequestDto value, ConstraintValidatorContext context) {
        boolean isConfirmed = value.getNewPassword().equals(value.getConfirmNewPassword());
        addConstraintViolationMessageIfInvalid(!isConfirmed, NEW_PASSWORD_NOT_MATCH_MESSAGE, context);
        return isConfirmed;
    }

    private boolean validateCurrentAndNewPasswordsAreDifferent(PasswordUpdateRequestDto value, ConstraintValidatorContext context) {
        boolean areDifferent = !value.getCurrentPassword().equals(value.getNewPassword());
        addConstraintViolationMessageIfInvalid(!areDifferent, CURRENT_PASSWORD_EQUAL_NEW_PASSWORD_MESSAGE, context);
        return areDifferent;
    }

    private void addConstraintViolationMessageIfInvalid(boolean isInvalid, String message, ConstraintValidatorContext context) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
