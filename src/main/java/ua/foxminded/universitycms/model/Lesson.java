package ua.foxminded.universitycms.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The {@code Lesson} class represents a lesson in the system.
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
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "lesson_start_time")
    private ZonedDateTime lessonStartTime;

    @Column(name = "lesson_end_time")
    private ZonedDateTime lessonEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_day_id", nullable = false)
    private StudyDay studyDay;

    /**
     * Constructs a new {@code Lesson} object with the given parameters.
     *
     * @param lessonStartTime the start time of the lesson
     * @param lessonEndTime   the end time of the lesson
     * @param course          the course that the lesson belongs to
     * @param studyDay        the study day that the lesson is part of
     */
    public Lesson(ZonedDateTime lessonStartTime, ZonedDateTime lessonEndTime, Course course, StudyDay studyDay) {
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
        this.course = course;
        this.studyDay = studyDay;
    }

    /**
     * Constructs a new {@code Lesson} object with default values.
     */
    public Lesson() {
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public ZonedDateTime getLessonStartTime() {
        return lessonStartTime;
    }

    public void setLessonStartTime(ZonedDateTime lessonStartTime) {
        this.lessonStartTime = lessonStartTime;
    }

    public ZonedDateTime getLessonEndTime() {
        return lessonEndTime;
    }

    public void setLessonEndTime(ZonedDateTime lessonEndTime) {
        this.lessonEndTime = lessonEndTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public StudyDay getStudyDay() {
        return studyDay;
    }

    public void setStudyDay(StudyDay studyDay) {
        this.studyDay = studyDay;
    }

    /**
     * Returns a hash code value for the lesson.
     *
     * @return a hash code value for this lesson
     */
    @Override
    public int hashCode() {
        return Objects.hash(course, lessonEndTime, lessonStartTime, studyDay);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * course, lesson end time, lesson start time, and study day.
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
        if (!(obj instanceof Lesson)) {
            return false;
        }
        Lesson other = (Lesson) obj;
        return Objects.equals(course, other.course) && Objects.equals(lessonEndTime, other.lessonEndTime)
                && Objects.equals(lessonStartTime, other.lessonStartTime) && Objects.equals(studyDay, other.studyDay);
    }

    /**
     * Returns a string representation of the lesson.
     *
     * @return a string representation of this lesson
     */
    @Override
    public String toString() {
        return "Lesson [lessonId=" + lessonId + ", lessonStartTime=" + lessonStartTime + ", lessonEndTime="
                + lessonEndTime + ", course=" + course + ", studyDay=" + studyDay + "]";
    }

}
