package ua.foxminded.universitycms.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.util.annotation.UniqueGroup;

/**
 * Validator for the {@link UniqueGroup} annotation, ensuring that a group is unique in the system.
 * <p>
 * This class implements the {@link ConstraintValidator} interface and provides the logic
 * to check whether a {@link GroupDto} object violates the uniqueness constraint. It is responsible
 * for validating that no other group exists with the same name, except for the case of an update where
 * the current group is allowed to retain its name.
 * <p>
 * The uniqueness check is performed against the data stored in the {@link GroupRepository}.
 * This validator is applied at the DTO level, typically during group creation or update operations,
 * and ensures that group names are not duplicated across the system.
 */
@Component
@RequiredArgsConstructor
public class UniqueGroupValidator implements ConstraintValidator<UniqueGroup, GroupDto> {

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
    public void initialize(UniqueGroup constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates whether the given {@link GroupDto} is unique.
     * <p>
     * If the {@code GroupDto} has an ID (indicating an update), it checks for uniqueness
     * excluding the current group being updated. Otherwise, it checks for name uniqueness.
     *
     * @param value   the {@code GroupDto} object to validate
     * @param context the validation context
     * @return {@code true} if the group name is unique, otherwise {@code false}
     */
    @Override
    public boolean isValid(GroupDto value, ConstraintValidatorContext context) {
        if (value.getId() != null) {
            return isGroupNameUnique(value);
        }

        return isGroupNameUnique(value.getGroupName());
    }

    private boolean isGroupNameUnique(String groupName) {
        return groupRepository.findAll().stream()
            .noneMatch(g -> g.getGroupName().equals(groupName));
    }

    private boolean isGroupNameUnique(GroupDto group) {
        return groupRepository.findAll().stream()
            .filter(g -> !g.getId().equals(group.getId()))
            .noneMatch(g -> g.getGroupName().equals(group.getGroupName()));
    }

}
