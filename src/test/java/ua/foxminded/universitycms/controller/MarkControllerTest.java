package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.service.MarkService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = MarkController.class)
class MarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkService markServiceMock;

    @Test
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarksThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        long studentId = 60;
        long courseId = 1;
        String courseName = "CourseName1";
        String keyword = "TopicName1";
        List<String> namesOfTopics = getAllNamesOfTopicsInCourseForTest();
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(namesOfTopics);
        when(markServiceMock.getStudentCourseMarksByTopicName(studentId, courseId, keyword)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .param("courseName", courseName)
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("courseName"))
            .andExpect(model().attributeExists("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("courses/marks"));

        verify(markServiceMock, times(1)).getStudentCourseMarksByTopicName(studentId, courseId, keyword);
    }

    @Test
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarks_whenKeywordIsNull() throws Exception {
        long studentId = 60;
        long courseId = 1;
        String courseName = "CourseName1";
        List<String> namesOfTopics = getAllNamesOfTopicsInCourseForTest();
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(namesOfTopics);
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .param("courseName", courseName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("courseName"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("courses/marks"));

        verify(markServiceMock, times(1)).getStudentCourseMarks(studentId, courseId);
    }

    @Test
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarks_whenKeywordIsBlank() throws Exception {
        long studentId = 60;
        long courseId = 1;
        String courseName = "CourseName1";
        String blankKeyword = "       ";
        List<String> namesOfTopics = getAllNamesOfTopicsInCourseForTest();
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(namesOfTopics);
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .param("courseName", courseName)
                .param("keyword", blankKeyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("courseName"))
            .andExpect(model().attributeExists("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("courses/marks"));

        verify(markServiceMock, times(1)).getStudentCourseMarks(studentId, courseId);
    }

    private List<String> getAllNamesOfTopicsInCourseForTest() {
        return Arrays.asList("TopicName1", "TopicName2", "TopicName3", "TopicName4");
    }

    private List<MarkDto> getEmptyMarksListForTest() {
        return new ArrayList<>();
    }

}
