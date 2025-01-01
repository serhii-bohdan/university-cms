package ua.foxminded.universitycms.util.validator;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.model.StudyDay;
import ua.foxminded.universitycms.repository.StudyDayRepository;
import ua.foxminded.universitycms.util.annotation.UniqueStudyDayWithinSchedule;

/**
 * Validator class for enforcing the rules defined by the {@link UniqueStudyDayWithinSchedule} annotation.
 * This validator checks that a study day is unique within the specified schedule
 * and that the selected date is valid for scheduling. If validation fails, an error message is added
 * to the {@link ConstraintValidatorContext}.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class StudyDayValidator implements ConstraintValidator<UniqueStudyDayWithinSchedule, StudyDayDto> {

    /**
     * Message indicating that a study day with the specified date already exists in the schedule.
     * This message is used when the uniqueness validation fails.
     */
    private static final String DUPLICATE_STUDY_DAY_MESSAGE = """
        A study day with this date already exists in the schedule.
        """;

    /**
     * Message indicating that the selected date for the study day has already passed.
     * This message is used when the date validation fails.
     */
    private static final String PAST_DATE_NOT_ALLOWED_MESSAGE = """
        The selected date has already passed. You can only schedule
        the current or a future date.
        """;

    /**
     * Repository for performing database operations related to {@link StudyDay} entities.
     * Used to check for existing study days in the schedule during validation.
     */
    private final StudyDayRepository studyDayRepository;

    /**
     * The {@link StudyDayDto} object being validated.
     * This field stores the current value being checked for constraints.
     */
    private StudyDayDto value;

    /**
     * The context in which the constraint validation is executed.
     * Used to build and add custom constraint violation messages.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator for the {@link UniqueStudyDayWithinSchedule} annotation.
     * This method is invoked before the validation logic is applied, allowing for any
     * necessary setup or initialization.
     *
     * @param constraintAnnotation the annotation instance being validated
     */
    @Override
    public void initialize(UniqueStudyDayWithinSchedule constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the {@link StudyDayDto} object against the rules for uniqueness and valid scheduling.
     * Returns true if the {@link StudyDayDto} passes validation, otherwise false.
     *
     * @param value   the {@link StudyDayDto} object to validate
     * @param context the validation context for adding constraint violation messages
     * @return {@code true} if the {@link StudyDayDto} passes validation, otherwise {@code false}
     */
    @Override
    public boolean isValid(StudyDayDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        this.value = value;
        this.context = context;

        return isUniqueStudyDay() && isDateValidForScheduling();
    }

    private boolean isUniqueStudyDay() {
        boolean isUnique = studyDayRepository.findByScheduleIdAndDate(value.getScheduleId(), value.getDate()).isEmpty();
        addConstraintViolationMessageIfInvalid(!isUnique, DUPLICATE_STUDY_DAY_MESSAGE);
        return isUnique;
    }

    private boolean isDateValidForScheduling() {
        LocalDate today = LocalDate.now();
        LocalDate studyDayDate = value.getDate();
        boolean isValid = studyDayDate != null && (today.isEqual(studyDayDate) || today.isBefore(studyDayDate));
        addConstraintViolationMessageIfInvalid(!isValid, PAST_DATE_NOT_ALLOWED_MESSAGE);
        return isValid;
    }

    private void addConstraintViolationMessageIfInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
