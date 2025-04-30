package ua.foxminded.universitycms.model;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a scheduled lesson within a course and schedule in the university system.
 * Extends {@link AbstractEntity} to inherit an ID and defines lesson-specific attributes like
 * start/end times, time zone offset, and associations with a {@link Course} and {@link Schedule}.
 * Mapped to the {@code lessons} table via JPA annotations.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Course
 * @see Schedule
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"date", "lessonStartTime", "lessonEndTime", "zoneOffset", "schedule"})
@ToString(callSuper = true, exclude = {"course", "schedule"})
@SuperBuilder
@Entity
@Table(name = "lessons")
public class Lesson extends AbstractEntity {

    /**
     * The date of the lesson.
     * Mapped to the {@code date} column in the {@code lessons} table.
     */
    @Column(name = "date")
    private LocalDate date;

    /**
     * The start time of the lesson.
     * Mapped to the {@code lesson_start_time} column in the {@code lessons} table, indicating when
     * the lesson begins on the associated study day.
     */
    @Column(name = "lesson_start_time")
    private LocalTime lessonStartTime;

    /**
     * The end time of the lesson.
     * Mapped to the {@code lesson_end_time} column in the {@code lessons} table, indicating when
     * the lesson ends on the associated study day.
     */
    @Column(name = "lesson_end_time")
    private LocalTime lessonEndTime;

    /**
     * The time zone offset for the lesson.
     * Mapped to the {@code zone_offset} column in the {@code lessons} table, representing the UTC
     * offset (e.g., "+02:00") for the lesson's times.
     */
    @Column(name = "zone_offset")
    private String zoneOffset;

    /**
     * The course associated with this lesson.
     * Establishes a many-to-one relationship with {@link Course}, mapped to the {@code course_id}
     * column in the {@code lessons} table. Fetched lazily and required (non-null).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The schedule associated with this lesson.
     * Establishes a many-to-one relationship with {@link Schedule}, mapped to the
     * {@code schedule_id} column in the {@code lessons} table. Fetched lazily and required.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

}
