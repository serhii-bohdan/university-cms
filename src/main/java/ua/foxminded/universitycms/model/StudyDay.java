package ua.foxminded.universitycms.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The {@code StudyDay} class represents a single day of study within a {@link Schedule}.
 * It holds information about the date, day of the week, associated schedule, and lessons
 * planned for that day. {@code StudyDay} inherits from the {@link AbstractEntity} class.
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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"date", "schedule"})
@ToString(callSuper = true, exclude = {"schedule", "lessons"})
@SuperBuilder
@Entity
@Table(name = "study_days")
public class StudyDay extends AbstractEntity {

    /**
     * The date of the study day.
     */
    @Column(name = "day_date")
    private LocalDate date;

    /**
     * The day of the week of the study day (e.g., Monday, Tuesday, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    private DayOfWeek weekDay;

    /**
     * The {@link Schedule} that this study day belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    /**
     * The collection of {@link Lesson} objects scheduled for this study day.
     */
    @OneToMany(mappedBy = "studyDay", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Lesson> lessons = new HashSet<>();

}
