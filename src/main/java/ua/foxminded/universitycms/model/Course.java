package ua.foxminded.universitycms.model;

import java.time.LocalDateTime;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * The {@code Course} class represents a course in the system.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * course's ID, name, description, author (teacher), topics, students, lessons,
 * and timestamps for when the course was created and last updated. It also
 * includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_description")
    private String courseDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher author;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Topic> topics = new HashSet<>();

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Lesson> lessons = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Constructs a new {@code Course} object with the given parameters.
     *
     * @param courseName        the name of the course
     * @param courseDescription the description of the course
     * @param author            the teacher who authored the course
     */
    public Course(String courseName, String courseDescription, Teacher author) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.author = author;
    }

    /**
     * Constructs a new {@code Course} object with default values.
     */
    public Course() {
    }

    /**
     * Sets the {@code createdAt} field to the current time when the course is
     * created.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Sets the {@code updatedAt} field to the current time when the course is
     * updated.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public Teacher getAuthor() {
        return author;
    }

    public void setAuthor(Teacher author) {
        this.author = author;
    }

    public Set<Topic> getTopics() {
        return Collections.unmodifiableSet(topics);
    }

    public Set<Student> getStudents() {
        return Collections.unmodifiableSet(students);
    }

    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(lessons);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns a hash code value for the course.
     *
     * @return a hash code value for this course
     */
    @Override
    public int hashCode() {
        return Objects.hash(courseDescription, courseName);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * course names and descriptions.
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
        if (!(obj instanceof Course)) {
            return false;
        }
        Course other = (Course) obj;
        return Objects.equals(courseDescription, other.courseDescription)
                && Objects.equals(courseName, other.courseName);
    }

    /**
     * Returns a string representation of the course.
     *
     * @return a string representation of this course
     */
    @Override
    public String toString() {
        return "Course [courseId=" + courseId + ", courseName=" + courseName + ", courseDescription="
                + courseDescription + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}
