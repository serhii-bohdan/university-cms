package ua.foxminded.universitycms.dto;

import java.util.Set;

/**
 * The {@code ScheduleDto} class is a data transfer object (DTO) for schedule
 * entities.
 * <p>
 * This class includes fields for schedule ID and a set of study days. It also
 * includes getter and setter methods for these fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the schedule DTO.
 *
 * @author Serhii Bohdan
 */
public class ScheduleDto {

    private Long scheduleId;
    private Set<StudyDayDto> studyDays;

    /**
     * Constructs a new {@code ScheduleDto} with no initial values.
     */
    public ScheduleDto() {
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Set<StudyDayDto> getStudyDays() {
        return studyDays;
    }

    public void setStudyDays(Set<StudyDayDto> studyDays) {
        this.studyDays = studyDays;
    }

    /**
     * Returns a string representation of the schedule DTO.
     *
     * @return a string representation of the schedule DTO
     */
    @Override
    public String toString() {
        return "ScheduleDto [scheduleId=" + scheduleId + ", studyDays=" + studyDays + "]";
    }

}
