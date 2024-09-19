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
 * The {@code Student} class represents a student in the system.
 * <p>
 * This class is a subclass of the {@link User} class and includes additional
 * fields for the student's group, courses, schedule, and marks. It also
 * includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"group", "schedule", "courses", "marks"})
@SuperBuilder
@Entity
@Table(name = "students")
public class Student extends User {

    /**
     * The role assigned to the student. This role defines the student's permissions
     * and access levels within the system.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * The student's group.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    /**
     * The student's schedule.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    /**
     * The courses enrolled by the student.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "students_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    /**
     * The marks (grades) received by the student for various courses.
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Mark> marks = new HashSet<>();

    /**
     * Constructs a new Student with complete initial data, including group and schedule.
     *
     * @param name         The student's full name.
     * @param email        The student's email address.
     * @param passwordHash The hashed representation of the student's password.
     * @param role         The role assigned to the student.
     * @param isActive     Whether the student's account is active.
     * @param group        The group to which the student belongs.
     * @param schedule     The student's personal schedule.
     */
    public Student(Name name, String email, String passwordHash, Role role, Boolean isActive, Group group, Schedule schedule) {
        super(name, email, passwordHash, isActive);
        this.role = role;
        this.group = group;
        this.schedule = schedule;
    }

    /**
     * Constructs a new Student with basic information, without a schedule.
     *
     * @param name         The student's full name.
     * @param email        The student's email address.
     * @param passwordHash The hashed representation of the student's password.
     * @param role         The role assigned to the student.
     * @param isActive     Whether the student's account is active.
     * @param group        The group to which the student belongs.
     */
    public Student(Name name, String email, String passwordHash, Role role, Boolean isActive, Group group) {
        super(name, email, passwordHash, isActive);
        this.role = role;
        this.group = group;
    }

}
