package ua.foxminded.universitycms.service;

import jakarta.validation.constraints.NotNull;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.model.Mark;
import java.util.List;

/**
 * The {@code MarkService} interface defines a set of operations for managing {@link Mark} entities and their
 * corresponding {@link MarkDto} representations. It extends the generic {@link Service} interface, providing
 * specialized services for working with marks within a course, including retrieval based on student, course,
 * and topic criteria.
 *
 * @author Serhii Bohdan
 */
public interface MarkService extends Service<Mark, MarkDto> {

    /**
     * Retrieves a list of student's marks for a given course.
     *
     * @param studentId the ID of the student whose marks to retrieve
     * @param courseId  the ID of the course for which to retrieve marks
     * @return a list of {@link MarkDto} objects representing the student's marks in the course
     */
    List<MarkDto> getStudentCourseMarks(long studentId, long courseId);

    /**
     * Retrieves a list of student's marks for a given course and topic.
     *
     * @param studentId   the ID of the student whose marks to retrieve
     * @param courseId    the ID of the course for which to retrieve marks
     * @param topicName   the name of the topic for which to retrieve marks
     * @return a list of {@link MarkDto} objects representing the student's marks for the specified topic in the course
     */
    List<MarkDto> getStudentCourseMarksByTopicName(long studentId, long courseId, @NotNull String topicName);

    /**
     * Retrieves the names of topics within a given course.
     *
     * @param courseId the ID of the course for which to retrieve topic names
     * @return a list of topic names as strings
     */
    List<String> getNamesOfTopicsInCourse(long courseId);

}
