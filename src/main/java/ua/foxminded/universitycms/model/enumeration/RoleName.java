package ua.foxminded.universitycms.model.enumeration;

/**
 * Enumerates the distinct roles a user can have within the application.
 * <p>
 * Each role represents a specific level of access and authorization within the system.
 *
 * @author Serhii Bohdan
 */
public enum RoleName {

    /**
     * Administrator role with the highest level of privileges.\
     */
    ADMIN,

    /**
     * Manager role with administrative capabilities within a specific scope.
     */
    MANAGER,

    /**
     * Teacher role with permissions to create and manage educational content.
     */
    TEACHER,

    /**
     * Student role with limited access primarily for consuming educational content.
     */
    STUDENT;

}
