package ua.foxminded.universitycms.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.enumeration.RoleName;

/**
 * Custom extension of the Spring Security {@link UserDetails} interface for enhanced user representation.
 * <p>
 * This interface augments the standard {@link UserDetails} contract by adding application-specific attributes,
 * such as the user's unique identifier, first name, last name, and role name, alongside core security details
 * like username, password, and authorities. It enables a richer representation of users within the university
 * management system's security context, supporting authentication and authorization processes.
 *
 * @author Serhii Bohdan
 * @see UserDetails
 * @see RoleName
 * @see FullName
 */
public interface CustomUserDetails extends UserDetails {

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the user's ID as a {@code Long} value
     */
    Long getId();

    /**
     * Retrieves the user's first name.
     *
     * @return the user's first name as a {@code String}
     */
    String getFirstName();

    /**
     * Retrieves the user's last name.
     *
     * @return the user's last name as a {@code String}
     */
    String getLastName();

    /**
     * Retrieves the user's time zone offset from UTC.
     *
     * @return the time zone offset as a {@code String}, e.g., "+02:00" or "-05:00"
     */
    String getLocationZoneOffset();

    /**
     * Retrieves the role name assigned to the user.
     *
     * @return the user's role as a {@link RoleName} enum value
     */
    RoleName getRoleName();

    /**
     * Sets the user's full name.
     * <p>
     * This method allows updating the user's name information using a {@link FullName} object, which
     * typically includes both first and last names.
     *
     * @param fullName the {@link FullName} object containing the user's first and last names
     */
    void setFullName(FullName fullName);

}
