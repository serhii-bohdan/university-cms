package ua.foxminded.universitycms.service;

import jakarta.validation.constraints.NotNull;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;

/**
 * Service interface for managing operations related to authorized users in the university management system.
 * <p>
 * This interface defines methods for retrieving and updating information about authenticated users based on their
 * {@link CustomUserDetails}. It provides functionality to fetch a user's DTO representation, update their password,
 * and modify their full name, leveraging security context details for user identification and authorization.
 * Implementations of this interface handle business logic specific to authenticated user management, ensuring data
 * integrity through validation constraints.
 *
 * @author Serhii Bohdan
 * @see CustomUserDetails
 * @see UserDto
 * @see PasswordUpdateRequestDto
 * @see FullName
 */
public interface AuthorizedUserService {

    /**
     * Retrieves a user's DTO representation based on their authentication details.
     * <p>
     * Fetches the user data associated with the provided {@link CustomUserDetails}, returning it as a {@link UserDto}.
     * The {@link NotNull} constraint ensures that user details are provided for accurate identification.
     *
     * @param customUserDetails the authenticated user's details, must be non-null
     * @return a {@link UserDto} representing the user's data
     */
    UserDto findUserByUserDetails(@NotNull CustomUserDetails customUserDetails);

    /**
     * Updates a user's password based on the provided request.
     * <p>
     * Modifies the password of the user identified in the {@link PasswordUpdateRequestDto}, ensuring the request
     * meets validation criteria. The {@link NotNull} constraint ensures that the request DTO is provided.
     *
     * @param passwordUpdateRequestDto the {@link PasswordUpdateRequestDto} containing the password update details,
     *                                 must be non-null
     */
    void updateUserPassword(@NotNull PasswordUpdateRequestDto passwordUpdateRequestDto);

    /**
     * Updates a user's full name based on their authentication details.
     * <p>
     * Changes the full name of the user identified by {@link CustomUserDetails} to the provided {@link FullName}
     * object. Both parameters are constrained by {@link NotNull} to ensure valid user identification and data for
     * the update.
     *
     * @param customUserDetails the authenticated user's details, must be non-null
     * @param fullName          the new {@link FullName} object containing the updated first and last names, must
     *                          be non-null
     */
    void updateFullNameByUserDetails(@NotNull CustomUserDetails customUserDetails, @NotNull FullName fullName);

}
