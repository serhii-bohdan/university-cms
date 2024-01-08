package ua.foxminded.universitycms.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The {@code Topic} class represents a topic in a course in the system.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * topic's ID, name, description, the course it belongs to, and the marks
 * associated with it. It also includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "topic_description")
    private String topicDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Mark> marks = new HashSet<>();

    /**
     * Constructs a new {@code Topic} object with the given parameters.
     *
     * @param topicName        the name of the topic
     * @param topicDescription the description of the topic
     * @param course           the course that the topic belongs to
     */
    public Topic(String topicName, String topicDescription, Course course) {
        this.topicName = topicName;
        this.topicDescription = topicDescription;
        this.course = course;
    }

    /**
     * Constructs a new {@code Topic} object with default values.
     */
    public Topic() {
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Mark> getMarks() {
        return Collections.unmodifiableSet(marks);
    }

    /**
     * Returns a hash code value for the topic.
     *
     * @return a hash code value for this topic
     */
    @Override
    public int hashCode() {
        return Objects.hash(course, topicDescription, topicName);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * course, topic description, and topic name.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Topic)) {
            return false;
        }
        Topic other = (Topic) obj;
        return Objects.equals(course, other.course) && Objects.equals(topicDescription, other.topicDescription)
                && Objects.equals(topicName, other.topicName);
    }

    /**
     * Returns a string representation of the topic.
     *
     * @return a string representation of this topic
     */
    @Override
    public String toString() {
        return "Topic [topicId=" + topicId + ", topicName=" + topicName + ", topicDescription=" + topicDescription
                + "]";
    }

}
