package ua.foxminded.universitycms.dto;

import java.time.ZonedDateTime;

/**
 * The {@code LessonDto} class is a data transfer object (DTO) for lesson
 * entities.
 * <p>
 * This class includes fields for lesson ID, lesson start time, lesson end time,
 * course ID, and study day ID. It also includes getter and setter methods for
 * these fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the lesson DTO.
 *
 * @author Serhii Bohdan
 */
public class LessonDto {

    private Long lessonId;
    private ZonedDateTime lessonStartTime;
    private ZonedDateTime lessonEndTime;
    private Long courseId;
    private Long studyDayId;

    /**
     * Constructs a new {@code LessonDto} with the specified lesson start time,
     * lesson end time, course ID, and study day ID.
     *
     * @param lessonStartTime the start time of the lesson
     * @param lessonEndTime   the end time of the lesson
     * @param courseId        the ID of the course that the lesson belongs to
     * @param studyDayId      the ID of the study day that the lesson belongs to
     */
    public LessonDto(ZonedDateTime lessonStartTime, ZonedDateTime lessonEndTime, Long courseId, Long studyDayId) {
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
        this.courseId = courseId;
        this.studyDayId = studyDayId;
    }

    /**
     * Constructs a new {@code LessonDto} with no initial values.
     */
    public LessonDto() {
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStudyDayId() {
        return studyDayId;
    }

    public void setStudyDayId(Long studyDayId) {
        this.studyDayId = studyDayId;
    }

    /**
     * Returns a string representation of the lesson DTO.
     *
     * @return a string representation of the lesson DTO
     */
    @Override
    public String toString() {
        return "LessonDto [lessonId=" + lessonId + ", lessonStartTime=" + lessonStartTime + ", lessonEndTime="
                + lessonEndTime + ", courseId=" + courseId + ", studyDayId=" + studyDayId + "]";
    }

}
