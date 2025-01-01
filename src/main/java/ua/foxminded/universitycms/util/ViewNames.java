package ua.foxminded.universitycms.util;

/**
 * Utility class that contains constant view names for the Spring MVC application.
 * <p>
 * These constants represent the logical view names used by controllers to
 * render specific views. This helps centralize view names, making it easier
 * to manage and modify them without hard-coding view names throughout the application.
 * <p>
 * This class cannot be instantiated and is designed to hold static final constants.
 *
 * @author Serhii Bohdan
 */
public class ViewNames {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Calling this constructor will throw an IllegalStateException.
     */
    private ViewNames() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * View name for the error page.
     */
    public static final String ERROR_PAGE = "error-page";

    /**
     * View name for the login page.
     */
    public static final String LOGIN_PAGE = "security/login";

    /**
     * The view name for the password update form.
     */
    public static final String PASSWORD_UPDATE_FORM = "security/password-update-form";

    /**
     * View name for the home page.
     */
    public static final String HOME_PAGE = "home";

    /**
     * View name for the page that displays all administrators.
     */
    public static final String ALL_ADMINS_PAGE = "admins/all-admins";

    /**
     * View name for the page that displays all groups.
     */
    public static final String ALL_GROUPS_PAGE = "groups/all-groups";

    /**
     * The view name for the group creation form.
     */
    public static final String GROUP_CREATION_FORM = "groups/creation-form";

    /**
     * The view name for the group update form.
     */
    public static final String GROUP_UPDATE_FORM = "groups/update-form";

    /**
     * The view name for displaying a specific group.
     */
    public static final String SPECIFIC_GROUP = "groups/specific-group";

    /**
     * The view name for displaying groups available for enrollment in a course.
     */
    public static final String GROUPS_FOR_ENROLL_IN_COURSE = "groups/groups-for-enroll-in-course";

    /**
     * View name for the page that displays all managers.
     */
    public static final String ALL_MANAGERS_PAGE = "managers/all-managers";

    /**
     * View name for the calendar page.
     */
    public static final String CALENDAR = "schedule/calendar";

    /**
     * View name for the page that displays all students.
     */
    public static final String ALL_STUDENTS_PAGE = "students/all-students";

    /**
     * The view name for the student creation form.
     */
    public static final String STUDENT_CREATION_FORM = "students/creation-form";

    /**
     * The view name for the student update form.
     */
    public static final String STUDENT_UPDATE_FORM = "students/update-form";

    /**
     * View name for the page that displays students who are not enrolled in any course.
     */
    public static final String STUDENTS_NOT_ENROLLED_IN_COURSE = "students/not-enrolled-in-course";

    /**
     * The view name used to display details of a particular student.
     */
    public static final String PARTICULAR_STUDENT = "students/particular-student";

    /**
     * The view name for displaying the actual study day schedule.
     */
    public static final String ACTUAL_STUDY_DAY_PAGE = "schedule/actual-study-day";

    /**
     * The view name for displaying the past study day schedule.
     */
    public static final String PAST_STUDY_DAY_PAGE = "schedule/past-study-day";

    /**
     * View name for the lesson creation form page.
     */
    public static final String LESSON_CREATION_FORM = "schedule/lesson-creation-form";

    /**
     * View name for the lesson update form page.
     */
    public static final String LESSON_UPDATE_FORM = "schedule/lesson-update-form";

    /**
     * View name for the page that displays all teachers.
     */
    public static final String ALL_TEACHERS_PAGE = "teachers/all-teachers";

    /**
     * The view name for the page that displays detailed information about a specific teacher.
     */
    public static final String PARTICULAR_TEACHER = "teachers/particular-teacher";

    /**
     * View name for the teacher creation form page.
     */
    public static final String TEACHER_CREATION_FORM = "teachers/creation-form";

    /**
     * View name for the teacher update form page.
     */
    public static final String TEACHER_UPDATE_FORM = "teachers/update-form";

    /**
     * View name for the page that displays all courses.
     */
    public static final String ALL_COURSES_PAGE = "courses/all-courses";

    /**
     * View name for the page that displays the user's courses.
     */
    public static final String USER_COURSES = "courses/user-courses";

    /**
     * View name for the page that displays a specific course.
     */
    public static final String SPECIFIC_COURSE = "courses/specific-course";

    /**
     * View name for the course creation form.
     */
    public static final String COURSE_CREATION_FORM = "courses/creation-form";

    /**
     * View name for the course update form.
     */
    public static final String COURSE_UPDATE_FORM = "courses/update-form";

    /**
     * View name for the page that displays students enrolled in a course.
     */
    public static final String COURSE_STUDENTS = "courses/course-students";

    /**
     * View name for the topic creation form.
     */
    public static final String TOPIC_CREATION_FORM = "topics/creation-form";

    /**
     * View name for the topic update form.
     */
    public static final String TOPIC_UPDATE_FORM = "topics/update-form";

    /**
     * View name for the page that displays a student's marks.
     */
    public static final String STUDENT_MARKS = "marks/student-marks";

    /**
     * View name for the mark creation form.
     */
    public static final String MARK_CREATION_FORM = "marks/creation-form";

    /**
     * View name for the mark update form.
     */
    public static final String MARK_UPDATE_FORM = "marks/update-form";

}
