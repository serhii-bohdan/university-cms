package ua.foxminded.universitycms.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ua.foxminded.universitycms.model.enums.WeekDay;

/**
 * The {@code StudyDay} class represents a study day in the system.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * study day's ID, date, day of the week, the schedule it belongs to, and the
 * lessons on that day. It also includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "study_days")
public class StudyDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_day_id")
    private Long studyDayId;

    @Column(name = "day_date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    private WeekDay weekDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToMany(mappedBy = "studyDay", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Lesson> lessons = new HashSet<>();

    /**
     * Constructs a new {@code StudyDay} object with the given parameters.
     *
     * @param date     the date of the study day
     * @param weekDay  the day of the week of the study day
     * @param schedule the schedule that the study day belongs to
     */
    public StudyDay(LocalDate date, WeekDay weekDay, Schedule schedule) {
        this.date = date;
        this.weekDay = weekDay;
        this.schedule = schedule;
    }

    /**
     * Constructs a new {@code StudyDay} object with default values.
     */
    public StudyDay() {
    }

    public Long getStudyDayId() {
        return studyDayId;
    }

    public void setStudyDayId(Long studyDayId) {
        this.studyDayId = studyDayId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(lessons);
    }

    /**
     * Returns a hash code value for the study day.
     *
     * @return a hash code value for this study day
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, schedule, weekDay);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * date, schedule, and day of the week.
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
        if (!(obj instanceof StudyDay)) {
            return false;
        }
        StudyDay other = (StudyDay) obj;
        return Objects.equals(date, other.date) && Objects.equals(schedule, other.schedule) && weekDay == other.weekDay;
    }

    /**
     * Returns a string representation of the study day.
     *
     * @return a string representation of this study day
     */
    @Override
    public String toString() {
        return "StudyDay [studyDayId=" + studyDayId + ", date=" + date + ", weekDay=" + weekDay + ", schedule="
                + schedule + "]";
    }

}
