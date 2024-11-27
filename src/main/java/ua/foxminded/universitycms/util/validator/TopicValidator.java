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
 * Validator for the {@link UniqueCourseTopic} annotation.
 * <p>
 * This class validates that a topic is unique within the context of a course. It ensures that
 * both the topic name and the topic order are unique. The validation logic is implemented for
 * both saving a new topic and updating an existing one.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class TopicValidator implements ConstraintValidator<UniqueCourseTopic, TopicDto> {

    /**
     * Error message for duplicate topic names within the same course.
     */
    private static final String TOPIC_NAME_NOT_UNIQUE_MESSAGE = """
        A topic with this name already exists in this course. Enter a new topic name.
        """;

    /**
     * Error message for duplicate topic orders within the same course.
     */
    private static final String TOPIC_ORDER_NOT_UNIQUE_MESSAGE = """
        A topic with this order already exists in this course. Enter a new topic sequence number.
        """;

    /**
     * Repository used for querying existing topics in the database.
     */
    private final TopicRepository topicRepository;

    /**
     * The {@link TopicDto} object being validated.
     */
    private TopicDto value;

    /**
     * The {@link ConstraintValidatorContext} used to build validation error messages.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator.
     * <p>
     * This method is called before the validator is used. It can be overridden to implement any
     * initialization logic, but this implementation relies on the default behavior.
     *
     * @param constraintAnnotation the annotation instance for {@link UniqueCourseTopic}
     */
    @Override
    public void initialize(UniqueCourseTopic constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the given {@link TopicDto}.
     * <p>
     * This method checks if the topic's name and order are unique within the associated course. If the
     * {@code TopicDto} represents a new topic, it invokes {@code isTopicUniqueForSave()}. If the topic
     * already exists, it invokes {@code isTopicUniqueForUpdate()}.
     *
     * @param value   the {@link TopicDto} to validate
     * @param context the {@link ConstraintValidatorContext} for reporting validation errors
     * @return {@code true} if the topic is valid, otherwise {@code false}
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
