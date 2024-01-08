package ua.foxminded.universitycms.model;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The {@code Mark} class represents a mark in the system.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * mark's ID, value, the student who received the mark, and the topic for which
 * the mark was given. It also includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "marks")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mark_id")
    private Long markId;

    @Column(name = "mark_value")
    private Integer markValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    /**
     * Constructs a new {@code Mark} object with the given parameters.
     *
     * @param markValue the value of the mark
     * @param student   the student who received the mark
     * @param topic     the topic for which the mark was given
     */
    public Mark(Integer markValue, Student student, Topic topic) {
        this.markValue = markValue;
        this.student = student;
        this.topic = topic;
    }

    /**
     * Constructs a new {@code Mark} object with default values.
     */
    public Mark() {
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
     * Returns a hash code value for the mark.
     *
     * @return a hash code value for this mark
     */
    @Override
    public int hashCode() {
        return Objects.hash(markValue, student, topic);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * mark value, student, and topic.
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
        if (!(obj instanceof Mark)) {
            return false;
        }
        Mark other = (Mark) obj;
        return Objects.equals(markValue, other.markValue) && Objects.equals(student, other.student)
                && Objects.equals(topic, other.topic);
    }

    /**
     * Returns a string representation of the mark.
     *
     * @return a string representation of this mark
     */
    @Override
    public String toString() {
        return "Mark [markId=" + markId + ", markValue=" + markValue + "]";
    }

}
