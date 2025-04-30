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
 * Represents a course entity within the university management system.
 * <p>
 * This class extends {@link AbstractEntity} to inherit a unique identifier and defines attributes
 * specific to a course, such as its name, description, author, topics, enrolled students, lessons,
 * and timestamps for creation and updates. It is mapped to the {@code courses} table in the database
 * using JPA annotations. Instances of this class represent academic courses authored by a
 * {@link Teacher}, associated with {@link Topic} and {@link Lesson} entities, and enrolled by
 * {@link Student} entities, with lifecycle callbacks to manage timestamps automatically.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Teacher
 * @see Topic
 * @see Lesson
 * @see Student
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
     * The name of the course, uniquely identifying it within the system.
     * <p>
     * This field is mapped to the {@code course_name} column in the {@code courses} table.
     */
    @Column(name = "course_name")
    private String courseName;

    /**
     * A description of the course content and objectives.
     * <p>
     * This field is mapped to the {@code course_description} column in the {@code courses} table.
     */
    @Column(name = "course_description")
    private String courseDescription;

    /**
     * The teacher who authored and manages the course.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Teacher} entity and is mapped
     * to the {@code teacher_id} column in the {@code courses} table. The author is lazily fetched
     * ({@code FetchType.LAZY}) and must not be null, representing the course's creator and primary
     * instructor.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher author;

    /**
     * The set of topics covered by this course.
     * <p>
     * This field defines a one-to-many relationship with the {@link Topic} entity, mapped by the
     * {@code course} field in {@link Topic}. Topics are lazily fetched ({@code FetchType.LAZY}) and
     * managed with a cascading removal policy ({@code CascadeType.REMOVE}), meaning topics are deleted
     * when the course is removed. The set is initialized as an empty {@code HashSet}.
     */
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Topic> topics = new HashSet<>();

    /**
     * The set of students enrolled in this course.
     * <p>
     * This field establishes a many-to-many relationship with the {@link Student} entity, mapped by the
     * {@code courses} field in {@link Student} via the {@code students_courses} join table. Students
     * are lazily fetched ({@code FetchType.LAZY}), and the set is initialized as an empty
     * {@code HashSet}. Enrollment is managed using the {@link #addStudent(Student)} and
     * {@link #removeStudent(Student)} methods.
     */
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    /**
     * The set of lessons scheduled for this course.
     * <p>
     * This field defines a one-to-many relationship with the {@link Lesson} entity, mapped by the
     * {@code course} field in {@link Lesson}. Lessons are lazily fetched ({@code FetchType.LAZY}) and
     * managed with a cascading removal policy ({@code CascadeType.REMOVE}), meaning lessons are deleted
     * when the course is removed. The set is initialized as an empty {@code HashSet}.
     */
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Lesson> lessons = new HashSet<>();

    /**
     * The date and time when the course record was created, including time zone information.
     * <p>
     * This field is mapped to the {@code created_at} column in the {@code courses} table and is
     * automatically set by the {@link #onCreate()} lifecycle callback when the course is persisted.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the course record was last updated, including time zone information.
     * <p>
     * This field is mapped to the {@code updated_at} column in the {@code courses} table and is
     * automatically updated by the {@link #onUpdate()} lifecycle callback when the course is modified.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * Lifecycle callback invoked by JPA before the course is persisted.
     * <p>
     * Sets the {@code createdAt} field to the current timestamp when the course is first saved to the
     * database.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * Lifecycle callback invoked by JPA before the course is updated.
     * <p>
     * Sets the {@code updatedAt} field to the current timestamp when the course is modified in the
     * database.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

    /**
     * Enrolls a student in this course.
     * <p>
     * Adds the specified student to the {@code students} set and updates the student's {@code courses}
     * set by calling {@link Student#addCourse(Course)}, ensuring a bidirectional relationship between
     * the course and the student.
     *
     * @param student the {@link Student} to enroll in the course
     */
    public void addStudent(Student student) {
        this.students.add(student);
        student.addCourse(this);
    }

    /**
     * Removes a student from this course.
     * <p>
     * Removes the specified student from the {@code students} set and updates the student's
     * {@code courses} set by calling {@link Student#removeCourse(Course)}, ensuring a bidirectional
     * relationship between the course and the student.
     *
     * @param student the {@link Student} to unenroll from the course
     */
    public void removeStudent(Student student) {
        this.students.remove(student);
        student.removeCourse(this);
    }

}
