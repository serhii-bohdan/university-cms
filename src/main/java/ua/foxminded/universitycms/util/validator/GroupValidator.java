package ua.foxminded.universitycms.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.util.annotation.UniqueGroupName;

/**
 * Validator for checking the uniqueness of a group in the system.
 * <p>
 * This class implements the {@link ConstraintValidator} for the {@link UniqueGroupName} annotation,
 * ensuring that the group being created or updated has a unique group name. It uses the
 * {@link GroupRepository} to verify whether a group with the same name already exists in the database.
 * <p>
 * The validation process varies based on whether the group already has an ID (indicating it is being updated)
 * or not (indicating it is being created). If an ID is present, the validator ensures that the updated group's name
 * is unique among other groups except the one being updated.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class GroupValidator implements ConstraintValidator<UniqueGroupName, GroupDto> {

    /**
     * Error message indicating that a group name is not unique.
     */
    private static final String GROUP_NAME_NOT_UNIQUE_MESSAGE = """
        A group with this name already exists. Enter a different group name.
        """;

    /**
     * Repository for accessing group data.
     */
    private final GroupRepository groupRepository;

    /**
     * Initializes the validator. This method is called before validation starts.
     *
     * @param constraintAnnotation the annotation instance for the validation
     */
    @Override
    public void initialize(UniqueGroupName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the uniqueness of a group name.
     * <p>
     * If the group has an ID, it is assumed to be an update operation, and the group name must
     * be unique among other groups except itself. If no ID is present, it is a creation operation,
     * and the name must be unique among all existing groups.
     *
     * @param value   the {@link GroupDto} object to be validated
     * @param context the context in which the constraint is evaluated
     * @return {@code true} if the group name is unique, {@code false} otherwise
     */
    @Override
    public boolean isValid(GroupDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.getId() != null
            ? isGroupNameUnique(value, context)
            : isGroupNameUnique(value.getGroupName(), context);
    }

    private boolean isGroupNameUnique(String groupName, ConstraintValidatorContext context) {
        boolean isGroupNameUnique = groupRepository.findAll().stream()
            .noneMatch(g -> g.getGroupName().equals(groupName));

        addViolationMessageIfInvalid(!isGroupNameUnique, context);
        return isGroupNameUnique;
    }

    private boolean isGroupNameUnique(GroupDto group, ConstraintValidatorContext context) {
        boolean isGroupNameUnique = groupRepository.findAll().stream()
            .filter(g -> !g.getId().equals(group.getId()))
            .noneMatch(g -> g.getGroupName().equals(group.getGroupName()));

        addViolationMessageIfInvalid(!isGroupNameUnique, context);
        return isGroupNameUnique;
    }

    private void addViolationMessageIfInvalid(boolean isInvalid, ConstraintValidatorContext context) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(GROUP_NAME_NOT_UNIQUE_MESSAGE)
                .addConstraintViolation();
        }
    }

}
