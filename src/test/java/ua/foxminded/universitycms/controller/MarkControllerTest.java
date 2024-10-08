package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.service.MarkService;
import java.util.*;

@WebMvcTest(controllers = MarkController.class)
@Import(SecurityConfig.class)
class MarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkService markServiceMock;

    @Test
    @WithMockUser(authorities = "MARKS_READ")
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarksThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String studentFullName = "FullName";
        String keyword = "TopicName1";
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarksByTopicName(studentId, courseId, keyword)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/marks")
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName)
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("studentId"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeExists("studentFullName"))
            .andExpect(model().attributeExists("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("marks/student-marks"));

        verify(markServiceMock, times(1)).getNamesOfTopicsInCourse(courseId);
        verify(markServiceMock, times(1)).getStudentCourseMarksByTopicName(studentId, courseId, keyword);
    }

    @Test
    @WithMockUser(authorities = "MARKS_READ")
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarks_whenKeywordIsNull() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String studentFullName = "FullName";
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/marks")
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("studentId"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeExists("studentFullName"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("marks/student-marks"));

        verify(markServiceMock, times(1)).getNamesOfTopicsInCourse(courseId);
        verify(markServiceMock, times(1)).getStudentCourseMarks(studentId, courseId);
    }

    @Test
    @WithMockUser(authorities = "MARKS_READ")
    void getPageWithStudentMarks_shouldReturnPageWithStudentCourseMarks_whenKeywordIsBlank() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String studentFullName = "FullName";
        String blankKeyword = "       ";
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/marks")
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName)
                .param("keyword", blankKeyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("marks"))
            .andExpect(model().attributeExists("studentId"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeExists("studentFullName"))
            .andExpect(model().attributeExists("keyword"))
            .andExpect(model().attributeExists("namesOfTopics"))
            .andExpect(view().name("marks/student-marks"));

        verify(markServiceMock, times(1)).getNamesOfTopicsInCourse(courseId);
        verify(markServiceMock, times(1)).getStudentCourseMarks(studentId, courseId);
    }

    @Test
    @WithMockUser(authorities = "MARKS_READ")
    void getPageWithStudentMarks_should400ClientError_whenStudentFullNameParameterIsNull() throws Exception {
        long studentId = 1;
        long courseId = 1;
        when(markServiceMock.getNamesOfTopicsInCourse(courseId)).thenReturn(getAllNamesOfTopicsInCourseForTest());
        when(markServiceMock.getStudentCourseMarks(studentId, courseId)).thenReturn(getEmptyMarksListForTest());

        mockMvc.perform(get("/ui/v1/marks", courseId)
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = {"MARKS_CREATE"})
    void getCreationForm_shouldPageWithFormToCreateNewMark_whenRequestIsValid() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String studentFullName = "Full Name";
        when(markServiceMock.getUnratedTopics(studentId, courseId)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/ui/v1/marks/new")
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("mark"))
            .andExpect(model().attributeExists("studentId"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeExists("studentFullName"))
            .andExpect(model().attributeExists("unratedTopics"))
            .andExpect(view().name("marks/creation-form"));

        verify(markServiceMock, times(1)).getUnratedTopics(studentId, courseId);
    }

    @Test
    @WithMockUser(authorities = {"MARKS_CREATE"})
    void performMarkCreation_shouldSaveNewMarkAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        long studentId = 1;
        long topicId = 1;
        long courseId = 1;
        Integer markValue = 100;
        String studentFullName = "Full Name";

        mockMvc.perform(post("/ui/v1/marks/create")
                .with(csrf())
                .param("markValue", String.valueOf(markValue))
                .param("studentId", String.valueOf(studentId))
                .param("topicId", String.valueOf(topicId))
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/marks?sid=%s&cid=%s&fullName=%s", studentId, courseId, studentFullName)));

        verify(markServiceMock, times(1)).save(any(MarkDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MARKS_CREATE"})
    void performMarkCreation_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        long studentId = 1;
        long topicId = 1;
        long courseId = 1;
        String studentFullName = "Full Name";
        when(markServiceMock.getUnratedTopics(studentId, courseId)).thenReturn(new HashMap<>());

        mockMvc.perform(post("/ui/v1/marks/create")
                .with(csrf())
                .param("studentId", String.valueOf(studentId))
                .param("topicId", String.valueOf(topicId))
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studentId"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeExists("studentFullName"))
            .andExpect(model().attributeExists("unratedTopics"))
            .andExpect(view().name("marks/creation-form"));

        verify(markServiceMock, times(1)).getUnratedTopics(studentId, courseId);
    }

    @Test
    @WithMockUser(authorities = {"MARKS_UPDATE"})
    void getUpdateForm_shouldPageWithFormToUpdateExistentMark_whenMarkWithGivenIdExist() throws Exception {
        long studentId = 1;
        long courseId = 1;
        long markId = 1;
        String studentFullName = "Full Name";
        MarkDto markMock = mock(MarkDto.class);
        when(markServiceMock.getById(markId)).thenReturn(Optional.of(markMock));

        mockMvc.perform(get("/ui/v1/marks/{markId}/edit", markId)
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("mark"))
            .andExpect(model().attributeExists("studentId"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeExists("studentFullName"))
            .andExpect(view().name("marks/update-form"));

        verify(markServiceMock, times(1)).getById(markId);
    }

    @Test
    @WithMockUser(authorities = {"MARKS_UPDATE"})
    void getUpdateForm_shouldPageWithErrorMessage_whenMarkWithGivenIdDoesNotExist() throws Exception {
        long studentId = 1;
        long courseId = 1;
        long markId = 1;
        String studentFullName = "Full Name";
        when(markServiceMock.getById(markId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/marks/{markId}/edit", markId)
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(markServiceMock, times(1)).getById(markId);
    }

    @Test
    @WithMockUser(authorities = {"MARKS_UPDATE"})
    void performMarkUpdate_shouldUpdateMarkAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        long studentId = 1;
        long topicId = 1;
        long courseId = 1;
        Integer markValue = 100;
        String studentFullName = "Full Name";

        mockMvc.perform(put("/ui/v1/marks/update")
                .with(csrf())
                .param("markValue", String.valueOf(markValue))
                .param("studentId", String.valueOf(studentId))
                .param("topicId", String.valueOf(topicId))
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/marks?sid=%s&cid=%s&fullName=%s", studentId, courseId, studentFullName)));

        verify(markServiceMock, times(1)).update(any(MarkDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MARKS_UPDATE"})
    void performMarkUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        long studentId = 1;
        long courseId = 1;
        String studentFullName = "Full Name";

        mockMvc.perform(put("/ui/v1/marks/update")
                .with(csrf())
                .param("studentId", String.valueOf(studentId))
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("studentId"))
            .andExpect(model().attributeExists("courseId"))
            .andExpect(model().attributeExists("studentFullName"))
            .andExpect(view().name("marks/update-form"));
    }

    @Test
    @WithMockUser(authorities = {"MARKS_DELETE"})
    void performMarkDeletion_shouldDeleteMarkAndRedirectToAnotherUrl_whenMarkWithGivenIdExist() throws Exception {
        long studentId = 1;
        long courseId = 1;
        long markId = 1;
        String studentFullName = "Full Name";

        mockMvc.perform(delete("/ui/v1/marks/{markId}/delete", markId)
                .with(csrf())
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/marks?sid=%s&cid=%s&fullName=%s", studentId, courseId, studentFullName)));

        verify(markServiceMock, times(1)).deleteById(markId);
    }

    @Test
    @WithMockUser(authorities = {"MARKS_DELETE"})
    void performMarkDeletion_shouldPageWithErrorMessage_whenMarkServiceThrowEntityNotFoundException() throws Exception {
        long studentId = 1;
        long courseId = 1;
        long markId = 1;
        String studentFullName = "Full Name";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(markServiceMock).deleteById(markId);

        mockMvc.perform(delete("/ui/v1/marks/{markId}/delete", markId)
                .with(csrf())
                .param("cid", String.valueOf(courseId))
                .param("sid", String.valueOf(studentId))
                .param("fullName", studentFullName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(markServiceMock, times(1)).deleteById(markId);
    }

    private List<String> getAllNamesOfTopicsInCourseForTest() {
        return Arrays.asList("TopicName1", "TopicName2", "TopicName3", "TopicName4");
    }

    private List<MarkDto> getEmptyMarksListForTest() {
        return new ArrayList<>();
    }

}
