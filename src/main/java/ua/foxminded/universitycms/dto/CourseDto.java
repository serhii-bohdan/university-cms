package ua.foxminded.universitycms.dto;

import java.util.Set;

/**
 * The {@code CourseDto} class is a data transfer object (DTO) for course
 * entities.
 * <p>
 * This class includes fields for course ID, course name, course description,
 * author ID, and a set of topics. It also includes getter and setter methods
 * for these fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the course DTO.
 *
 * @author Serhii Bohdan
 */
public class CourseDto {

    private Long courseId;
    private String courseName;
    private String courseDescription;
    private Long authorId;
    private Set<TopicDto> topics;

    /**
     * Constructs a new {@code CourseDto} with the specified course name, course
     * description, and author ID.
     *
     * @param courseName        the name of the course
     * @param courseDescription the description of the course
     * @param authorId          the ID of the author of the course
     */
    public CourseDto(String courseName, String courseDescription, Long authorId) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.authorId = authorId;
    }

    /**
     * Constructs a new {@code CourseDto} with no initial values.
     */
    public CourseDto() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Set<TopicDto> getTopics() {
        return topics;
    }

    public void setTopics(Set<TopicDto> topics) {
        this.topics = topics;
    }

    /**
     * Returns a string representation of the course DTO.
     *
     * @return a string representation of the course DTO
     */
    @Override
    public String toString() {
        return "CourseDto [courseId=" + courseId + ", courseName=" + courseName + ", courseDescription="
                + courseDescription + ", authorId=" + authorId + ", topics=" + topics + "]";
    }

}
