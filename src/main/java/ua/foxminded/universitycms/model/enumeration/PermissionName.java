package ua.foxminded.universitycms.model.enumeration;

/**
 * Enumerates fine-grained permissions for controlling access to specific actions within the university
 * management system.
 * <p>
 * Each permission is tied to a particular resource (e.g., Admins, Students, Courses) and an action
 * (e.g., Create, Read, Update, Delete). These permissions are assigned to roles to define the scope
 * of access and operations available to users within the application.
 * <p>
 * Permissions follow a consistent naming convention: <code>RESOURCE_ACTION</code>, where RESOURCE
 * indicates the entity (e.g., TEACHERS, COURSES) and ACTION specifies the operation (e.g., CREATE, READ).
 *
 * @author Serhii Bohdan
 * @see RoleName
 */
public enum PermissionName {

    /**
     * Permission to create new admin accounts.
     */
    ADMINS_CREATE,

    /**
     * Permission to view admin account details.
     */
    ADMINS_READ,

    /**
     * Permission to modify existing admin accounts.
     */
    ADMINS_UPDATE,

    /**
     * Permission to remove admin accounts.
     */
    ADMINS_DELETE,

    /**
     * Permission to create new manager accounts.
     */
    MANAGERS_CREATE,

    /**
     * Permission to view manager account details.
     */
    MANAGERS_READ,

    /**
     * Permission to modify existing manager accounts.
     */
    MANAGERS_UPDATE,

    /**
     * Permission to remove manager accounts.
     */
    MANAGERS_DELETE,

    /**
     * Permission to create new teacher profiles.
     */
    TEACHERS_CREATE,

    /**
     * Permission to view teacher profile details.
     */
    TEACHERS_READ,

    /**
     * Permission to modify existing teacher profiles.
     */
    TEACHERS_UPDATE,

    /**
     * Permission to remove teacher profiles.
     */
    TEACHERS_DELETE,

    /**
     * Permission to create new student records.
     */
    STUDENTS_CREATE,

    /**
     * Permission to view student record details.
     */
    STUDENTS_READ,

    /**
     * Permission to modify existing student records.
     */
    STUDENTS_UPDATE,

    /**
     * Permission to remove student records.
     */
    STUDENTS_DELETE,

    /**
     * Permission to create new student groups.
     */
    GROUPS_CREATE,

    /**
     * Permission to view group details.
     */
    GROUPS_READ,

    /**
     * Permission to modify existing groups.
     */
    GROUPS_UPDATE,

    /**
     * Permission to remove groups.
     */
    GROUPS_DELETE,

    /**
     * Permission to create new courses.
     */
    COURSES_CREATE,

    /**
     * Permission to view course details.
     */
    COURSES_READ,

    /**
     * Permission to modify existing courses.
     */
    COURSES_UPDATE,

    /**
     * Permission to remove courses.
     */
    COURSES_DELETE,

    /**
     * Permission to create new course topics.
     */
    TOPICS_CREATE,

    /**
     * Permission to view topic details.
     */
    TOPICS_READ,

    /**
     * Permission to modify existing topics.
     */
    TOPICS_UPDATE,

    /**
     * Permission to remove topics.
     */
    TOPICS_DELETE,

    /**
     * Permission to create new student marks.
     */
    MARKS_CREATE,

    /**
     * Permission to view mark details.
     */
    MARKS_READ,

    /**
     * Permission to modify existing marks.
     */
    MARKS_UPDATE,

    /**
     * Permission to remove marks.
     */
    MARKS_DELETE,

    /**
     * Permission to create new schedules.
     */
    SCHEDULE_CREATE,

    /**
     * Permission to view schedule details.
     */
    SCHEDULE_READ,

    /**
     * Permission to modify existing schedules.
     */
    SCHEDULE_UPDATE,

    /**
     * Permission to remove schedules.
     */
    SCHEDULE_DELETE,

    /**
     * Permission to create new lesson entries.
     */
    LESSONS_CREATE,

    /**
     * Permission to view lesson details.
     */
    LESSONS_READ,

    /**
     * Permission to modify existing lesson entries.
     */
    LESSONS_UPDATE,

    /**
     * Permission to remove lesson entries.
     */
    LESSONS_DELETE;

}
