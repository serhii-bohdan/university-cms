package ua.foxminded.universitycms.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * The {@code Teacher} class represents a teacher in the system.
 * <p>
 * This class is a subclass of the {@link User} class and includes additional
 * fields for the teacher's courses and schedule. It also includes methods to
 * get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    /**
     * Constructs a new {@code Teacher} object with the given parameters.
     *
     * @param firstName the first name of the teacher
     * @param lastName  the last name of the teacher
     * @param email     the email of the teacher
     * @param password  the password of the teacher
     * @param isActive  the activity status of the teacher
     * @param schedule  the teacher's schedule
     */
    public Teacher(String firstName, String lastName, String email, String password, Boolean isActive,
            Schedule schedule) {
        super(firstName, lastName, email, password, isActive);
        this.schedule = schedule;
    }

    /**
     * Constructs a new {@code Teacher} object with the given parameters, without a
     * schedule.
     *
     * @param firstName the first name of the teacher
     * @param lastName  the last name of the teacher
     * @param email     the email of the teacher
     * @param password  the password of the teacher
     * @param isActive  the activity status of the teacher
     */
    public Teacher(String firstName, String lastName, String email, String password, Boolean isActive) {
        super(firstName, lastName, email, password, isActive);
    }

    /**
     * Constructs a new {@code Teacher} object with default values.
     */
    public Teacher() {
        super();
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

}
