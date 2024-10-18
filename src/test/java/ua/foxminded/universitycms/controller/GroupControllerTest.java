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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.TestConfiguration;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.service.GroupService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = GroupController.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Import(SecurityConfig.class)
class GroupControllerTest {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupServiceMock;

    @Test
    @WithMockUser(authorities = {"GROUPS_READ"})
    void getPageWithGroups_shouldReturnPageWithGroupsThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "GroupName";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAll()).thenReturn(getAllGroupsForTest());
        when(groupServiceMock.getGroupInPageByName(keyword, pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/groups")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupInPageByName(keyword, pageable);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_READ"})
    void getPageWithGroups_shouldReturnPageWitAllGroups_whenKeywordIsNull() throws Exception {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAll()).thenReturn(getAllGroupsForTest());
        when(groupServiceMock.getGroupsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/groups"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupsPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_READ"})
    void getPageWithGroups_shouldReturnPageWitAllGroups_whenKeywordIsBlank() throws Exception {
        String keyword = "       ";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAll()).thenReturn(getAllGroupsForTest());
        when(groupServiceMock.getGroupsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/groups")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", ""))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupsPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_READ"})
    void getPageWithGroups_shouldReturnPageWitAllGroups_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAll()).thenReturn(getAllGroupsForTest());
        when(groupServiceMock.getGroupsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/groups")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupsPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_READ"})
    void getPageWithGroupsForEnrollInCourse_shouldPageWithGroupThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "HT-09";
        long courseId = 1L;
        when(groupServiceMock.getListOfGroupsWhoseStudentsNotEnrolledInCourse(courseId)).thenReturn(getAllGroupsForTest());

        mockMvc.perform(get("/ui/v1/groups/for-enroll")
                .param("keyword", keyword)
                .param("cid", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("allNamesOfGroups"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attributeExists("courseId"));

        verify(groupServiceMock, times(1)).getListOfGroupsWhoseStudentsNotEnrolledInCourse(courseId);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_READ"})
    void getPageWithGroupsForEnrollInCourse_shouldPageWithGroupsForEnroll_whenKeywordIsBlank() throws Exception {
        String keyword = "    ";
        long courseId = 1L;
        when(groupServiceMock.getListOfGroupsWhoseStudentsNotEnrolledInCourse(courseId)).thenReturn(getAllGroupsForTest());

        mockMvc.perform(get("/ui/v1/groups/for-enroll")
                .param("keyword", keyword)
                .param("cid", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("allNamesOfGroups"))
            .andExpect(model().attribute("keyword", ""))
            .andExpect(model().attributeExists("courseId"));

        verify(groupServiceMock, times(1)).getListOfGroupsWhoseStudentsNotEnrolledInCourse(courseId);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_READ"})
    void getPageWithGroupsForEnrollInCourse_shouldPageWithGroupsForEnroll_whenKeywordIsNull() throws Exception {
        long courseId = 1L;
        when(groupServiceMock.getListOfGroupsWhoseStudentsNotEnrolledInCourse(courseId)).thenReturn(getAllGroupsForTest());

        mockMvc.perform(get("/ui/v1/groups/for-enroll")
                .param("cid", String.valueOf(courseId)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("allNamesOfGroups"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(model().attributeExists("courseId"));

        verify(groupServiceMock, times(1)).getListOfGroupsWhoseStudentsNotEnrolledInCourse(courseId);
    }

    @Test
    @WithMockUser(authorities = "GROUPS_READ")
    void getPageWithSpecificGroup_shouldReturnPageWithSpecificGroup_whenGroupWithGivenIdExists() throws Exception {
        long groupId = 1;
        GroupDto groupMock = mock(GroupDto.class);
        when(groupServiceMock.getById(groupId)).thenReturn(Optional.of(groupMock));

        mockMvc.perform(get("/ui/v1/groups/{groupId}", groupId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("group"))
            .andExpect(view().name("groups/specific-group"));

        verify(groupServiceMock, times(1)).getById(groupId);
    }

    @Test
    @WithMockUser(authorities = "GROUPS_READ")
    void getPageWithSpecificGroup_shouldPageWithErrorMessage_whenGroupWithGivenIdDoesNotExist() throws Exception {
        long groupId = 1;
        when(groupServiceMock.getById(groupId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/groups/{groupId}", groupId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(groupServiceMock, times(1)).getById(groupId);
    }

    @Test
    @WithMockUser(authorities = "GROUPS_CREATE")
    void getCreationForm_shouldPageWithFormToCreateNewGroup_whenRequestMadeForCorrectUrl() throws Exception {
        mockMvc.perform(get("/ui/v1/groups/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("group"))
            .andExpect(view().name("groups/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_CREATE"})
    void performGroupCreation_shouldSaveNewGroupAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String groupName = "LS-09";

        mockMvc.perform(post("/ui/v1/groups/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("groupName", groupName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/groups"));

        verify(groupServiceMock, times(1)).save(any(GroupDto.class));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_CREATE"})
    void performGroupCreation_shouldPageWithCreationForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        mockMvc.perform(post("/ui/v1/groups/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_CREATE"})
    void performGroupCreation_shouldPageWithCreationForm_whenBindingResultHasErrors() throws Exception {
        String groupName = "invalidGroupNameFormat";

        mockMvc.perform(post("/ui/v1/groups/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("groupName", groupName))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/creation-form"));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_UPDATE"})
    void getUpdateForm_shouldPageWithFormToUpdateExistentGroup_whenGroupWithGivenIdExists() throws Exception {
        long groupId = 1L;
        GroupDto group = mock(GroupDto.class);
        when(groupServiceMock.getById(groupId)).thenReturn(Optional.of(group));

        mockMvc.perform(get("/ui/v1/groups/{groupId}/edit", groupId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("group"))
            .andExpect(view().name("groups/update-form"));

        verify(groupServiceMock, times(1)).getById(groupId);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_UPDATE"})
    void getUpdateForm_shouldPageWithErrorMessage_whenGroupWithGivenIdDoesNotExist() throws Exception {
        long groupId = 1L;
        when(groupServiceMock.getById(groupId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/groups/{groupId}/edit", groupId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(groupServiceMock, times(1)).getById(groupId);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_UPDATE"})
    void performGroupUpdate_shouldUpdateGroupAndRedirectToAnotherUrl_whenAllRequiredFieldsAreFilled() throws Exception {
        String groupName = "HL-09";

        mockMvc.perform(put("/ui/v1/groups/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("groupName", groupName))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/groups"));

        verify(groupServiceMock, times(1)).update(any(GroupDto.class));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_UPDATE"})
    void performGroupUpdate_shouldPageWithUpdateForm_whenNotAllRequiredFieldsAreFilled() throws Exception {
        mockMvc.perform(put("/ui/v1/groups/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/update-form"));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_UPDATE"})
    void performGroupUpdate_shouldPageWithUpdateForm_whenBindingResultHasErrors() throws Exception {
        String groupName = "invalidGroupNameFormat";

        mockMvc.perform(put("/ui/v1/groups/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("groupName", groupName))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/update-form"));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_UPDATE"})
    void performGroupUpdate_shouldPageWithErrorMessage_whenGroupServiceThrowEntityNotFoundException() throws Exception {
        String groupName = "RT-98";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(groupServiceMock.update(any(GroupDto.class))).thenThrow(entityNotFoundExceptionMock);

        mockMvc.perform(put("/ui/v1/groups/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("groupName", groupName))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(groupServiceMock, times(1)).update(any(GroupDto.class));
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_DELETE"})
    void performGroupDeletion_shouldDeleteGroupAndRedirectToAnotherUrl_whenGroupWithGivenIdExist() throws Exception {
        long groupId = 1L;

        mockMvc.perform(delete("/ui/v1/groups/{groupId}/delete", groupId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ui/v1/groups"));

        verify(groupServiceMock, times(1)).deleteById(groupId);
    }

    @Test
    @WithMockUser(authorities = {"GROUPS_DELETE"})
    void performGroupDeletion_shouldPageWithErrorMessage_whenGroupServiceThrowEntityNotFoundException() throws Exception {
        long groupId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        EntityNotFoundException entityNotFoundExceptionMock = mock(EntityNotFoundException.class);
        when(entityNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        doThrow(entityNotFoundExceptionMock).when(groupServiceMock).deleteById(groupId);

        mockMvc.perform(delete("/ui/v1/groups/{groupId}/delete", groupId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exception"))
            .andExpect(view().name("error-page"));

        verify(groupServiceMock, times(1)).deleteById(groupId);
    }

    private List<GroupDto> getAllGroupsForTest() {
        return new ArrayList<>();
    }

    private Page<GroupDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
