package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that wraps an {@link Admin} entity.
 *
 * <p>This class is used by Spring Security to represent the authenticated admin user. It provides the necessary
 * details about the admin, such as their authorities (permissions), password, username (email), and other relevant information.
 *
 * @author Serhii Bohdan
 */
@RequiredArgsConstructor
public class AdminDetails implements CustomUserDetails {

    /**
     * The underlying {@link Admin} entity that this object represents.
     */
    private final Admin admin;

    /**
     * Retrieves the authorities (permissions) granted to this admin user.
     * <p>
     * This implementation fetches the permissions associated with the admin's role and converts them
     * into {@link SimpleGrantedAuthority} objects for use by Spring Security.
     *
     * @return A collection of granted authorities (permissions).
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
     * @return The hashed password.
     */
    @Override
    public String getPassword() {
        return this.admin.getPasswordHash();
    }

    /**
     * Retrieves the username (email) of the admin user.
     *
     * @return The admin's email address.
     */
    @Override
    public String getUsername() {
        return this.admin.getEmail();
    }

    /**
     * Retrieves the ID of the admin user.
     *
     * @return The admin's ID.
     */
    public Long getId() {
        return this.admin.getId();
    }

    /**
     * Retrieves the first name of the admin user.
     *
     * @return The admin's first name.
     */
    @Override
    public String getFirstName() {
        return this.admin.getName().getFirstName();
    }

    /**
     * Retrieves the last name of the admin user.
     *
     * @return The admin's last name.
     */
    @Override
    public String getLastName() {
        return this.admin.getName().getLastName();
    }

    /**
     * Retrieves the role name of the admin user.
     *
     * @return The admin's role name.
     */
    public RoleName getRoleName() {
        return this.admin.getRole().getRoleName();
    }

}
