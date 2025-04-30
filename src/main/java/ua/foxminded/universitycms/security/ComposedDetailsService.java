package ua.foxminded.universitycms.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.AdminRepository;
import ua.foxminded.universitycms.repository.ManagerRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.security.userdetails.AdminDetails;
import ua.foxminded.universitycms.security.userdetails.ManagerDetails;
import ua.foxminded.universitycms.security.userdetails.StudentDetails;
import ua.foxminded.universitycms.security.userdetails.TeacherDetails;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService} for multi-type user authentication.
 * <p>
 * This service provides a flexible and scalable solution for loading user details in the university management
 * system. It supports authentication for multiple user types ({@link Admin}, {@link Manager}, {@link Teacher},
 * {@link Student}) by delegating to specific repositories based on the user's email (username). A map of user
 * type loaders enables dynamic lookup and easy extension for additional user types in the future. The
 * {@code @Service} annotation registers this class as a Spring-managed bean.
 *
 * @author Serhii Bohdan
 * @see UserDetailsService
 * @see AdminDetails
 * @see ManagerDetails
 * @see TeacherDetails
 * @see StudentDetails
 */
@Service
public class ComposedDetailsService implements UserDetailsService {

    /**
     * Flag indicating that only activated users should be retrieved during authentication.
     */
    private static final Boolean USER_ACTIVATED = true;

    /**
     * Error message template used when a user with the specified email cannot be found.
     */
    private static final String USER_NOT_FOUND_MESSAGE = "User not found with email: %s.";

    /**
     * Map associating user classes with functions for loading their corresponding {@link UserDetails}.
     * <p>
     * Each entry maps a user type ({@link Admin}, {@link Manager}, {@link Teacher}, {@link Student}) to a
     * function that queries the appropriate repository using the provided email and returns an
     * {@link Optional} of the corresponding {@link UserDetails} implementation.
     */
    private final Map<Class<?>, Function<String, Optional<? extends UserDetails>>> userLoaders;

    /**
     * Constructs a new {@code ComposedDetailsService} instance with the specified repositories.
     * <p>
     * Initializes the {@code userLoaders} map with functions that fetch user entities from their respective
     * repositories and wrap them in appropriate {@link UserDetails} implementations (e.g., {@link AdminDetails},
     * {@link ManagerDetails}). The map supports extensibility for additional user types.
     *
     * @param adminRepository   the repository for {@link Admin} entities
     * @param managerRepository the repository for {@link Manager} entities
     * @param teacherRepository the repository for {@link Teacher} entities
     * @param studentRepository the repository for {@link Student} entities
     */
    public ComposedDetailsService(AdminRepository adminRepository, ManagerRepository managerRepository,
                                  TeacherRepository teacherRepository, StudentRepository studentRepository) {
        userLoaders = Map.of(
            Student.class, username -> studentRepository.findByEmailAndIsActive(username, USER_ACTIVATED).map(StudentDetails::new),
            Teacher.class, username -> teacherRepository.findByEmailAndIsActive(username, USER_ACTIVATED).map(TeacherDetails::new),
            Manager.class, username -> managerRepository.findByEmail(username).map(ManagerDetails::new),
            Admin.class, username -> adminRepository.findByEmail(username).map(AdminDetails::new)
        );
    }

    /**
     * Loads user details by the provided username (email).
     * <p>
     * Iterates through the {@code userLoaders} map, applying each loader function to the username. Returns the
     * first matching {@link UserDetails} instance found across the user types. If no user is found, throws a
     * {@link UsernameNotFoundException} with a formatted error message.
     *
     * @param username the email address of the user to authenticate
     * @return the {@link UserDetails} representing the authenticated user
     * @throws UsernameNotFoundException if no user is found with the specified email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userLoaders.values().stream()
            .map(loader -> loader.apply(username))
            .flatMap(Optional::stream)
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(username)));
    }

}
