package ua.foxminded.universitycms.util;

import java.util.List;

/**
 * Utility class containing constant attribute names for storing entities in the Spring MVC Model.
 * Designed as a non-instantiable class with static final constants used as keys for model data.
 *
 * @author Serhii Bohdan
 * @see org.springframework.ui.Model
 */
public class ModelAttributeNames {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an {@link IllegalStateException} if called.
     */
    private ModelAttributeNames() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Attribute name for storing the current page number in the Spring MVC Model.
     */
    public static final String PAGE_ATTRIBUTE = "page";

    /**
     * Attribute name for storing the total number of items in the dataset in the Spring MVC Model.
     */
    public static final String TOTAL_ITEMS_ATTRIBUTE = "totalItems";

    /**
     * Attribute name for storing the total number of pages in the dataset in the Spring MVC Model.
     */
    public static final String TOTAL_PAGES_ATTRIBUTE = "totalPages";

    /**
     * Attribute name for storing the page size in the Spring MVC Model.
     */
    public static final String SIZE_ATTRIBUTE = "size";

    /**
     * Attribute name for storing a search keyword in the Spring MVC Model.
     */
    public static final String KEYWORD_ATTRIBUTE = "keyword";

    /**
     * Attribute name for storing a password update request in the Spring MVC Model.
     */
    public static final String PASSWORD_UPDATE_REQUEST_ATTRIBUTE = "passwordUpdateRequest";

    /**
     * Attribute name for storing exception details in the Spring MVC Model.
     */
    public static final String EXCEPTION_ATTRIBUTE = "exception";

    /**
     * Attribute name for storing user-related data in the Spring MVC Model.
     */
    public static final String USER_ATTRIBUTE = "user";

    /**
     * Attribute name for storing the user's ID in the Spring MVC Model.
     */
    public static final String USER_ID_ATTRIBUTE = "userId";

    /**
     * Attribute name for storing the user's full name in the Spring MVC Model.
     */
    public static final String USER_FULL_NAME_ATTRIBUTE = "userFullName";

    /**
     * Attribute name for storing the user's role in the Spring MVC Model.
     */
    public static final String USER_ROLE_ATTRIBUTE = "userRole";

    /**
     * Attribute name for storing a list of administrators in the Spring MVC Model.
     */
    public static final String ADMINS_ATTRIBUTE = "admins";

    /**
     * Attribute name for storing a single admin DTO in the Spring MVC Model.
     */
    public static final String ADMIN_ATTRIBUTE = "admin";

    /**
     * Attribute name for storing all email addresses of admins in the Spring MVC Model.
     */
    public static final String ADMINS_ALL_EMAILS_ATTRIBUTE = "allEmailsOfAdmins";

    /**
     * Attribute name for storing a list of groups in the Spring MVC Model.
     */
    public static final String GROUPS_ATTRIBUTE = "groups";

    /**
     * Attribute name for storing a group object in the Spring MVC Model.
     */
    public static final String GROUP_ATTRIBUTE = "group";

    /**
     * Attribute name for storing all names of groups in the Spring MVC Model.
     */
    public static final String GROUPS_ALL_NAMES_ATTRIBUTE = "allNamesOfGroups";

    /**
     * Attribute name for storing a list of all existing groups in the Spring MVC Model.
     */
    public static final String ALL_GROUPS_ATTRIBUTE = "allExistingGroups";

    /**
     * Attribute name for storing a list of managers in the Spring MVC Model.
     */
    public static final String MANAGERS_ATTRIBUTE = "managers";

    /**
     * Attribute name for storing a single manager DTO in the Spring MVC Model.
     */
    public static final String MANAGER_ATTRIBUTE = "manager";

    /**
     * Attribute name for storing all email addresses of managers in the Spring MVC Model.
     */
    public static final String MANAGERS_ALL_EMAILS_ATTRIBUTE = "allEmailsOfManagers";

    /**
     * Attribute name for storing a list of students in the Spring MVC Model.
     */
    public static final String STUDENTS_ATTRIBUTE = "students";

    /**
     * Attribute name for storing a student object in the Spring MVC Model.
     */
    public static final String STUDENT_ATTRIBUTE = "student";

    /**
     * Attribute name for storing the student ID in the Spring MVC Model.
     */
    public static final String STUDENT_ID_ATTRIBUTE = "studentId";

    /**
     * Attribute name for storing the student's full name in the Spring MVC Model.
     */
    public static final String STUDENT_FULL_NAME_ATTRIBUTE = "studentFullName";

    /**
     * Attribute name for storing all email addresses of students in the Spring MVC Model.
     */
    public static final String STUDENTS_ALL_EMAILS_ATTRIBUTE = "allEmailsOfStudents";

