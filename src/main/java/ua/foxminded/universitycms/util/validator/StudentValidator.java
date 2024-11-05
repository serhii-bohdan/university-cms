package ua.foxminded.universitycms.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.util.annotation.UniqueStudentEmail;

/**
 * Validator for ensuring the uniqueness of student email addresses.
 * <p>
 * This class implements the {@link ConstraintValidator} interface, validating that a student's email
 * address is unique when creating or updating a student record.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class StudentValidator implements ConstraintValidator<UniqueStudentEmail, StudentDto> {

    /**
     * Error message indicating that the email is not unique.
     */
    private static final String EMAIL_NOT_UNIQUE_MESSAGE = """
        A student with such an email already exists.
        Please enter another email.""";

    /**
     * Repository for accessing student data.
     */
    private final StudentRepository studentRepository;

    /**
     * Initializes the validator with the given annotation.
     *
     * @param constraintAnnotation The annotation instance for a given constraint.
     */
    @Override
    public void initialize(UniqueStudentEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the student DTO to ensure that the email address is unique.
     *
     * @param value   The student DTO to validate.
     * @param context The context in which the constraint is evaluated.
     * @return {@code true} if the email is unique, {@code false} otherwise.
     */
    @Override
    public boolean isValid(StudentDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.getId() != null
            ? isEmailUnique(value, context)
            : isEmailUnique(value.getEmail(), context);
    }

    private boolean isEmailUnique(String email, ConstraintValidatorContext context) {
        boolean isEmailUnique = studentRepository.findAll().stream()
            .noneMatch(s -> s.getEmail().equals(email));

        addViolationMessageIfInvalid(!isEmailUnique, context);
        return isEmailUnique;
    }

    private boolean isEmailUnique(StudentDto student, ConstraintValidatorContext context) {
        boolean isEmailUnique = studentRepository.findAll().stream()
            .filter(s -> !s.getId().equals(student.getId()))
            .noneMatch(s -> s.getEmail().equals(student.getEmail()));

        addViolationMessageIfInvalid(!isEmailUnique, context);
        return isEmailUnique;
    }

    private void addViolationMessageIfInvalid(boolean isInvalid, ConstraintValidatorContext context) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(EMAIL_NOT_UNIQUE_MESSAGE)
                .addConstraintViolation();
        }
    }

}
