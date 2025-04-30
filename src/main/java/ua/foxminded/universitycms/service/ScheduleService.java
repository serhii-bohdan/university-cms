package ua.foxminded.universitycms.service;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;

/**
 * Service interface for managing {@link Schedule} entities and their DTO representations in the university management
 * system.
 * <p>
 * This interface extends the generic {@link Service} interface, specializing in operations for {@link Schedule}
 * entities mapped to {@link ScheduleDto} DTOs. It provides methods for CRUD operations inherited from {@link Service},
 * along with an additional method for retrieving a user's schedule based on their authentication details.
 * Implementations of this interface handle business logic related to schedule management, leveraging user-specific
 * data for personalized scheduling information.
 *
 * @author Serhii Bohdan
 * @see Service
 * @see Schedule
 * @see ScheduleDto
 * @see CustomUserDetails
 */
public interface ScheduleService extends Service<Schedule, ScheduleDto> {

    /**
     * Retrieves the schedule for a specific user.
     * <p>
     * Fetches the schedule associated with the user represented by {@link CustomUserDetails}, such as a student's
     * or teacher's timetable. The {@link NotNull} constraint ensures that user details are provided, enabling accurate
     * retrieval of the schedule based on the user's role and identity.
     *
     * @param customUserDetails the authenticated user's details, must be non-null
     * @return a {@link ScheduleDto} representing the user's schedule
     */
    ScheduleDto getScheduleForUser(@NotNull CustomUserDetails customUserDetails);

    /**
     * Retrieves the current local date based on the user's time zone offset.
     * <p>
     * This method calculates the local date for the specified user by applying their time zone offset,
     * obtained from the provided {@link CustomUserDetails}, to the current time.
     *
     * @param customUserDetails the user details containing the time zone offset, must not be null
     * @return the current local date as a {@code LocalDate} in the user's time zone
     */
    LocalDate getUserLocalDate(@NotNull CustomUserDetails customUserDetails);

    /**
     * Retrieves the current local date based on the provided time zone offset.
     * <p>
     * Calculates the local date by applying the specified time zone offset to the current time.
     * The {@link NotNull} constraint ensures that the offset is provided.
     *
     * @param userLocationZoneOffset the time zone offset (e.g., "+02:00" or "-05:00"), must not be null
     * @return the current local date as a {@code LocalDate} in the specified time zone
     */
    LocalDate getUserLocalDate(@NotNull String userLocationZoneOffset);

}
