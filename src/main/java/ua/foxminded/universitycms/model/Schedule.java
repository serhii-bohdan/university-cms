package ua.foxminded.universitycms.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The {@code Schedule} class represents a schedule in the system.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * schedule's ID and the study days in the schedule. It also includes methods to
 * get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<StudyDay> studyDays = new HashSet<>();

    /**
     * Constructs a new {@code Schedule} object with default values.
     */
    public Schedule() {
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Set<StudyDay> getStudyDays() {
        return Collections.unmodifiableSet(studyDays);
    }

    /**
     * Returns a hash code value for the schedule.
     *
     * @return a hash code value for this schedule
     */
    @Override
    public int hashCode() {
        return Objects.hash(scheduleId);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * schedule IDs.
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
        if (!(obj instanceof Schedule)) {
            return false;
        }
        Schedule other = (Schedule) obj;
        return Objects.equals(scheduleId, other.scheduleId);
    }

    /**
     * Returns a string representation of the schedule.
     *
     * @return a string representation of this schedule
     */
    @Override
    public String toString() {
        return "Schedule [scheduleId=" + scheduleId + "]";
    }

}
