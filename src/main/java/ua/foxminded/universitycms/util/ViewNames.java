package ua.foxminded.universitycms.util;

/**
 * Utility class containing constant view names for the Spring MVC application.
 * Provides logical view names used by controllers to render specific views, centralizing them
 * for easier management. Designed as a non-instantiable class with static final constants.
 *
 * @author Serhii Bohdan
 */
public class ViewNames {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an {@link IllegalStateException} if called.
     */
    private ViewNames() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * View name for displaying the error page.
     */
    public static final String CUSTOM_ERROR_PAGE = "custom-error-page";

    /**
     * View name for displaying the login page.
     */
    public static final String LOGIN_PAGE = "security/login";

    /**
     * View name for displaying the password update form.
     */
    public static final String PASSWORD_UPDATE_FORM = "security/password-update-form";

    /**
     * View name for displaying the home page.
     */
    public static final String HOME_PAGE = "home";

    /**
     * View name for displaying the authorized user's profile page.
     */
    public static final String AUTHORIZED_USER_PROFILE_PAGE = "authorized/profile-page";

    /**
     * View name for displaying the authorized user's password update form.
     */
    public static final String AUTHORIZED_USER_PASSWORD_UPDATE_FORM = "authorized/password-update-form";

    /**
     * View name for displaying the authorized user's name update form.
     */
    public static final String AUTHORIZED_USER_NAME_UPDATE_FORM = "authorized/name-update-form";

    /**
     * View name for displaying all administrators.
     */
    public static final String ALL_ADMINS_PAGE = "admins/all-admins";

    /**
     * View name for displaying the admin creation form.
     */
    public static final String ADMIN_CREATION_FORM = "admins/creation-form";

    /**
     * View name for displaying the administrator update form.
     */
    public static final String ADMIN_UPDATE_FORM = "admins/update-form";

    /**
     * View name for displaying the details of a specific administrator.
     */
    public static final String PARTICULAR_ADMIN = "admins/particular-admin";

    /**
     * View name for displaying all groups.
     */
    public static final String ALL_GROUPS_PAGE = "groups/all-groups";

    /**
     * View name for displaying the group creation form.
     */
    public static final String GROUP_CREATION_FORM = "groups/creation-form";

    /**
     * View name for displaying the group update form.
     */
    public static final String GROUP_UPDATE_FORM = "groups/update-form";

    /**
     * View name for displaying a specific group.
     */
    public static final String SPECIFIC_GROUP = "groups/specific-group";

    /**
     * View name for displaying groups available for course enrollment.
     */
    public static final String GROUPS_FOR_ENROLL_IN_COURSE = "groups/groups-for-enroll-in-course";

    /**
     * View name for displaying all managers.
     */
    public static final String ALL_MANAGERS_PAGE = "managers/all-managers";

    /**
     * View name for displaying the manager creation form.
     */
    public static final String MANAGER_CREATION_FORM = "managers/creation-form";

    /**
     * View name for displaying the manager update form.
     */
    public static final String MANAGER_UPDATE_FORM = "managers/update-form";

    /**
     * View name for displaying a specific manager's details.
     */
    public static final String PARTICULAR_MANAGER = "managers/particular-manager";

    /**
     * View name for displaying all students.
     */
    public static final String ALL_STUDENTS_PAGE = "students/all-students";

    /**
     * View name for displaying the student creation form.
     */
    public static final String STUDENT_CREATION_FORM = "students/creation-form";

    /**
     * View name for displaying the student update form.
     */
    public static final String STUDENT_UPDATE_FORM = "students/update-form";

    /**
     * View name for displaying students not enrolled in any course.
     */
    public static final String STUDENTS_NOT_ENROLLED_IN_COURSE = "students/not-enrolled-in-course";

    /**
     * View name for displaying a specific student's details.
     */
    public static final String PARTICULAR_STUDENT = "students/particular-student";

    /**
     * View name for displaying lessons assigned to an educator.
     */
    public static final String EDUCATOR_LESSONS = "schedule/educator-lessons";

    /**
     * View name for displaying the lesson creation form.
     */
    public static final String LESSON_CREATION_FORM = "schedule/lesson-creation-form";

    /**
     * View name for displaying the lesson update form.
     */
    public static final String LESSON_UPDATE_FORM = "schedule/lesson-update-form";

    /**
     * View name for displaying all teachers.
     */
    public static final String ALL_TEACHERS_PAGE = "teachers/all-teachers";

    /**
     * View name for displaying a specific teacher's details.
     */
    public static final String PARTICULAR_TEACHER = "teachers/particular-teacher";

    /**
     * View name for displaying the teacher creation form.
     */
    public static final String TEACHER_CREATION_FORM = "teachers/creation-form";

    /**
     * View name for displaying the teacher update form.
     */
    public static final String TEACHER_UPDATE_FORM = "teachers/update-form";

    /**
     * View name for displaying all courses.
     */
    public static final String ALL_COURSES_PAGE = "courses/all-courses";

    /**
     * View name for displaying the user's courses.
     */
    public static final String USER_COURSES = "courses/user-courses";

    /**
     * View name for displaying a specific course.
     */
    public static final String SPECIFIC_COURSE = "courses/specific-course";

    /**
     * View name for displaying the course creation form.
     */
    public static final String COURSE_CREATION_FORM = "courses/creation-form";

    /**
     * View name for displaying the course update form.
     */
    public static final String COURSE_UPDATE_FORM = "courses/update-form";

    /**
     * View name for displaying students enrolled in a course.
     */
    public static final String COURSE_STUDENTS = "courses/course-students";

    /**
     * View name for displaying the topic creation form.
     */
    public static final String TOPIC_CREATION_FORM = "topics/creation-form";

    /**
     * View name for displaying the topic update form.
     */
    public static final String TOPIC_UPDATE_FORM = "topics/update-form";

    /**
     * View name for displaying a student's marks.
     */
    public static final String STUDENT_MARKS = "marks/student-marks";

    /**
     * View name for displaying the mark creation form.
     */
    public static final String MARK_CREATION_FORM = "marks/creation-form";

    /**
     * View name for displaying the mark update form.
     */
    public static final String MARK_UPDATE_FORM = "marks/update-form";

}
