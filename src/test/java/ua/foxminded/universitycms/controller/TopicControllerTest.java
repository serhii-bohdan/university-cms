package ua.foxminded.universitycms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.ValidationException;
import ua.foxminded.universitycms.service.TopicService;

@WebMvcTest(controllers = TopicController.class)
@Import({SecurityConfig.class})
class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicServiceMock;

    @Test
    @WithMockUser(authorities = {"TOPICS_CREATE"})
    void getCreationForm_shouldPageWithFormToCreateNewTopic_whenRequestIsValid() throws Exception {
        long courseId = 1L;

        mockMvc.perform(get("/ui/v1/topics/new")
                .param("cid", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("topic"))
            .andExpect(view().name("topics/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_CREATE"})
    void performTopicCreation_shouldSaveNewTopicAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String topicName = "Name";
        String topicDescription = "Description";
        Integer topicOrder = 1;
        long courseId = 1L;

        mockMvc.perform(post("/ui/v1/topics/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("topicName", topicName)
                .param("topicDescription", topicDescription)
                .param("topicOrder", String.valueOf(topicOrder))
                .param("courseId", String.valueOf(courseId)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/courses/my/%s", courseId)));

        verify(topicServiceMock, times(1)).save(any(TopicDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_CREATE"})
    void performTopicCreation_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String topicDescription = "Description";
        Integer topicOrder = 1;
        long courseId = 1L;

        mockMvc.perform(post("/ui/v1/topics/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("topicDescription", topicDescription)
                .param("topicOrder", String.valueOf(topicOrder))
                .param("courseId", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(view().name("topics/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_CREATE"})
    void performTopicCreation_shouldPageWithCreationFormAndWithErrorMessage_whenTopicServiceThrowValidationException() throws Exception {
        String topicName = "Name";
        String topicDescription = "Description";
        Integer topicOrder = 1;
        long courseId = 1L;
        when(topicServiceMock.save(any(TopicDto.class))).thenThrow(ValidationException.class);

        mockMvc.perform(post("/ui/v1/topics/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("topicName", topicName)
                .param("topicDescription", topicDescription)
                .param("topicOrder", String.valueOf(topicOrder))
                .param("courseId", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("validationErrorMessage"))
            .andExpect(view().name("topics/creation-form"));

        verify(topicServiceMock, times(1)).save(any(TopicDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_UPDATE"})
    void getUpdateForm_shouldPageWithFormToUpdateExistentTopic_whenTopicWithGivenIdExists() throws Exception {
        long topicId = 1L;
        TopicDto topicMock = mock(TopicDto.class);
        when(topicServiceMock.getById(topicId)).thenReturn(Optional.of(topicMock));

        mockMvc.perform(get("/ui/v1/topics/{topicId}/edit", topicId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("topic"))
            .andExpect(view().name("topics/update-form"));

        verify(topicServiceMock, times(1)).getById(topicId);
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_UPDATE"})
    void getUpdateForm_shouldPageWithErrorMessage_whenTopicWithGivenIdDoesNotExist() throws Exception {
        long topicId = 1L;
        when(topicServiceMock.getById(topicId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/topics/{topicId}/edit", topicId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(topicServiceMock, times(1)).getById(topicId);
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_UPDATE"})
    void performTopicUpdate_shouldUpdateTopicAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String topicName = "Name";
        String topicDescription = "Description";
        Integer topicOrder = 1;
        long courseId = 1L;

        mockMvc.perform(put("/ui/v1/topics/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("topicName", topicName)
                .param("topicDescription", topicDescription)
                .param("topicOrder", String.valueOf(topicOrder))
                .param("courseId", String.valueOf(courseId)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/courses/my/%s", courseId)));

        verify(topicServiceMock, times(1)).update(any(TopicDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_UPDATE"})
    void performTopicUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        String topicDescription = "Description";
        Integer topicOrder = 1;
        long courseId = 1L;

        mockMvc.perform(put("/ui/v1/topics/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("topicDescription", topicDescription)
                .param("topicOrder", String.valueOf(topicOrder))
                .param("courseId", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(view().name("topics/update-form"));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_UPDATE"})
    void performTopicUpdate_shouldPageWithUpdateFormAndWithErrorMessage_whenTopicServiceThrowValidationException() throws Exception {
        String topicName = "Name";
        String topicDescription = "Description";
        Integer topicOrder = 1;
        long courseId = 1L;
        when(topicServiceMock.update(any(TopicDto.class))).thenThrow(ValidationException.class);

        mockMvc.perform(put("/ui/v1/topics/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("topicName", topicName)
                .param("topicDescription", topicDescription)
                .param("topicOrder", String.valueOf(topicOrder))
                .param("courseId", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("validationErrorMessage"))
            .andExpect(view().name("topics/update-form"));

        verify(topicServiceMock, times(1)).update(any(TopicDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_UPDATE"})
    void performTopicUpdate_shouldPageWithErrorMessage_whenTopicServiceThrowEntityNotFoundException() throws Exception {
        String topicName = "Name";
        String topicDescription = "Description";
        Integer topicOrder = 1;
        long courseId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(topicServiceMock.update(any(TopicDto.class))).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(put("/ui/v1/topics/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("topicName", topicName)
                .param("topicDescription", topicDescription)
                .param("topicOrder", String.valueOf(topicOrder))
                .param("courseId", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(topicServiceMock, times(1)).update(any(TopicDto.class));
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_DELETE"})
    void performTopicDeletion_shouldDeleteTopicAndRedirectToAnotherUrl_whenTopicWithGivenIdExist() throws Exception {
        long topicId = 1L;
        long courseId = 1L;

        mockMvc.perform(delete("/ui/v1/topics/{topicId}/delete", topicId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("cid", String.valueOf(courseId)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(String.format("/ui/v1/courses/my/%s", courseId)));

        verify(topicServiceMock, times(1)).deleteById(topicId);
    }

    @Test
    @WithMockUser(authorities = {"TOPICS_DELETE"})
    void performTopicDeletion_shouldPageWithErrorMessage_whenTopicServiceThrowEntityNotFoundException() throws Exception {
        long topicId = 1L;
        long courseId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(topicServiceMock).deleteById(topicId);

        mockMvc.perform(delete("/ui/v1/topics/{topicId}/delete", topicId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("cid", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(topicServiceMock, times(1)).deleteById(topicId);
    }

}
