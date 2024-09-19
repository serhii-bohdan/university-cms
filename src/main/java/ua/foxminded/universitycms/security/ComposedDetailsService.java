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
 * Custom implementation of Spring Security's {@link UserDetailsService} that handles authentication for
 * multiple user types.
 *
 * <p>This service intelligently delegates the loading of user details based on the user's email address.
 * It utilizes a map to associate specific user types (Admin, Manager, Teacher, Student) with their
 * corresponding repository methods for fetching the user data. This allows for a more flexible and
 * scalable authentication approach where you can easily add new user types in the future.
 *
 * @author Serhii Bohdan
 */
@Service
public class ComposedDetailsService implements UserDetailsService {

    /**
     * A map that associates each user class (Admin, Manager, Teacher, Student) with a function that
     * loads the corresponding user details based on the provided username (email).
     */
    private final Map<Class<?>, Function<String, Optional<? extends UserDetails>>> userLoaders;

    /**
     * Constructs a new `ComposedDetailsService` instance and initializes the `userLoaders` map with the
     * provided repositories.
     *
     * @param adminRepository   the repository for {@link Admin} entities
     * @param managerRepository the repository for {@link Manager} entities
     * @param teacherRepository the repository for {@link Teacher} entities
     * @param studentRepository the repository for {@link Student} entities
     */
    public ComposedDetailsService(AdminRepository adminRepository, ManagerRepository managerRepository,
                                  TeacherRepository teacherRepository, StudentRepository studentRepository) {
        userLoaders = Map.of(
            Student.class, username -> studentRepository.findByEmail(username).map(StudentDetails::new),
            Teacher.class, username -> teacherRepository.findByEmail(username).map(TeacherDetails::new),
            Manager.class, username -> managerRepository.findByEmail(username).map(ManagerDetails::new),
            Admin.class, username -> adminRepository.findByEmail(username).map(AdminDetails::new)
        );
    }

    /**
     * Loads the user details for the user with the given username (email).
     * <p>
     * This method iterates through the `userLoaders` map and attempts to find a matching user in each repository.
     * If a user is found, it is wrapped in the appropriate `UserDetails` implementation and returned. If no user
     * is found, a `UsernameNotFoundException` is thrown.
     *
     * @param username the username (email) of the user to load
     * @return the user details if found
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userLoaders.values().stream()
            .map(loader -> loader.apply(username))
            .flatMap(Optional::stream)
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s is not found", username)));
    }

}
