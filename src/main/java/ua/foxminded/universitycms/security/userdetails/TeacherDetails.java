package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that wraps a {@link Teacher} entity.
 *
 * <p>This class is used by Spring Security to represent the authenticated teacher user.
 * It provides the necessary details about the teacher, such as their authorities (permissions),
 * password, username (email), and other relevant information.
 *
 * @author Serhii Bohdan
 */
@RequiredArgsConstructor
public class TeacherDetails implements CustomUserDetails {

    /**
     * The underlying {@link Teacher} entity that this object represents.
     */
    private final Teacher teacher;

    /**
     * Retrieves the authorities (permissions) granted to this teacher user.
     * <p>
     * This implementation fetches the permissions associated with the teacher's role and converts them
     * into {@link SimpleGrantedAuthority} objects for use by Spring Security.
     *
     * @return A collection of granted authorities (permissions).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.teacher.getRole().getPermissions().stream()
            .map(p -> new SimpleGrantedAuthority(p.getPermissionName().name()))
            .collect(Collectors.toSet());
    }

    /**
     * Retrieves the hashed password of the teacher user.
     *
     * @return The hashed password.
     */
    @Override
    public String getPassword() {
        return this.teacher.getPasswordHash();
    }

    /**
     * Retrieves the username (email) of the teacher user.
     *
     * @return The teacher's email address.
     */
    @Override
    public String getUsername() {
        return this.teacher.getEmail();
    }

    /**
     * Retrieves the ID of the teacher user.
     *
     * @return The teacher's ID.
     */
    @Override
    public Long getId() {
        return this.teacher.getId();
    }

    /**
     * Retrieves the first name of the teacher user.
     *
     * @return The teacher's first name.
     */
    @Override
    public String getFirstName() {
        return teacher.getName().getFirstName();
    }

    /**
     * Retrieves the last name of the teacher user.
     *
     * @return The teacher's last name.
     */
    @Override
    public String getLastName() {
        return teacher.getName().getLastName();
    }

    /**
     * Retrieves the role name of the teacher user.
     *
     * @return The teacher's role name as a {@link RoleName} enum value.
     */
    @Override
    public RoleName getRoleName() {
        return this.teacher.getRole().getRoleName();
    }

}
