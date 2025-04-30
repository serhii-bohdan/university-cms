package ua.foxminded.universitycms.util.validator;

import java.util.List;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.util.annotation.UniqueTeacherCourse;

/**
 * Validator for ensuring the uniqueness of a teacher's course in the university management system.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to enforce the {@link UniqueTeacherCourse}
 * annotation. It validates whether a course's name and description, as provided in a {@link CourseDto} object,
 * are unique among the courses associated with a specific teacher. The validation logic distinguishes between
 * creating a new course and updating an existing one, querying the {@link CourseRepository} to check for duplicates.
 * The {@code @Component} annotation registers this class as a Spring-managed bean, and
 * {@code @RequiredArgsConstructor} ensures dependency injection of the repository.
 *
 * @author Serhii Bohdan
 * @see ConstraintValidator
 * @see UniqueTeacherCourse
 * @see CourseDto
 * @see CourseRepository
 */
@Component
@RequiredArgsConstructor
public class CourseValidator implements ConstraintValidator<UniqueTeacherCourse, CourseDto> {

    /**
     * Error message displayed when the course name is not unique among a teacher's courses.
     */
    private static final String COURSE_NAME_NOT_UNIQUE_MESSAGE = """
        A course with this name already exists. Enter a new name for the course.
        """;

    /**
     * Error message displayed when the course description is not unique among a teacher's courses.
     */
    private static final String DESCRIPTION_NOT_UNIQUE_MESSAGE = """
        A course with this description already exists. Enter a new description for the course.
        """;

    /**
     * Repository for querying course data to perform uniqueness checks.
     */
    private final CourseRepository courseRepository;

    /**
     * The {@link CourseDto} object currently being validated.
     */
    private CourseDto value;

    /**
     * The validation context used to report constraint violations.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link UniqueTeacherCourse} annotation.
     * <p>
     * This method is invoked once during validator instantiation to perform any necessary setup based on the
     * annotation's configuration. Currently, it delegates to the default implementation without additional logic.
     *
     * @param constraintAnnotation the {@link UniqueTeacherCourse} annotation instance being validated
     */
    @Override
    public void initialize(UniqueTeacherCourse constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the uniqueness of a course's name and description for a teacher.
     * <p>
     * This method checks the provided {@link CourseDto} against existing courses for the specified teacher. If the
     * course ID is null, it performs a creation validation; otherwise, it performs an update validation. Returns
     * {@code true} if both the name and description are unique among the teacher's courses, and {@code false}
     * otherwise, adding appropriate violation messages to the context if validation fails.
     *
     * @param value   the {@link CourseDto} object to validate
     * @param context the {@link ConstraintValidatorContext} for reporting validation errors
     * @return {@code true} if the course is unique; {@code false} if it violates uniqueness rules
     */
    @Override
    public boolean isValid(CourseDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        this.value = value;
        this.context = context;

        return this.value.getId() == null
            ? isCourseUniqueForSave()
            : isCourseUniqueForUpdate();
    }

    private boolean isCourseUniqueForSave() {
        List<Course> teacherCourses = courseRepository.findByAuthorId(value.getAuthorId());
        return isCourseNameUniqueAmongTeacherCoursesForSave(teacherCourses) &&
            isDescriptionUniqueAmongTeacherCoursesForSave(teacherCourses);
    }

    private boolean isCourseNameUniqueAmongTeacherCoursesForSave(List<Course> teacherCourses) {
        boolean isCourseNameUnique = teacherCourses.stream().noneMatch(c -> c.getCourseName().equals(value.getCourseName()));
        addConstraintViolationMessageIfInvalid(!isCourseNameUnique, COURSE_NAME_NOT_UNIQUE_MESSAGE);
        return isCourseNameUnique;
    }

    private boolean isDescriptionUniqueAmongTeacherCoursesForSave(List<Course> teacherCourses) {
        boolean isDescriptionUnique = teacherCourses.stream().noneMatch(c -> c.getCourseDescription().equals(value.getCourseDescription()));
        addConstraintViolationMessageIfInvalid(!isDescriptionUnique, DESCRIPTION_NOT_UNIQUE_MESSAGE);
        return isDescriptionUnique;
    }

    private boolean isCourseUniqueForUpdate() {
        List<Course> teacherCourses = courseRepository.findByAuthorId(value.getAuthorId());
        return isCourseNameUniqueAmongTeacherCoursesForUpdate(teacherCourses) &&
            isDescriptionUniqueAmongTeacherCoursesForUpdate(teacherCourses);
    }

    private boolean isCourseNameUniqueAmongTeacherCoursesForUpdate(List<Course> teacherCourses) {
        boolean isCourseNameUnique = teacherCourses.stream()
            .filter(c -> !c.getId().equals(value.getId()))
            .noneMatch(c -> c.getCourseName().equals(value.getCourseName()));
        addConstraintViolationMessageIfInvalid(!isCourseNameUnique, COURSE_NAME_NOT_UNIQUE_MESSAGE);
        return isCourseNameUnique;
    }

    private boolean isDescriptionUniqueAmongTeacherCoursesForUpdate(List<Course> teacherCourses) {
        boolean isDescriptionUnique = teacherCourses.stream()
            .filter(c -> !c.getId().equals(value.getId()))
            .noneMatch(c -> c.getCourseDescription().equals(value.getCourseDescription()));
        addConstraintViolationMessageIfInvalid(!isDescriptionUnique, DESCRIPTION_NOT_UNIQUE_MESSAGE);
        return isDescriptionUnique;
    }

    private void addConstraintViolationMessageIfInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
