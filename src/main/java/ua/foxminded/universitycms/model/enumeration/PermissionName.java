package ua.foxminded.universitycms.model.enumeration;

/**
 * Enumerates fine-grained permissions for controlling access to specific actions within the application.
 * <p>
 * Each permission is associated with a particular resource (e.g., Admins, Students) and an action
 * (e.g., Create, Read, Update, Delete).
 * <p>
 * These permissions can be assigned to different roles to control the level of access that users have.
 *
 * @author Serhii Bohdan
 */
public enum PermissionName {

    /**
     * Permission to create new admins.
     */
    ADMINS_CREATE,

    /**
     * Permission to read admin data.
     */
    ADMINS_READ,

    /**
     * Permission to update existing admins.
     */
    ADMINS_UPDATE,

    /**
     * Permission to delete admins.
     */
    ADMINS_DELETE,

    /**
     * Permission to create new managers.
     */
    MANAGERS_CREATE,

    /**
     * Permission to read manager data.
     */
    MANAGERS_READ,

    /**
     * Permission to update existing managers.
     */
    MANAGERS_UPDATE,

    /**
     * Permission to delete managers.
     */
    MANAGERS_DELETE,

    /**
     * Permission to create new teachers.
     */
    TEACHERS_CREATE,

    /**
     * Permission to read teacher data.
     */
    TEACHERS_READ,

    /**
     * Permission to update existing teachers.
     */
    TEACHERS_UPDATE,

    /**
     * Permission to delete teachers.
     */
    TEACHERS_DELETE,

    /**
     * Permission to create new students.
     */
    STUDENTS_CREATE,

    /**
     * Permission to read student data.
     */
    STUDENTS_READ,

    /**
     * Permission to update existing students.
     */
    STUDENTS_UPDATE,

    /**
     * Permission to delete students.
     */
    STUDENTS_DELETE,

    /**
     * Permission to create new groups.
     */
    GROUPS_CREATE,

    /**
     * Permission to read group data.
     */
    GROUPS_READ,

    /**
     * Permission to update existing groups.
     */
    GROUPS_UPDATE,

    /**
     * Permission to delete groups.
     */
    GROUPS_DELETE,

    /**
     * Permission to create new courses.
     */
    COURSES_CREATE,

    /**
     * Permission to read course data.
     */
    COURSES_READ,

    /**
     * Permission to update existing courses.
     */
    COURSES_UPDATE,

    /**
     * Permission to delete courses.
     */
    COURSES_DELETE,

    /**
     * Permission to create new topics.
     */
    TOPICS_CREATE,

    /**
     * Permission to read topic data.
     */
    TOPICS_READ,

    /**
     * Permission to update existing topics.
     */
    TOPICS_UPDATE,

    /**
     * Permission to delete topics.
     */
    TOPICS_DELETE,

    /**
     * Permission to create new marks.
     */
    MARKS_CREATE,

    /**
     * Permission to read mark data.
     */
    MARKS_READ,

    /**
     * Permission to update existing marks.
     */
    MARKS_UPDATE,

    /**
     * Permission to delete marks.
     */
    MARKS_DELETE,

    /**
     * Permission to create new schedules.
     */
    SCHEDULE_CREATE,

    /**
     * Permission to read schedule data.
     */
    SCHEDULE_READ,

    /**
     * Permission to update existing schedules.
     */
    SCHEDULE_UPDATE,

    /**
     * Permission to delete schedules.
     */
    SCHEDULE_DELETE,

    /**
     * Permission to create new study days.
     */
    STUDY_DAYS_CREATE,

    /**
     * Permission to read study day data.
     */
    STUDY_DAYS_READ,

    /**
     * Permission to update existing study days.
     */
    STUDY_DAYS_UPDATE,

    /**
     * Permission to delete study days.
     */
    STUDY_DAYS_DELETE,

    /**
     * Permission to create new lessons.
     */
    LESSONS_CREATE,

    /**
     * Permission to read lesson data.
     */
    LESSONS_READ,

    /**
     * Permission to update existing lessons.
     */
    LESSONS_UPDATE,

    /**
     * Permission to delete lessons.
     */
    LESSONS_DELETE;

}
