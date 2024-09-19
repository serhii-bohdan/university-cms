package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that wraps a {@link Manager} entity.
 *
 * <p>This class is used by Spring Security to represent the authenticated manager user. It provides the necessary
 * details about the manager, such as their authorities (permissions), password, username (email), and other relevant information.
 *
 * @author Serhii Bohdan
 */
@RequiredArgsConstructor
public class ManagerDetails implements CustomUserDetails {

    /**
     * The underlying {@link Manager} entity that this object represents.
     */
    private final Manager manager;

    /**
     * Retrieves the authorities (permissions) granted to this manager user.
     * <p>
     * This implementation fetches the permissions associated with the manager's role and converts them
     * into {@link SimpleGrantedAuthority} objects for use by Spring Security.
     *
     * @return A collection of granted authorities (permissions).
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
     * @return The hashed password.
     */
    @Override
    public String getPassword() {
        return this.manager.getPasswordHash();
    }

    /**
     * Retrieves the username (email) of the manager user.
     *
     * @return The manager's email address.
     */
    @Override
    public String getUsername() {
        return this.manager.getEmail();
    }

    /**
     * Retrieves the ID of the manager user.
     *
     * @return The manager's ID.
     */
    @Override
    public Long getId() {
        return this.manager.getId();
    }

    /**
     * Retrieves the first name of the manager user.
     *
     * @return The manager's first name.
     */
    @Override
    public String getFirstName() {
        return this.manager.getName().getFirstName();
    }

    /**
     * Retrieves the last name of the manager user.
     *
     * @return The manager's last name.
     */
    @Override
    public String getLastName() {
        return this.manager.getName().getLastName();
    }

    /**
     * Retrieves the role name of the manager user.
     *
     * @return The manager's role name.
     */
    @Override
    public RoleName getRoleName() {
        return this.manager.getRole().getRoleName();
    }

}
