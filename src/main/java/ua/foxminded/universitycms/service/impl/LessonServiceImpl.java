package ua.foxminded.universitycms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.LessonDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Lesson;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.LessonService;

/**
 * The {@code LessonServiceImpl} class implements the {@link LessonService} interface, providing concrete
 * implementations for managing lesson entities. It extends the {@link AbstractService} class, inheriting common
 * service functionalities for basic CRUD operations and validation.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 */
@Service
@Validated
public class LessonServiceImpl extends AbstractService<Lesson, LessonDto> implements LessonService {

    /**
     * Manages courses associated with a user based on their role.
     */
    private final CourseRepository courseRepository;

    /**
     * Constructs a new {@code LessonServiceImpl} instance with the given dependencies.
     *
     * @param repository the repository for managing lesson entities
     * @param mapper     the mapper for converting between lesson entities and DTOs
     */
    public LessonServiceImpl(JpaRepository<Lesson, Long> repository, Mapper<Lesson, LessonDto> mapper,
                             CourseRepository courseRepository) {
        super(repository, mapper);
        this.courseRepository = courseRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getUserCourses(CustomUserDetails customUserDetails) {
        long userId = customUserDetails.getId();
        RoleName userRole = customUserDetails.getRoleName();
        List<Course> userCourses = new ArrayList<>();

        if (RoleName.TEACHER.equals(userRole)) {
            userCourses = courseRepository.findByAuthorId(userId);
        } else if (RoleName.STUDENT.equals(userRole)) {
            userCourses = courseRepository.findStudentCoursesByStudentId(userId);
        }

        return userCourses.stream()
            .collect(Collectors.toMap(Course::getCourseName, Course::getId));
    }

}
