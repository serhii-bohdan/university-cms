package ua.foxminded.universitycms.model;

import java.util.Collections;
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

/**
 * The {@code Student} class represents a student in the system.
 * <p>
 * This class is a subclass of the {@link User} class and includes additional
 * fields for the student's group, courses, schedule, and marks. It also
 * includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "students")
public class Student extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "students_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Mark> marks = new HashSet<>();

    /**
     * Constructs a new {@code Student} object with the given parameters.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param email     the email of the student
     * @param password  the password of the student
     * @param isActive  the activity status of the student
     * @param group     the group to which the student belongs
     * @param schedule  the student's schedule
     */
    public Student(String firstName, String lastName, String email, String password, Boolean isActive, Group group,
            Schedule schedule) {
        super(firstName, lastName, email, password, isActive);
        this.group = group;
        this.schedule = schedule;
    }

    /**
     * Constructs a new {@code Student} object with the given parameters, without a
     * schedule.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param email     the email of the student
     * @param password  the password of the student
     * @param isActive  the activity status of the student
     * @param group     the group to which the student belongs
     */
    public Student(String firstName, String lastName, String email, String password, Boolean isActive, Group group) {
        super(firstName, lastName, email, password, isActive);
        this.group = group;
    }

    /**
     * Constructs a new {@code Student} object with default values.
     */
    public Student() {
        super();
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<Course> getCourses() {
        return Collections.unmodifiableSet(courses);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Set<Mark> getMarks() {
        return Collections.unmodifiableSet(marks);
    }

}
