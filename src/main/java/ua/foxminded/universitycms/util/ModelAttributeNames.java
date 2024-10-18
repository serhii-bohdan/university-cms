package ua.foxminded.universitycms.util;

/**
 * Utility class that contains constant attribute names used for storing
 * various entities in the Spring MVC Model.
 * <p>
 * This class cannot be instantiated and is designed to hold static final
 * constants, which are used as keys to store and retrieve data in the Model.
 *
 * @author Serhii Bohdan
 */
public class ModelAttributeNames {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Calling this constructor will throw an IllegalStateException.
     */
    private ModelAttributeNames() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The attribute name used to store the current page number in the Spring MVC Model.
     */
    public static final String PAGE_ATTRIBUTE = "page";

    /**
     * The attribute name used to store the total number of items in the dataset in the Spring MVC Model.
     */
    public static final String TOTAL_ITEMS_ATTRIBUTE = "totalItems";

    /**
     * The attribute name used to store the total number of pages in the dataset in the Spring MVC Model.
     */
    public static final String TOTAL_PAGES_ATTRIBUTE = "totalPages";

    /**
     * The attribute name used to store the page size in the Spring MVC Model.
     */
    public static final String SIZE_ATTRIBUTE = "size";

    /**
     * Used as a model attribute to store a keyword for search.
     */
    public static final String KEYWORD_ATTRIBUTE = "keyword";

    /**
     * Used as a model attribute to store error messages related to validation.
     */
    public static final String ERROR_MESSAGE_ATTRIBUTE = "validationErrorMessage";

    /**
     * The attribute name used to store exception details in the Spring MVC Model.
     */
    public static final String EXCEPTION_ATTRIBUTE = "exception";

    /**
     * The attribute name used to store a list of administrators in the Spring MVC Model.
     */
    public static final String ADMINS_ATTRIBUTE = "admins";

    /**
     * The attribute name used to store all names of administrators in the Spring MVC Model.
     */
    public static final String ADMINS_ALL_NAMES_ATTRIBUTE = "allNamesOfAdmins";

    /**
     * The attribute name used to store a list of groups in the Spring MVC Model.
     */
    public static final String GROUPS_ATTRIBUTE = "groups";

    /**
     * The attribute name used to store a group object in the Spring MVC Model.
     */
    public static final String GROUP_ATTRIBUTE = "group";

    /**
     * The attribute name used to store all names of groups in the Spring MVC Model.
     */
    public static final String GROUPS_ALL_NAMES_ATTRIBUTE = "allNamesOfGroups";

    /**
     * The attribute name used to store a list of managers in the Spring MVC Model.
     */
    public static final String MANAGERS_ATTRIBUTE = "managers";

    /**
     * The attribute name used to store all names of managers in the Spring MVC Model.
     */
    public static final String MANAGERS_ALL_NAMES_ATTRIBUTE = "allNamesOfManagers";

    /**
     * The attribute name used to store a list of students in the Spring MVC Model.
     */
    public static final String STUDENTS_ATTRIBUTE = "students";

    /**
     * The attribute name used to store the student ID in the Spring MVC Model.
     */
    public static final String STUDENT_ID_ATTRIBUTE = "studentId";

    /**
     * The attribute name used to store the student's full name in the Spring MVC Model.
     */
    public static final String STUDENT_FULL_NAME_ATTRIBUTE = "studentFullName";

    /**
     * The attribute name used to store all names of students in the Spring MVC Model.
     */
    public static final String STUDENTS_ALL_NAMES_ATTRIBUTE = "allNamesOfStudents";

    /**
     * The attribute name used to store students who are not enrolled in any course.
     */
    public static final String NOT_ENROLLED_STUDENTS_ATTRIBUTE = "notEnrolledStudents";

    /**
     * The attribute name used to store student email addresses in the Spring MVC Model.
     */
    public static final String STUDENT_EMAILS_ATTRIBUTE = "studentEmails";

    /**
     * The attribute name used to store a list of teachers in the Spring MVC Model.
     */
    public static final String TEACHERS_ATTRIBUTE = "teachers";

    /**
     * The attribute name used to store all names of teachers in the Spring MVC Model.
     */
    public static final String TEACHERS_ALL_NAMES_ATTRIBUTE = "allNamesOfTeachers";

    /**
     * Used as a model attribute to store the course object.
     */
    public static final String COURSE_ATTRIBUTE = "course";

    /**
     * The attribute name used to store a list of courses in the Spring MVC Model.
     */
    public static final String COURSES_ATTRIBUTE = "courses";

    /**
     * The attribute name used to store the course ID in the Spring MVC Model.
     */
    public static final String COURSE_ID_ATTRIBUTE = "courseId";

    /**
     * The attribute name used to store all names of courses in the Spring MVC Model.
     */
    public static final String COURSES_ALL_NAMES_ATTRIBUTE = "allNamesOfCourses";

    /**
     * The attribute name used to store the user's courses in the Spring MVC Model.
     */
    public static final String USER_COURSES_ATTRIBUTE = "userCourses";

    /**
     * The attribute name used to store the names of the user's courses in the Spring MVC Model.
     */
    public static final String USER_COURSES_NAMES_ATTRIBUTE = "userCoursesNames";

    /**
     * The attribute name used to store students of a course in the Spring MVC Model.
     */
    public static final String COURSE_STUDENTS_ATTRIBUTE = "courseStudents";

    /**
     * The attribute name used to store the topic data in the Spring MVC Model.
     */
    public static final String TOPIC_ATTRIBUTE = "topic";

    /**
     * The attribute name used to store a list of topics in the Spring MVC Model.
     */
    public static final String TOPICS_ATTRIBUTE = "topics";

    /**
     * The attribute name used to store a list of unrated topics in the Spring MVC Model.
     */
    public static final String UNRATED_TOPICS_ATTRIBUTE = "unratedTopics";

    /**
     * The attribute name used to store the names of topics in the Spring MVC Model.
     */
    public static final String TOPIC_NAMES_ATTRIBUTE = "namesOfTopics";

    /**
     * The attribute name used to store the mark data in the Spring MVC Model.
     */
    public static final String MARK_ATTRIBUTE = "mark";

    /**
     * The attribute name used to store a list of marks in the Spring MVC Model.
     */
    public static final String MARKS_ATTRIBUTE = "marks";

    /**
     * The attribute name used to store a schedule object in the Spring MVC Model.
     */
    public static final String SCHEDULE_ATTRIBUTE = "schedule";

    /**
     * The attribute name used to store a study day object in the Spring MVC Model.
     */
    public static final String STUDY_DAY_ATTRIBUTE = "studyDay";

}
