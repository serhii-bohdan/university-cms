package ua.foxminded.universitycms.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import ua.foxminded.universitycms.model.enumeration.RoleName;

/**
 * Custom extension of the Spring Security {@link UserDetails} interface.
 *
 * <p>This interface provides additional attributes relevant to the application's users,
 * such as the user's ID, first name, last name, and role name, along with the standard
 * Spring Security user details like username, password, and authorities.
 *
 * @author Serhii Bohdan
 */
public interface CustomUserDetails extends UserDetails {

    /**
     * Returns the unique identifier (ID) of the user.
     *
     * @return the user's ID
     */
    Long getId();

    /**
     * Returns the user's first name.
     *
     * @return the user's first name
     */
    String getFirstName();

    /**
     * Returns the user's last name.
     *
     * @return the user's last name
     */
    String getLastName();

    /**
     * Returns the name of the role associated with the user.
     *
     * @return the user's role name as a {@link RoleName} enum value
     */
    RoleName getRoleName();

}
