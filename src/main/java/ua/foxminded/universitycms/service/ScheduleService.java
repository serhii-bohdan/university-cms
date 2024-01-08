package ua.foxminded.universitycms.service;

import java.util.Optional;
import ua.foxminded.universitycms.dto.ScheduleDto;

/**
 * The {@code ScheduleService} interface provides methods for managing
 * schedules.
 * <p>
 * This interface includes methods for adding a schedule and getting a schedule
 * by ID.
 *
 * @author Serhii Bohdan
 */
public interface ScheduleService {

    /**
     * Adds a new schedule.
     *
     * @param scheduleDto the schedule DTO to add
     * @return true if the schedule was added successfully, false otherwise
     */
    boolean addSchedule(ScheduleDto scheduleDto);

    /**
     * Gets a schedule by ID.
     *
     * @param scheduleId the ID of the schedule to get
     * @return an Optional containing the schedule DTO if found, an empty Optional
     *         otherwise
     */
    Optional<ScheduleDto> getScheduleById(Long scheduleId);

}
