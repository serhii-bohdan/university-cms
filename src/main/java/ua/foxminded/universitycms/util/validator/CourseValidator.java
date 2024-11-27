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
 * Validator for checking the uniqueness of a teacher's course.
 * <p>
 * This class implements the {@link ConstraintValidator} interface for the {@link UniqueTeacherCourse}
 * annotation. It validates whether a course's name and description are unique for a particular teacher
 * based on the provided {@link CourseDto} object.
 * <p>
 * The validation logic differentiates between creating a new course and updating an existing course.
 * It queries the {@link CourseRepository} to retrieve the list of courses associated with the teacher.
 * It then checks if the course name and description are unique within that teacher's courses.
 *
 * @author Serhii Bohdan
 */
@Component
@RequiredArgsConstructor
public class CourseValidator implements ConstraintValidator<UniqueTeacherCourse, CourseDto> {

    /**
     * Error message used when the course name is not unique among a teacher's courses.
     */
    private static final String COURSE_NAME_NOT_UNIQUE_MESSAGE = """
        A course with this name already exists. Enter a new name for the course.
        """;

    /**
     * Error message used when the course description is not unique among a teacher's courses.
     */
    private static final String DESCRIPTION_NOT_UNIQUE_MESSAGE = """
        A course with this description already exists. Enter a new description for the course.
        """;

    /**
     * The repository for accessing and querying course data.
     */
    private final CourseRepository courseRepository;

    /**
     * The course data being validated.
     */
    private CourseDto value;

    /**
     * The context for validation, used to add constraint violations.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the given constraint annotation.
     * <p>
     * This method is called once when the validator is created. It can be used to perform any setup
     * necessary for the validation logic.
     *
     * @param constraintAnnotation the annotation instance for the constraint being validated
     */
    @Override
    public void initialize(UniqueTeacherCourse constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the {@link CourseDto} object to ensure that the course's name and description are unique.
     * <p>
     * This method checks whether the course is being created or updated and applies the appropriate
     * uniqueness checks.
     *
     * @param value   the course data to validate
     * @param context the validation context
     * @return {@code true} if the course's name and description are unique; {@code false} otherwise
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
