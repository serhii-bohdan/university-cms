package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that encapsulates an {@link Admin} entity for Spring Security.
 * <p>
 * This class provides a Spring Security-compatible representation of an authenticated admin user within the
 * university management system. It wraps an {@link Admin} entity, exposing security-relevant details such as
 * authorities (permissions), hashed password, username (email), and additional user attributes like ID, full
 * name, and role name. The implementation leverages the admin's role and permissions to define access rights.
 *
 * @author Serhii Bohdan
 * @see CustomUserDetails
 * @see Admin
 * @see SimpleGrantedAuthority
 * @see RoleName
 * @see FullName
 */
@RequiredArgsConstructor
public class AdminDetails implements CustomUserDetails {

    /**
     * The underlying {@link Admin} entity represented by this object.
     */
    private final Admin admin;

    /**
     * Retrieves the authorities granted to this admin user.
     * <p>
     * This method extracts the permissions from the admin's role and converts them into a collection of
     * {@link SimpleGrantedAuthority} objects, which Spring Security uses to determine the user's access rights.
     *
     * @return a collection of {@link GrantedAuthority} objects representing the admin's permissions
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.admin.getRole().getPermissions().stream()
            .map(p -> new SimpleGrantedAuthority(p.getPermissionName().name()))
            .collect(Collectors.toSet());
    }

    /**
     * Retrieves the hashed password of the admin user.
     *
     * @return the admin's hashed password as a {@code String}
     */
    @Override
    public String getPassword() {
        return this.admin.getPasswordHash();
    }

    /**
     * Retrieves the username of the admin user, represented by their email address.
     *
     * @return the admin's email address as a {@code String}
     */
    @Override
    public String getUsername() {
        return this.admin.getEmail();
    }

    /**
     * Retrieves the unique identifier of the admin user.
     *
     * @return the admin's ID as a {@code Long}
     */
    @Override
    public Long getId() {
        return this.admin.getId();
    }

    /**
     * Retrieves the first name of the admin user.
     *
     * @return the admin's first name as a {@code String}
     */
    @Override
    public String getFirstName() {
        return this.admin.getFullName().getFirstName();
    }

    /**
     * Retrieves the last name of the admin user.
     *
     * @return the admin's last name as a {@code String}
     */
    @Override
    public String getLastName() {
        return this.admin.getFullName().getLastName();
    }

    /**
     * Retrieves the admin user's time zone offset from UTC.
     *
     * @return the admin's time zone offset as a {@code String}, e.g., "+02:00" or "-05:00"
     */
    @Override
    public String getLocationZoneOffset() {
        return this.admin.getLocationZoneOffset();
    }

    /**
     * Retrieves the role name assigned to the admin user.
     *
     * @return the admin's role name as a {@link RoleName} enum value
     */
    @Override
    public RoleName getRoleName() {
        return this.admin.getRole().getRoleName();
    }

    /**
     * Sets the full name of the admin user.
     * <p>
     * Updates the {@link FullName} object of the underlying {@link Admin} entity with the provided value.
     *
     * @param fullName the {@link FullName} object containing the admin's first and last names
     */
    @Override
    public void setFullName(FullName fullName) {
        this.admin.setFullName(fullName);
    }

}
