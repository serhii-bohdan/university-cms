package ua.foxminded.universitycms.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ua.foxminded.universitycms.TestConfiguration;
import ua.foxminded.universitycms.exception.ValidationException;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.exception.EntityNotFoundException;

@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {

    @MockBean
    private CourseRepository courseRepositoryMock;

    @MockBean
    private TeacherRepository teacherRepositoryMock;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @MockBean
    private CourseMapper courseMapperMock;

    @Autowired
    private CourseService courseService;

    @Test
    void deductStudentFromCourse_shouldEntityNotFoundException_whenNoCourseWasFoundForGivenCourseId() {
        long courseId = 1L;
        long studentId = 1L;
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.deductStudentFromCourse(courseId, studentId));
        verify(courseRepositoryMock, times(1)).findById(courseId);
    }

    @Test
    void deductStudentFromCourse_shouldEntityNotFoundException_whenNoStudentWasFoundForGivenStudentId() {
        long courseId = 1L;
        long studentId = 1L;
        Course courseMock = mock(Course.class);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.deductStudentFromCourse(courseId, studentId));
        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findById(studentId);
    }

    @Test
    void deductStudentFromCourse_shouldValidationException_whenStudentNotInSetOfStudentsEnrolledInCourse() {
        long courseId = 1L;
        long studentId = 1L;
        Course courseMock = mock(Course.class);
        Student studentMock = mock(Student.class);
        Set<Student> courseStudents = mock(Set.class);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(studentMock));
        when(courseMock.getStudents()).thenReturn(courseStudents);
        when(courseStudents.contains(studentMock)).thenReturn(false);

        assertThrows(ValidationException.class, () -> courseService.deductStudentFromCourse(courseId, studentId));
        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findById(studentId);
    }

    @Test
    void deductStudentFromCourse_shouldRemoveStudentFromSetOfStudentsEnrolledInCourse_whenStudentExistInSetOfStudentsEnrolledInCourse() {
        long courseId = 1L;
        long studentId = 1L;
        Course courseMock = mock(Course.class);
        Student studentMock = mock(Student.class);
        Set<Student> courseStudents = Set.of(studentMock);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(studentMock));
        when(courseMock.getStudents()).thenReturn(courseStudents);

        courseService.deductStudentFromCourse(courseId, studentId);

        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findById(studentId);
        verify(courseMock, times(1)).removeStudent(studentMock);
    }

    @Test
    void enrollStudentInCourse_shouldEntityNotFoundException_whenNoCourseWasFoundForGivenCourseId() {
        long courseId = 1L;
        long studentId = 1L;
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.enrollStudentInCourse(courseId, studentId));
        verify(courseRepositoryMock, times(1)).findById(courseId);
    }

    @Test
    void enrollStudentInCourse_shouldEntityNotFoundException_whenNoStudentWasFoundForGivenStudentId() {
        long courseId = 1L;
        long studentId = 1L;
        Course courseMock = mock(Course.class);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.enrollStudentInCourse(courseId, studentId));
        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findById(studentId);
    }

    @Test
    void enrollStudentInCourse_shouldValidationException_whenStudentAlreadyInSetOfStudentsEnrolledInCourse() {
        long courseId = 1L;
        long studentId = 1L;
        Course courseMock = mock(Course.class);
        Student studentMock = mock(Student.class);
        Set<Student> courseStudents = Set.of(studentMock);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(studentMock));
        when(courseMock.getStudents()).thenReturn(courseStudents);

        assertThrows(ValidationException.class, () -> courseService.enrollStudentInCourse(courseId, studentId));
        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findById(studentId);
    }

    @Test
    void enrollStudentInCourse_shouldAddStudentToSetOfStudentsEnrolledInCourse_whenStudentNotExistInSetOfStudentsEnrolledInCourse() {
        long courseId = 1L;
        long studentId = 1L;
        Course courseMock = mock(Course.class);
        Student studentMock = mock(Student.class);
        Set<Student> courseStudents = mock(Set.class);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(studentMock));
        when(courseMock.getStudents()).thenReturn(courseStudents);
        when(courseStudents.contains(studentMock)).thenReturn(false);

        courseService.enrollStudentInCourse(courseId, studentId);

        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findById(studentId);
        verify(courseMock, times(1)).addStudent(studentMock);
    }

    @Test
    void enrollAllStudentsFromGroupInCourse_shouldEntityNotFoundException_whenNoCourseWasFoundForGivenCourseId() {
        long courseId = 1L;
        long groupId = 1L;
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.enrollAllStudentsFromGroupInCourse(courseId, groupId));
        verify(courseRepositoryMock, times(1)).findById(courseId);
    }

    @Test
    void enrollAllStudentsFromGroupInCourse_shouldNotAddAnyStudentToSetOfCourseStudents_whenNoStudentWithGivenGroupIdWasFound() {
        long courseId = 1L;
        long groupId = 1L;
        Course courseMock = mock(Course.class);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findByGroupId(groupId)).thenReturn(new ArrayList<>());

        courseService.enrollAllStudentsFromGroupInCourse(courseId, groupId);

        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findByGroupId(groupId);
        verify(courseMock, never()).addStudent(any(Student.class));
    }

    @Test
    void enrollAllStudentsFromGroupInCourse_shouldAddStudentsToSetOfStudentsEnrolledInCourse_whenFoundStudentsWithGivenGroupId() {
        long courseId = 1L;
        long groupId = 1L;
        Course courseMock = mock(Course.class);
        Student firstStudentMock = mock(Student.class);
        Student secondStudentMock = mock(Student.class);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(courseMock));
        when(studentRepositoryMock.findByGroupId(groupId)).thenReturn(List.of(firstStudentMock, secondStudentMock));

        courseService.enrollAllStudentsFromGroupInCourse(courseId, groupId);

        verify(courseRepositoryMock, times(1)).findById(courseId);
        verify(studentRepositoryMock, times(1)).findByGroupId(groupId);
        verify(courseMock, times(2)).addStudent(any(Student.class));
    }

}
