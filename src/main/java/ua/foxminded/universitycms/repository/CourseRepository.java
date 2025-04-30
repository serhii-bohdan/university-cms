package ua.foxminded.universitycms.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;

/**
 * Spring Data JPA repository for managing {@link Course} entities in the university management system.
 * <p>
 * This interface extends {@link JpaRepository}, inheriting standard CRUD operations (create, read,
 * update, delete) and pagination support for the {@link Course} entity, identified by a {@code Long}
 * primary key. It also provides custom query methods to retrieve courses based on specific criteria,
 * such as course name, author, or student association. The {@code @Repository} annotation marks this
 * interface as a Spring Data repository, enabling automatic implementation by Spring to encapsulate
 * storage, retrieval, and search behavior.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Course
 * @see Student
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Retrieves courses with a name matching the specified value, ignoring case sensitivity.
     * <p>
     * This method queries the database for {@link Course} entities where the course name matches the
     * provided value (case-insensitive), returning results as a {@link Page} object to support
     * pagination and sorting. The {@link Pageable} parameter defines the page size, page number, and
     * sort options.
     *
     * @param name     the course name to search for, ignoring case
     * @param pageable the pagination and sorting configuration
     * @return a {@link Page} containing matching courses, or an empty page if no matches are found
     */
    Page<Course> findCourseByCourseNameIgnoreCase(String name, Pageable pageable);

    /**
     * Retrieves all courses authored by a specific teacher.
     * <p>
     * This method queries the database for {@link Course} entities where the author (teacher) matches
     * the specified ID, returning the results as a {@link List}.
     *
     * @param authorId the ID of the teacher who authored the courses
     * @return a {@link List} of courses authored by the specified teacher, or an empty list if none are found
     */
    List<Course> findByAuthorId(long authorId);

    /**
     * Retrieves all courses associated with a specific student by their ID.
     * <p>
     * This method uses a custom JPQL query to join the {@link Student} entity with its associated
     * {@link Course} entities through the many-to-many relationship, returning a list of courses in
     * which the student with the specified ID is enrolled.
     *
     * @param studentId the ID of the student whose courses are to be retrieved
     * @return a {@link List} of courses associated with the specified student, or an empty list if no
     * courses are found
     */
    @Query("SELECT c FROM Student s JOIN s.courses c WHERE s.id = :studentId")
    List<Course> findStudentCoursesByStudentId(long studentId);

}
