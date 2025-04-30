package ua.foxminded.universitycms.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.util.annotation.UniqueGroupName;

/**
 * Validator for ensuring the uniqueness of a group name in the university management system.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to enforce the {@link UniqueGroupName}
 * annotation. It checks whether the group name provided in a {@link GroupDto} object is unique among existing
 * groups in the system, using the {@link GroupRepository} to query the database. The validation logic distinguishes
 * between creating a new group (no ID) and updating an existing group (with an ID), ensuring uniqueness in each case.
 * The {@code @Component} annotation registers this class as a Spring-managed bean, and {@code @RequiredArgsConstructor}
 * ensures dependency injection of the repository.
 *
 * @author Serhii Bohdan
 * @see ConstraintValidator
 * @see UniqueGroupName
 * @see GroupDto
 * @see GroupRepository
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
     * The {@code GroupDto} object that is being validated.
     */
    private GroupDto value;

    /**
     * The context of the validation process.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link UniqueGroupName} annotation.
     * <p>
     * This method is invoked once during validator instantiation to perform any necessary setup based on the
     * annotation's configuration. Currently, it delegates to the default implementation without additional logic.
     *
     * @param constraintAnnotation the {@link UniqueGroupName} annotation instance being validated
     */
    @Override
    public void initialize(UniqueGroupName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the uniqueness of a group name.
     * <p>
     * If the group has no ID (indicating creation), the name must be unique among all existing groups. If the group
     * has an ID (indicating an update), the name must be unique among all groups except the one being updated. Returns
     * {@code true} if the name is unique, and {@code false} otherwise, adding an error message to the context if
     * validation fails.
     *
     * @param value   the {@link GroupDto} object to validate
     * @param context the {@link ConstraintValidatorContext} for reporting validation errors
     * @return {@code true} if the group name is unique; {@code false} if it duplicates an existing name
     */
    @Override
    public boolean isValid(GroupDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        this.value = value;
        this.context = context;

        return this.value.getId() == null
            ? isGroupNameUnique(this.value.getGroupName())
            : isGroupNameUnique();
    }

    private boolean isGroupNameUnique(String groupName) {
        boolean isGroupNameUnique = groupRepository.findAll().stream()
            .noneMatch(g -> g.getGroupName().equals(groupName));

        addViolationMessageIfInvalid(!isGroupNameUnique);
        return isGroupNameUnique;
    }

    private boolean isGroupNameUnique() {
        boolean isGroupNameUnique = groupRepository.findAll().stream()
            .filter(g -> !g.getId().equals(value.getId()))
            .noneMatch(g -> g.getGroupName().equals(value.getGroupName()));

        addViolationMessageIfInvalid(!isGroupNameUnique);
        return isGroupNameUnique;
    }

    private void addViolationMessageIfInvalid(boolean isInvalid) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(GROUP_NAME_NOT_UNIQUE_MESSAGE)
                .addConstraintViolation();
        }
    }

}
