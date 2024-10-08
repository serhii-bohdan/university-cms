package ua.foxminded.universitycms.model;

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
 * The {@code Lesson} class represents a scheduled lesson within a {@link StudyDay}
 * and a {@link Course}. {@code Lesson} inherits from the {@link AbstractEntity} class.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * lesson's ID, start time, end time, the course it belongs to, and the study
 * day it is part of. It also includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"lessonStartTime", "lessonEndTime", "timezone", "studyDay"})
@ToString(callSuper = true, exclude = {"course", "studyDay"})
@SuperBuilder
@Entity
@Table(name = "lessons")
public class Lesson extends AbstractEntity {

    /**
     * The start time of the lesson.
     */
    @Column(name = "lesson_start_time")
    private LocalTime lessonStartTime;

    /**
     * The end time of the lesson.
     */
    @Column(name = "lesson_end_time")
    private LocalTime lessonEndTime;

    /**
     * The timezone in which the lesson times are specified.
     */
    @Column(name = "timezone")
    private String timezone;

    /**
     * The {@link Course} that this lesson belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The {@link StudyDay} that this lesson is part of.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_day_id", nullable = false)
    private StudyDay studyDay;

}