    /**
     * Attribute name for storing students not enrolled in any course in the Spring MVC Model.
     */
    public static final String NOT_ENROLLED_STUDENTS_ATTRIBUTE = "notEnrolledStudents";

    /**
     * Attribute name for storing student email addresses in the Spring MVC Model.
     */
    public static final String STUDENT_EMAILS_ATTRIBUTE = "studentEmails";

    /**
     * Attribute name for storing a teacher object in the Spring MVC Model.
     */
    public static final String TEACHER_ATTRIBUTE = "teacher";

    /**
     * Attribute name for storing a list of teachers in the Spring MVC Model.
     */
    public static final String TEACHERS_ATTRIBUTE = "teachers";

    /**
     * Attribute name for storing all email addresses of teachers in the Spring MVC Model.
     */
    public static final String TEACHERS_ALL_EMAILS_ATTRIBUTE = "allEmailsOfTeachers";

    /**
     * Attribute name for storing a course object in the Spring MVC Model.
     */
    public static final String COURSE_ATTRIBUTE = "course";

    /**
     * Attribute name for storing a list of courses in the Spring MVC Model.
     */
    public static final String COURSES_ATTRIBUTE = "courses";

    /**
     * Attribute name for storing the course ID in the Spring MVC Model.
     */
    public static final String COURSE_ID_ATTRIBUTE = "courseId";

    /**
     * Attribute name for storing all names of courses in the Spring MVC Model.
     */
    public static final String COURSES_ALL_NAMES_ATTRIBUTE = "allNamesOfCourses";

    /**
     * Attribute name for storing the user's courses in the Spring MVC Model.
     */
    public static final String USER_COURSES_ATTRIBUTE = "userCourses";

    /**
     * Attribute name for storing the names of the user's courses in the Spring MVC Model.
     */
    public static final String USER_COURSES_NAMES_ATTRIBUTE = "userCoursesNames";

    /**
     * Attribute name for storing students of a course in the Spring MVC Model.
     */
    public static final String COURSE_STUDENTS_ATTRIBUTE = "courseStudents";

    /**
     * Attribute name for storing a topic object in the Spring MVC Model.
     */
    public static final String TOPIC_ATTRIBUTE = "topic";

    /**
     * Attribute name for storing a list of topics in the Spring MVC Model.
     */
    public static final String TOPICS_ATTRIBUTE = "topics";

    /**
     * Attribute name for storing a list of unrated topics in the Spring MVC Model.
     */
    public static final String UNRATED_TOPICS_ATTRIBUTE = "unratedTopics";

    /**
     * Attribute name for storing the names of topics in the Spring MVC Model.
     */
    public static final String TOPIC_NAMES_ATTRIBUTE = "namesOfTopics";

    /**
     * Attribute name for storing a mark object in the Spring MVC Model.
     */
    public static final String MARK_ATTRIBUTE = "mark";

    /**
     * Attribute name for storing a list of marks in the Spring MVC Model.
     */
    public static final String MARKS_ATTRIBUTE = "marks";

    /**
     * Attribute name for storing the schedule ID in the Spring MVC Model.
     */
    public static final String SCHEDULE_ID_ATTRIBUTE = "scheduleId";

    /**
     * Attribute name for storing the current date of the user in the Spring MVC Model.
     */
    public static final String USER_CURRENT_DATE_ATTRIBUTE = "userCurrentDate";

    /**
     * Attribute name for storing the start date of a period in the Spring MVC Model.
     */
    public static final String START_DATE_ATTRIBUTE = "startDate";

    /**
     * Attribute name for storing the end date of a period in the Spring MVC Model.
     */
    public static final String END_DATE_ATTRIBUTE = "endDate";

    /**
     * Attribute name for storing a lesson object in the Spring MVC Model.
     */
    public static final String LESSON_ATTRIBUTE = "lesson";

    /**
     * Attribute name for storing a list of lessons in the Spring MVC Model.
     */
    public static final String LESSONS_ATTRIBUTE = "lessons";

    /**
     * Attribute name for storing a list of time zones in the Spring MVC Model.
     */
    public static final String TIME_ZONES_ATTRIBUTE = "timeZones";

    /**
     * List of supported time zone offsets in UTC format.
     */
    public static final List<String> TIME_ZONES_LIST = List.of(
        "-12:00", "-11:00", "-10:00", "-09:30", "-09:00", "-08:00", "-07:00", "-06:00",
        "-05:00", "-04:00", "-03:30", "-03:00", "-02:00", "-01:00", "+00:00", "+01:00",
        "+02:00", "+03:00", "+03:30", "+04:00", "+04:30", "+05:00", "+05:30", "+05:45",
        "+06:00", "+06:30", "+07:00", "+08:00", "+08:45", "+09:00", "+09:30", "+10:00",
        "+10:30", "+11:00", "+12:00", "+12:45", "+13:00", "+14:00"
    );

}
