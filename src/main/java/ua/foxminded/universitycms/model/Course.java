package ua.foxminded.universitycms.model;

import java.time.ZonedDateTime;
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
@EqualsAndHashCode(of = {"courseName", "courseDescription"})
@ToString(callSuper = true, exclude = {"author", "topics", "lessons", "students"})
@SuperBuilder
@Entity
@Table(name = "courses")
public class Course extends AbstractEntity {

    /**
     * The name of the course.
     */
    @Column(name = "course_name")
    private String courseName;

    /**
     * A description of the course content.
     */
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
     * The date and time when the course record was created in the database,
     * including time zone information.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the course record was last updated in the database,
     * including time zone information.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * Sets the {@code createdAt} field to the current time when the course is
     * created.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * Sets the {@code updatedAt} field to the current time when the course is
     * updated.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

    /**
     * Enrolls a student in this course.
     * <p>
     * This method adds the specified student to the course's `students` set
     * and also calls the `addCourse` method of the student to add this course
     * to the student's `courses` set. This ensures a bidirectional relationship
     * is maintained between courses and students.
     *
     * @param student The {@link Student} object to be enrolled.
     */
    public void addStudent(Student student) {
        this.students.add(student);
        student.addCourse(this);
    }

    /**
     * Removes a student from this course.
     * <p>
     * This method removes the specified student from the course's `students` set
     * and also calls the `removeCourse` method of the student to remove this course
     * from the student's `courses` set. This ensures a bidirectional relationship
     * is maintained between courses and students.
     *
     * @param student The {@link Student} object to be removed.
     */
    public void removeStudent(Student student) {
        this.students.remove(student);
        student.removeCourse(this);
    }

}
