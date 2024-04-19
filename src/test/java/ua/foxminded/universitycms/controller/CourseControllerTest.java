package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.CourseService;
import java.util.*;

@WebMvcTest(controllers = CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseServiceMock;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Test
    void getPageWithCourses_shouldReturnPageWithCoursesThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "CourseName1";
        List<String> allNamesOfCourses = getNamesOfCoursesForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(courseServiceMock.getAllNamesOfCourses()).thenReturn(allNamesOfCourses);
        when(courseServiceMock.getCourseByNameInPage(keyword, pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/courses")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("allNamesOfCourses"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getCourseByNameInPage(keyword, pageable);
    }

    @Test
    void getPageWithCourses_shouldReturnPageWithAllCourses_whenKeywordIsNull() throws Exception {
        List<String> allNamesOfCourses = getNamesOfCoursesForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(courseServiceMock.getAllNamesOfCourses()).thenReturn(allNamesOfCourses);
        when(courseServiceMock.getAllCoursesInPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/courses"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("allNamesOfCourses"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getAllCoursesInPage(pageable);
    }

    @Test
    void getPageWithCourses_shouldReturnPageWithAllCourses_whenKeywordIsBlank() throws Exception {
        String keyword = "       ";
        List<String> allNamesOfCourses = getNamesOfCoursesForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(courseServiceMock.getAllNamesOfCourses()).thenReturn(allNamesOfCourses);
        when(courseServiceMock.getAllCoursesInPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/courses")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("allNamesOfCourses"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getAllCoursesInPage(pageable);
    }

    @Test
    void getPageWithCourses_shouldReturnPageWithAllCourses_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        List<String> allNamesOfCourses = getNamesOfCoursesForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(courseServiceMock.getAllNamesOfCourses()).thenReturn(allNamesOfCourses);
        when(courseServiceMock.getAllCoursesInPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/courses")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("allNamesOfCourses"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getAllCoursesInPage(pageable);
    }

    @Test
    void getPageWithCourses_shouldSetHasErrorToTrue_whenServiceExceptionIsThrown() throws Exception {
        List<String> allNamesOfCourses = getNamesOfCoursesForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(courseServiceMock.getAllNamesOfCourses()).thenReturn(allNamesOfCourses);
        when(courseServiceMock.getAllCoursesInPage(pageable)).thenThrow(ServiceException.class);

        mockMvc.perform(get("/ui/v1/courses"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("allNamesOfCourses"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(model().attribute("hasError", true))
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getAllCoursesInPage(pageable);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithUserCourseFoundByName_whenKeywordNotNullAndNotBlank() throws Exception {
        long studentId = 60L;
        String keyword = "CourseName1";
        List<String> userCoursesNames = getNamesOfCoursesForTest();
        when(courseServiceMock.getStudentCoursesNames(studentId)).thenReturn(userCoursesNames);
        when(courseServiceMock.getStudentCourseByCourseName(studentId, keyword)).thenReturn(getEmptyCoursesListForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("courses/user-courses"));

        verify(courseServiceMock, times(1)).getStudentCourseByCourseName(studentId, keyword);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithAllUserCourses_whenKeywordIsNull() throws Exception {
        long studentId = 60L;
        List<String> userCoursesNames = getNamesOfCoursesForTest();
        when(courseServiceMock.getStudentCoursesNames(studentId)).thenReturn(userCoursesNames);
        when(courseServiceMock.getStudentCourses(studentId)).thenReturn(getEmptyCoursesListForTest());

        mockMvc.perform(get("/ui/v1/courses/my"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("courses/user-courses"));

        verify(courseServiceMock, times(1)).getStudentCourses(studentId);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithAllUserCourses_whenKeywordIsBlank() throws Exception {
        long studentId = 60L;
        String keyword = "";
        List<String> userCoursesNames = getNamesOfCoursesForTest();
        when(courseServiceMock.getStudentCoursesNames(studentId)).thenReturn(userCoursesNames);
        when(courseServiceMock.getStudentCourses(studentId)).thenReturn(getEmptyCoursesListForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("courses/user-courses"));

        verify(courseServiceMock, times(1)).getStudentCourses(studentId);
    }

    @Test
    void getPageWithCoursesForUser_shouldSetHasErrorToTrue_whenServiceExceptionIsThrown() throws Exception {
        long studentId = 60L;
        when(courseServiceMock.getStudentCoursesNames(studentId)).thenThrow(ServiceException.class);

        mockMvc.perform(get("/ui/v1/courses/my"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attribute("hasError", true))
            .andExpect(view().name("courses/user-courses"));

        verify(courseServiceMock, times(1)).getStudentCoursesNames(studentId);
    }

    @Test
    void getPageWithSpecificCourse_shouldReturnPageWithSpecificCourse_whenCourseWithProvidedIdExists() throws Exception {
        long courseId = 1L;
        long teacherId = 1L;
        CourseDto course = new CourseDto("CourseName", "Description", teacherId);
        course.setId(courseId);
        course.setTopics(new HashSet<>());
        when(courseServiceMock.getById(courseId)).thenReturn(Optional.of(course));
        when(courseServiceMock.getAuthorFullNameByTeacherId(teacherId)).thenReturn("AuthorName");

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}", courseId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("course"))
            .andExpect(model().attributeExists("authorName"))
            .andExpect(view().name("courses/course"));

        verify(courseServiceMock, times(1)).getById(courseId);
        verify(courseServiceMock, times(1)).getAuthorFullNameByTeacherId(teacherId);
    }

    @Test
    void getPageWithSpecificCourse_shouldSetHasErrorToTrue_whenCourseWithProvidedIdDoesNotExist() throws Exception {
        long courseId = 1L;
        when(courseServiceMock.getById(courseId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}", courseId))
            .andExpect(status().isOk())
            .andExpect(model().attribute("hasError", true))
            .andExpect(view().name("courses/course"));

        verify(courseServiceMock, times(1)).getById(courseId);
    }

    private List<String> getNamesOfCoursesForTest() {
        return Arrays.asList("CourseName1", "CourseName2", "CourseName2");
    }

    private List<CourseDto> getEmptyCoursesListForTest() {
        return new ArrayList<>();
    }

    private Page<CourseDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
