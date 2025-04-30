package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that encapsulates a {@link Student} entity for Spring Security.
 * <p>
 * This class provides a Spring Security-compatible representation of an authenticated student user within the
 * university management system. It wraps a {@link Student} entity, exposing security-relevant details such as
 * authorities (permissions), hashed password, username (email), and additional user attributes like ID, full
 * name, and role name. The implementation leverages the student's role and permissions to define access rights.
 *
 * @author Serhii Bohdan
 * @see CustomUserDetails
 * @see Student
 * @see SimpleGrantedAuthority
 * @see RoleName
 * @see FullName
 */
@RequiredArgsConstructor
public class StudentDetails implements CustomUserDetails {

    /**
     * The underlying {@link Student} entity represented by this object.
     */
    private final Student student;

    /**
     * Retrieves the authorities granted to this student user.
     * <p>
     * This method extracts the permissions from the student's role and converts them into a collection of
     * {@link SimpleGrantedAuthority} objects, which Spring Security uses to determine the user's access rights.
     *
     * @return a collection of {@link GrantedAuthority} objects representing the student's permissions
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.student.getRole().getPermissions().stream()
            .map(p -> new SimpleGrantedAuthority(p.getPermissionName().name()))
            .collect(Collectors.toSet());
    }

    /**
     * Retrieves the hashed password of the student user.
     *
     * @return the student's hashed password as a {@code String}
     */
    @Override
    public String getPassword() {
        return this.student.getPasswordHash();
    }

    /**
     * Retrieves the username of the student user, represented by their email address.
     *
     * @return the student's email address as a {@code String}
     */
    @Override
    public String getUsername() {
        return this.student.getEmail();
    }

    /**
     * Retrieves the unique identifier of the student user.
     *
     * @return the student's ID as a {@code Long}
     */
    @Override
    public Long getId() {
        return this.student.getId();
    }

    /**
     * Retrieves the first name of the student user.
     *
     * @return the student's first name as a {@code String}
     */
    @Override
    public String getFirstName() {
        return this.student.getFullName().getFirstName();
    }

    /**
     * Retrieves the last name of the student user.
     *
     * @return the student's last name as a {@code String}
     */
    @Override
    public String getLastName() {
        return this.student.getFullName().getLastName();
    }

    /**
     * Retrieves the student user's time zone offset from UTC.
     *
     * @return the student's time zone offset as a {@code String}, e.g., "+02:00" or "-05:00"
     */
    @Override
    public String getLocationZoneOffset() {
        return student.getLocationZoneOffset();
    }

    /**
     * Retrieves the role name assigned to the student user.
     *
     * @return the student's role name as a {@link RoleName} enum value
     */
    @Override
    public RoleName getRoleName() {
        return this.student.getRole().getRoleName();
    }

    /**
     * Sets the full name of the student user.
     * <p>
     * Updates the {@link FullName} object of the underlying {@link Student} entity with the provided value.
     *
     * @param fullName the {@link FullName} object containing the student's first and last names
     */
    @Override
    public void setFullName(FullName fullName) {
        this.student.setFullName(fullName);
    }

}
