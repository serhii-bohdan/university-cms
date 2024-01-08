package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.StudyDayDto;

/**
 * The {@code StudyDayService} interface provides methods for managing study
 * days.
 * <p>
 * This interface includes methods for adding a study day, getting a study day
 * by ID, and deleting a study day by ID.
 *
 * @author Serhii Bohdan
 */
public interface StudyDayService {

    /**
     * Adds a new study day.
     *
     * @param studyDayDto the study day DTO to add
     * @return true if the study day was added successfully, false otherwise
     */
    boolean addStudyDay(StudyDayDto studyDayDto);

    /**
     * Gets a study day by ID.
     *
     * @param studyDayId the ID of the study day to get
     * @return an Optional containing the study day DTO if found, an empty Optional
     *         otherwise
     */
    Optional<StudyDayDto> getStudyDayById(Long studyDayId);

    /**
     * Deletes a study day by ID.
     *
     * @param studyDayId the ID of the study day to delete
     * @return true if the study day was deleted successfully, false otherwise
     */
    boolean deleteStudyDayById(Long studyDayId);

}
