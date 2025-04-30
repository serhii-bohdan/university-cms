package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that encapsulates a {@link Teacher} entity for Spring Security.
 * <p>
 * This class provides a Spring Security-compatible representation of an authenticated teacher user within the
 * university management system. It wraps a {@link Teacher} entity, exposing security-relevant details such as
 * authorities (permissions), hashed password, username (email), and additional user attributes like ID, full
 * name, and role name. The implementation leverages the teacher's role and permissions to define access rights.
 *
 * @author Serhii Bohdan
 * @see CustomUserDetails
 * @see Teacher
 * @see SimpleGrantedAuthority
 * @see RoleName
 * @see FullName
 */
@RequiredArgsConstructor
public class TeacherDetails implements CustomUserDetails {

    /**
     * The underlying {@link Teacher} entity represented by this object.
     */
    private final Teacher teacher;

    /**
     * Retrieves the authorities granted to this teacher user.
     * <p>
     * This method extracts the permissions from the teacher's role and converts them into a collection of
     * {@link SimpleGrantedAuthority} objects, which Spring Security uses to determine the user's access rights.
     *
     * @return a collection of {@link GrantedAuthority} objects representing the teacher's permissions
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
     * @return the teacher's hashed password as a {@code String}
     */
    @Override
    public String getPassword() {
        return this.teacher.getPasswordHash();
    }

    /**
     * Retrieves the username of the teacher user, represented by their email address.
     *
     * @return the teacher's email address as a {@code String}
     */
    @Override
    public String getUsername() {
        return this.teacher.getEmail();
    }

    /**
     * Retrieves the unique identifier of the teacher user.
     *
     * @return the teacher's ID as a {@code Long}
     */
    @Override
    public Long getId() {
        return this.teacher.getId();
    }

    /**
     * Retrieves the first name of the teacher user.
     *
     * @return the teacher's first name as a {@code String}
     */
    @Override
    public String getFirstName() {
        return this.teacher.getFullName().getFirstName();
    }

    /**
     * Retrieves the last name of the teacher user.
     *
     * @return the teacher's last name as a {@code String}
     */
    @Override
    public String getLastName() {
        return this.teacher.getFullName().getLastName();
    }

    /**
     * Retrieves the teacher user's time zone offset from UTC.
     *
     * @return the teacher's time zone offset as a {@code String}, e.g., "+02:00" or "-05:00"
     */
    @Override
    public String getLocationZoneOffset() {
        return this.teacher.getLocationZoneOffset();
    }

    /**
     * Retrieves the role name assigned to the teacher user.
     *
     * @return the teacher's role name as a {@link RoleName} enum value
     */
    @Override
    public RoleName getRoleName() {
        return this.teacher.getRole().getRoleName();
    }

    /**
     * Sets the full name of the teacher user.
     * <p>
     * Updates the {@link FullName} object of the underlying {@link Teacher} entity with the provided value.
     *
     * @param fullName the {@link FullName} object containing the teacher's first and last names
     */
    @Override
    public void setFullName(FullName fullName) {
        this.teacher.setFullName(fullName);
    }

}
