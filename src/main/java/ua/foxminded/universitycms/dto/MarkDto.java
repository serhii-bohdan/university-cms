package ua.foxminded.universitycms.dto;

/**
 * The {@code MarkDto} class is a data transfer object (DTO) for mark entities.
 * <p>
 * This class includes fields for mark ID, mark value, student ID, and topic ID.
 * It also includes getter and setter methods for these fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the mark DTO.
 *
 * @author Serhii Bohdan
 */
public class MarkDto {

    private Long markId;
    private Integer markValue;
    private Long studentId;
    private Long topicId;

    /**
     * Constructs a new {@code MarkDto} with the specified mark value, student ID,
     * and topic ID.
     *
     * @param markValue the value of the mark
     * @param studentId the ID of the student who received the mark
     * @param topicId   the ID of the topic for which the mark was given
     */
    public MarkDto(Integer markValue, Long studentId, Long topicId) {
        this.markValue = markValue;
        this.studentId = studentId;
        this.topicId = topicId;
    }

    /**
     * Constructs a new {@code MarkDto} with no initial values.
     */
    public MarkDto() {
    }

    public Long getMarkId() {
        return markId;
    }

    public void setMarkId(Long markId) {
        this.markId = markId;
    }

    public Integer getMarkValue() {
        return markValue;
    }

    public void setMarkValue(Integer markValue) {
        this.markValue = markValue;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    /**
     * Returns a string representation of the mark DTO.
     *
     * @return a string representation of the mark DTO
     */
    @Override
    public String toString() {
        return "MarkDto [markId=" + markId + ", markValue=" + markValue + ", studentId=" + studentId + ", topicId="
                + topicId + "]";
    }

}
