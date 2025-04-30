package ua.foxminded.universitycms.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a student entity within the university management system.
 * <p>
 * This class extends {@link Educator} to inherit common educator properties and adds attributes
 * specific to students, such as their role, group, schedule, enrolled courses, and marks. It is
 * mapped to the {@code students} table in the database using JPA annotations. Instances of this class
 * represent individual students with permissions defined by their {@link Role}, membership in a
 * {@link Group}, a personal {@link Schedule}, and associations with {@link Course} and {@link Mark}
 * entities.
 *
 * @author Serhii Bohdan
 * @see Educator
 * @see Role
 * @see Group
 * @see Schedule
 * @see Course
 * @see Mark
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"group", "schedule", "courses", "marks"})
@SuperBuilder
@Entity
@Table(name = "students")
public class Student extends Educator {

    /**
     * The role assigned to the student, defining their permissions within the system.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Role} entity and is mapped to
     * the {@code role_id} column in the {@code students} table. The role is eagerly fetched
     * ({@code FetchType.EAGER}) and must not be null, determining the student's access rights and
     * capabilities.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * The group to which the student belongs.
     * <p>
     * This field defines a many-to-one relationship with the {@link Group} entity and is mapped to the
     * {@code group_id} column in the {@code students} table. The group is lazily fetched
     * ({@code FetchType.LAZY}) and must not be null, representing the student's academic cohort.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    /**
     * The student's personal schedule.
     * <p>
     * This field establishes a one-to-one relationship with the {@link Schedule} entity, mapped to the
     * {@code schedule_id} column in the {@code students} table. The schedule is lazily fetched
     * ({@code FetchType.LAZY}) and managed with a cascading policy ({@code CascadeType.ALL}), meaning
     * all operations on the student entity will propagate to its schedule.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    /**
     * The set of courses in which the student is enrolled.
     * <p>
     * This field defines a many-to-many relationship with the {@link Course} entity, mapped through the
     * {@code students_courses} join table. Courses are lazily fetched ({@code FetchType.LAZY}) and
     * stored in a {@code HashSet} initialized as empty. Students can add or remove courses using the
     * provided {@link #addCourse(Course)} and {@link #removeCourse(Course)} methods.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "students_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    /**
     * The set of marks (grades) received by the student.
     * <p>
     * This field establishes a one-to-many relationship with the {@link Mark} entity, where the student
     * is referenced as the recipient of the mark. It is mapped by the {@code student} field in
     * {@link Mark}, lazily fetched ({@code FetchType.LAZY}), and managed with a cascading policy
     * ({@code CascadeType.ALL}), meaning all operations on the student entity will propagate to its
     * marks. The set is initialized as an empty {@code HashSet}.
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Mark> marks = new HashSet<>();

    /**
     * Adds a course to the student's set of enrolled courses.
     * <p>
     * This method appends the specified course to the {@code courses} set, enrolling the student in it.
     *
     * @param course the {@link Course} to enroll the student in
     */
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    /**
     * Removes a course from the student's set of enrolled courses.
     * <p>
     * This method removes the specified course from the {@code courses} set, unenrolling the student
     * from it.
     *
     * @param course the {@link Course} to unenroll the student from
     */
    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

}
