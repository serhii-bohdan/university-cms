package ua.foxminded.universitycms.dto;

/**
 * The {@code TopicDto} class is a data transfer object (DTO) for topic
 * entities.
 * <p>
 * This class includes fields for topic ID, topic name, topic description, and
 * course ID. It also includes getter and setter methods for these fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the topic DTO.
 *
 * @author Serhii Bohdan
 */
public class TopicDto {

    private Long topicId;
    private String topicName;
    private String topicDescription;
    private Long courseId;

    /**
     * Constructs a new {@code TopicDto} with the specified topic name, topic
     * description, and course ID.
     *
     * @param topicName        the name of the topic
     * @param topicDescription the description of the topic
     * @param courseId         the ID of the course that the topic belongs to
     */
    public TopicDto(String topicName, String topicDescription, Long courseId) {
        this.topicName = topicName;
        this.topicDescription = topicDescription;
        this.courseId = courseId;
    }

    /**
     * Constructs a new {@code TopicDto} with no initial values.
     */
    public TopicDto() {
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    /**
     * Returns a string representation of the topic DTO.
     *
     * @return a string representation of the topic DTO
     */
    @Override
    public String toString() {
        return "TopicDto [topicId=" + topicId + ", topicName=" + topicName + ", topicDescription=" + topicDescription
                + ", courseId=" + courseId + "]";
    }

}
