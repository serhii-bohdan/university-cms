package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.MarkDto;

/**
 * The {@code MarkService} interface provides methods for managing marks.
 * <p>
 * This interface includes methods for adding a mark, getting a mark by ID, and
 * deleting a mark by ID.
 *
 * @author Serhii Bohdan
 */
public interface MarkService {

    /**
     * Adds a new mark.
     *
     * @param markDto the mark DTO to add
     * @return true if the mark was added successfully, false otherwise
     */
    boolean addMark(MarkDto markDto);

    /**
     * Gets a mark by ID.
     *
     * @param markId the ID of the mark to get
     * @return an Optional containing the mark DTO if found, an empty Optional
     *         otherwise
     */
    Optional<MarkDto> getMarkById(Long markId);

    /**
     * Deletes a mark by ID.
     *
     * @param markId the ID of the mark to delete
     * @return true if the mark was deleted successfully, false otherwise
     */
    boolean deleteMarkById(Long markId);

}
