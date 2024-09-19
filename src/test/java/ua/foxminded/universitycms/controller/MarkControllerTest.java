package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.MarkService;
import java.util.*;

@WebMvcTest(controllers = MarkController.class)
@Import(SecurityConfig.class)
class MarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkService markServiceMock;

    @Mock
    private CustomUserDetails customUserDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarksThatFoundByKeyword_whenLoggedInUserHasStudentRoleAndKeywordNotNullAndNotBlank() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String courseName = "CourseName1";
        String keyword = "TopicName1";
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("MARKS_READ")));
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarksByTopicName(studentId, courseId, keyword)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .param("courseName", courseName)
                .param("keyword", keyword)
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("courseName"))
            .andExpect(model().attributeExists("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("courses/student-marks"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(markServiceMock, times(1)).getStudentCourseMarksByTopicName(studentId, courseId, keyword);
    }

    @Test
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarks_whenLoggedInUserHasStudentRoleAndKeywordIsNull() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String courseName = "CourseName1";
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("MARKS_READ")));
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .param("courseName", courseName)
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("courseName"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("courses/student-marks"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(markServiceMock, times(1)).getStudentCourseMarks(studentId, courseId);
    }

    @Test
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarks_whenLoggedInUserHasStudentRoleAndKeywordIsBlank() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String courseName = "CourseName1";
        String blankKeyword = "       ";
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("MARKS_READ")));
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .param("courseName", courseName)
                .param("keyword", blankKeyword)
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("courseName"))
            .andExpect(model().attributeExists("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("courses/student-marks"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(markServiceMock, times(1)).getStudentCourseMarks(studentId, courseId);
    }

    @Test
    void getPageWithStudentMarks_should400ClientError_whenCourseNameParameterIsNull() throws Exception {
        long studentId = 1;
        long courseId = 1;
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("MARKS_READ")));
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .with(user(customUserDetails)))
            .andExpect(status().is4xxClientError());

        verify(customUserDetails, never()).getId();
        verify(customUserDetails, never()).getRoleName();
    }

    @Test
    void getPageWithStudentMarks_shouldForbiddenError_whenLoggedInUserDoesNotHaveRequiredPermission() throws Exception {
        long courseId = 1;
        String courseName = "CourseName1";
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SOME_PERMISSION")));

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}/marks", courseId)
                .param("courseName", courseName)
                .with(user(customUserDetails)))
            .andExpect(status().isForbidden());

        verify(customUserDetails, never()).getId();
        verify(customUserDetails, never()).getRoleName();
    }

    private List<String> getAllNamesOfTopicsInCourseForTest() {
        return Arrays.asList("TopicName1", "TopicName2", "TopicName3", "TopicName4");
    }

    private List<MarkDto> getEmptyMarksListForTest() {
        return new ArrayList<>();
    }

}
