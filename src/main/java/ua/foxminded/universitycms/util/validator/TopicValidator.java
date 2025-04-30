package ua.foxminded.universitycms.util.validator;

import java.util.List;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.repository.TopicRepository;
import ua.foxminded.universitycms.util.annotation.UniqueCourseTopic;

/**
 * Validator for ensuring the uniqueness of a topic within a course in the university management system.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to enforce the {@link UniqueCourseTopic}
 * annotation. It validates that a topic's name and order, as provided in a {@link TopicDto} object, are unique
 * among topics within the associated course. The validation logic distinguishes between creating a new topic (no ID)
 * and updating an existing one (with an ID), using the {@link TopicRepository} to query existing topics. The
 * {@code @Component} annotation registers this class as a Spring-managed bean, and {@code @RequiredArgsConstructor}
 * ensures dependency injection of the repository.
 *
 * @author Serhii Bohdan
 * @see ConstraintValidator
 * @see UniqueCourseTopic
 * @see TopicDto
 * @see TopicRepository
 */
@Component
@RequiredArgsConstructor
public class TopicValidator implements ConstraintValidator<UniqueCourseTopic, TopicDto> {

    /**
     * Error message indicating that a topic name is not unique within the course.
     */
    private static final String TOPIC_NAME_NOT_UNIQUE_MESSAGE = """
        A topic with this name already exists in this course. Enter a new topic name.
        """;

    /**
     * Error message indicating that a topic order is not unique within the course.
     */
    private static final String TOPIC_ORDER_NOT_UNIQUE_MESSAGE = """
        A topic with this order already exists in this course. Enter a new topic sequence number.
        """;

    /**
     * Repository for querying {@link Topic} data to perform uniqueness checks.
     */
    private final TopicRepository topicRepository;

    /**
     * The {@link TopicDto} object currently being validated.
     */
    private TopicDto value;

    /**
     * The context used to report constraint violations during validation.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link UniqueCourseTopic} annotation.
     * <p>
     * This method is invoked once during validator instantiation to perform any necessary setup based on the
     * annotation's configuration. Currently, it delegates to the default implementation without additional logic.
     *
     * @param constraintAnnotation the {@link UniqueCourseTopic} annotation instance being validated
     */
    @Override
    public void initialize(UniqueCourseTopic constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the uniqueness of a topic's name and order within a course.
     * <p>
     * Checks the provided {@link TopicDto} against existing topics in the associated course. If the topic has no ID
     * (indicating creation), it ensures the name and order are unique among all topics. If the topic has an ID
     * (indicating an update), it ensures uniqueness among all topics except the one being updated. Returns
     * {@code true} if both conditions are met, and {@code false} otherwise, adding error messages to the context if
     * validation fails.
     *
     * @param value   the {@link TopicDto} object to validate
     * @param context the {@link ConstraintValidatorContext} for reporting validation errors
     * @return {@code true} if the topic name and order are unique; {@code false} otherwise
     */
    @Override
    public boolean isValid(TopicDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        this.value = value;
        this.context = context;

        return this.value.getId() == null
            ? isTopicUniqueForSave()
            : isTopicUniqueForUpdate();
    }

    private boolean isTopicUniqueForSave() {
        List<Topic> courseTopics = topicRepository.findByCourseId(value.getCourseId());
        return isTopicNameUniqueWithinCourseForSave(courseTopics) &&
            isTopicOrderUniqueWithinCourseForSave(courseTopics);
    }

    private boolean isTopicNameUniqueWithinCourseForSave(List<Topic> topics) {
        boolean isTopicNameUnique = topics.stream().noneMatch(t -> t.getTopicName().equals(value.getTopicName()));
        addConstraintViolationMessageIfInvalid(!isTopicNameUnique, TOPIC_NAME_NOT_UNIQUE_MESSAGE);
        return isTopicNameUnique;
    }

    private boolean isTopicOrderUniqueWithinCourseForSave(List<Topic> topics) {
        boolean isTopicOrderUnique = topics.stream().noneMatch(t -> t.getTopicOrder().equals(value.getTopicOrder()));
        addConstraintViolationMessageIfInvalid(!isTopicOrderUnique, TOPIC_ORDER_NOT_UNIQUE_MESSAGE);
        return isTopicOrderUnique;
    }

    private boolean isTopicUniqueForUpdate() {
        List<Topic> courseTopics = topicRepository.findByCourseId(value.getCourseId());
        return isTopicNameUniqueWithinCourseForUpdate(courseTopics) &&
            isTopicOrderUniqueWithinCourseForUpdate(courseTopics);
    }

    private boolean isTopicNameUniqueWithinCourseForUpdate(List<Topic> topics) {
        boolean isTopicNameUnique = topics.stream()
            .filter(t -> !t.getId().equals(value.getId()))
            .noneMatch(t -> t.getTopicName().equals(value.getTopicName()));
        addConstraintViolationMessageIfInvalid(!isTopicNameUnique, TOPIC_NAME_NOT_UNIQUE_MESSAGE);
        return isTopicNameUnique;
    }

    private boolean isTopicOrderUniqueWithinCourseForUpdate(List<Topic> topics) {
        boolean isTopicOrderUnique = topics.stream()
            .filter(t -> !t.getId().equals(value.getId()))
            .noneMatch(t -> t.getTopicOrder().equals(value.getTopicOrder()));
        addConstraintViolationMessageIfInvalid(!isTopicOrderUnique, TOPIC_ORDER_NOT_UNIQUE_MESSAGE);
        return isTopicOrderUnique;
    }

    private void addConstraintViolationMessageIfInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
