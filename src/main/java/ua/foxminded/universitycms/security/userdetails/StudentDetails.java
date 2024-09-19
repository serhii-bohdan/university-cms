package ua.foxminded.universitycms.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CustomUserDetails} that wraps a {@link Student} entity.
 *
 * <p>This class is used by Spring Security to represent the authenticated student user. It provides the necessary
 * details about the student, such as their authorities (permissions), password, username (email), and other relevant information.
 *
 * @author Serhii Bohdan
 */
@RequiredArgsConstructor
public class StudentDetails implements CustomUserDetails {

    /**
     * The underlying {@link Student} entity that this object represents.
     */
    private final Student student;

    /**
     * Retrieves the authorities (permissions) granted to this student user.
     * <p>
     * This implementation fetches the permissions associated with the student's role and converts them
     * into {@link SimpleGrantedAuthority} objects for use by Spring Security.
     *
     * @return A collection of granted authorities (permissions).
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
     * @return The hashed password.
     */
    @Override
    public String getPassword() {
        return this.student.getPasswordHash();
    }

    /**
     * Retrieves the username (email) of the student user.
     *
     * @return The student's email address.
     */
    @Override
    public String getUsername() {
        return this.student.getEmail();
    }

    /**
     * Retrieves the ID of the student user.
     *
     * @return The student's ID.
     */
    @Override
    public Long getId() {
        return this.student.getId();
    }

    /**
     * Retrieves the first name of the student user.
     *
     * @return The student's first name.
     */
    @Override
    public String getFirstName() {
        return this.student.getName().getFirstName();
    }

    /**
     * Retrieves the last name of the student user.
     *
     * @return The student's last name.
     */
    @Override
    public String getLastName() {
        return this.student.getName().getLastName();
    }

    /**
     * Retrieves the role name of the student user.
     *
     * @return The student's role name.
     */
    @Override
    public RoleName getRoleName() {
        return this.student.getRole().getRoleName();
    }

}
