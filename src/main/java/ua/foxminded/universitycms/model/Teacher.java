package ua.foxminded.universitycms.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The {@code Teacher} class represents a teacher in the system and inherits from the {@link User} class.
 * It extends the user information with additional fields specific to teachers.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"schedule", "courses"})
@SuperBuilder
@Entity
@Table(name = "teachers")
public class Teacher extends User {

    /**
     * The teacher's schedule.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    /**
     * The courses taught by the teacher.
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    /**
     * Constructs a new {@code Teacher} instance with a complete set of initial data,
     * including schedule information.
     *
     * @param name         the teacher's full name
     * @param email        the teacher's email address
     * @param passwordHash a securely hashed representation of the teacher's password
     * @param isActive     indicates whether the teacher's account is active
     * @param schedule     the teacher's schedule
     */
    public Teacher(Name name, String email, String passwordHash, Boolean isActive, Schedule schedule) {
        super(name, email, passwordHash, isActive);
        this.schedule = schedule;
    }

    /**
     * Constructs a new {@code Teacher} instance with basic teacher information,
     * without specifying a schedule.
     *
     * @param name         the teacher's full name
     * @param email        the teacher's email address
     * @param passwordHash a securely hashed representation of the teacher's password
     * @param isActive     indicates whether the teacher's account is active
     */
    public Teacher(Name name, String email, String passwordHash, Boolean isActive) {
        super(name, email, passwordHash, isActive);
    }

}
