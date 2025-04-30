package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that encapsulates a {@link Manager} entity for Spring Security.
 * <p>
 * This class provides a Spring Security-compatible representation of an authenticated manager user within the
 * university management system. It wraps a {@link Manager} entity, exposing security-relevant details such as
 * authorities (permissions), hashed password, username (email), and additional user attributes like ID, full
 * name, and role name. The implementation leverages the manager's role and permissions to define access rights.
 *
 * @author Serhii Bohdan
 * @see CustomUserDetails
 * @see Manager
 * @see SimpleGrantedAuthority
 * @see RoleName
 * @see FullName
 */
@RequiredArgsConstructor
public class ManagerDetails implements CustomUserDetails {

    /**
     * The underlying {@link Manager} entity represented by this object.
     */
    private final Manager manager;

    /**
     * Retrieves the authorities granted to this manager user.
     * <p>
     * This method extracts the permissions from the manager's role and converts them into a collection of
     * {@link SimpleGrantedAuthority} objects, which Spring Security uses to determine the user's access rights.
     *
     * @return a collection of {@link GrantedAuthority} objects representing the manager's permissions
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.manager.getRole().getPermissions().stream()
            .map(p -> new SimpleGrantedAuthority(p.getPermissionName().name()))
            .collect(Collectors.toSet());
    }

    /**
     * Retrieves the hashed password of the manager user.
     *
     * @return the manager's hashed password as a {@code String}
     */
    @Override
    public String getPassword() {
        return this.manager.getPasswordHash();
    }

    /**
     * Retrieves the username of the manager user, represented by their email address.
     *
     * @return the manager's email address as a {@code String}
     */
    @Override
    public String getUsername() {
        return this.manager.getEmail();
    }

    /**
     * Retrieves the unique identifier of the manager user.
     *
     * @return the manager's ID as a {@code Long}
     */
    @Override
    public Long getId() {
        return this.manager.getId();
    }

    /**
     * Retrieves the first name of the manager user.
     *
     * @return the manager's first name as a {@code String}
     */
    @Override
    public String getFirstName() {
        return this.manager.getFullName().getFirstName();
    }

    /**
     * Retrieves the last name of the manager user.
     *
     * @return the manager's last name as a {@code String}
     */
    @Override
    public String getLastName() {
        return this.manager.getFullName().getLastName();
    }

    /**
     * Retrieves the manager user's time zone offset from UTC.
     *
     * @return the manager's time zone offset as a {@code String}, e.g., "+02:00" or "-05:00"
     */
    @Override
    public String getLocationZoneOffset() {
        return this.manager.getLocationZoneOffset();
    }

    /**
     * Retrieves the role name assigned to the manager user.
     *
     * @return the manager's role name as a {@link RoleName} enum value
     */
    @Override
    public RoleName getRoleName() {
        return this.manager.getRole().getRoleName();
    }

    /**
     * Sets the full name of the manager user.
     * <p>
     * Updates the {@link FullName} object of the underlying {@link Manager} entity with the provided value.
     *
     * @param fullName the {@link FullName} object containing the manager's first and last names
     */
    @Override
    public void setFullName(FullName fullName) {
        this.manager.setFullName(fullName);
    }

}
