package ua.foxminded.universitycms.dto;

import java.time.LocalDate;
import java.util.Set;
import ua.foxminded.universitycms.model.enums.WeekDay;

/**
 * The {@code StudyDayDto} class is a data transfer object (DTO) for study day
 * entities.
 * <p>
 * This class includes fields for study day ID, date, week day, schedule ID, and
 * a set of lessons. It also includes getter and setter methods for these
 * fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the study day DTO.
 *
 * @author Serhii Bohdan
 */
public class StudyDayDto {

    private Long studyDayId;
    private LocalDate date;
    private WeekDay weekDay;
    private Long scheduleId;
    private Set<LessonDto> lessons;

    /**
     * Constructs a new {@code StudyDayDto} with the specified date, week day, and
     * schedule ID.
     *
     * @param date       the date of the study day
     * @param weekDay    the week day of the study day
     * @param scheduleId the ID of the schedule that the study day belongs to
     */
    public StudyDayDto(LocalDate date, WeekDay weekDay, Long scheduleId) {
        this.date = date;
        this.weekDay = weekDay;
        this.scheduleId = scheduleId;
    }

    /**
     * Constructs a new {@code StudyDayDto} with no initial values.
     */
    public StudyDayDto() {
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

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Set<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(Set<LessonDto> lessons) {
        this.lessons = lessons;
    }

    /**
     * Returns a string representation of the study day DTO.
     *
     * @return a string representation of the study day DTO
     */
    @Override
    public String toString() {
        return "StudyDayDto [studyDayId=" + studyDayId + ", date=" + date + ", weekDay=" + weekDay + ", scheduleId="
                + scheduleId + ", lessons=" + lessons + "]";
    }

}
