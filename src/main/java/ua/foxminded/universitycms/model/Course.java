package ua.foxminded.universitycms.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The {@code Course} class represents a course in the system and inherits
 * from the {@link AbstractEntity} class.
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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = {"author", "topics", "lessons", "students"})
@SuperBuilder
@Entity
@Table(name = "courses")
public class Course extends AbstractEntity {

    /**
     * The name of the course.
     */
    @EqualsAndHashCode.Include
    @Column(name = "course_name")
    private String courseName;

    /**
     * A description of the course content.
     */
    @EqualsAndHashCode.Include
    @Column(name = "course_description")
    private String courseDescription;

    /**
     * The teacher who authored (created) the course.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher author;

    /**
     * The topics covered in this course.
     */
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Topic> topics = new HashSet<>();

    /**
     * The students enrolled in this course.
     */
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    /**
     * The lessons associated with this course.
     */
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Lesson> lessons = new HashSet<>();

    /**
     * The date and time the course record was created in the system.
     * This field is automatically populated with the current timestamp before persisting the entity.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * The date and time the course record was last updated in the system.
     * This field is automatically populated with the current timestamp before updating the entity.
     */
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
     * Sets the {@code createdAt} field to the current time when the course is
     * created.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(LocalDateTime.now());
    }

    /**
     * Sets the {@code updatedAt} field to the current time when the course is
     * updated.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }

}
