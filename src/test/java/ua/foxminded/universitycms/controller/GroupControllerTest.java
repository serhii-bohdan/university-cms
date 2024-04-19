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
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.GroupService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupServiceMock;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Test
    void getPageWithGroups_shouldReturnPageWithGroupsThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "GroupName";
        List<String> allNamesOfGroups = getAllNamesOfGroupsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAllNamesOfGroups()).thenReturn(allNamesOfGroups);
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
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupInPageByName(keyword, pageable);
    }

    @Test
    void getPageWithGroups_shouldReturnPageWitAllGroups_whenKeywordIsNull() throws Exception {
        List<String> allNamesOfGroups = getAllNamesOfGroupsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAllNamesOfGroups()).thenReturn(allNamesOfGroups);
        when(groupServiceMock.getGroupsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/groups"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupsPage(pageable);
    }

    @Test
    void getPageWithGroups_shouldReturnPageWitAllGroups_whenKeywordIsBlank() throws Exception {
        String keyword = "       ";
        List<String> allNamesOfGroups = getAllNamesOfGroupsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAllNamesOfGroups()).thenReturn(allNamesOfGroups);
        when(groupServiceMock.getGroupsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/groups")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupsPage(pageable);
    }

    @Test
    void getPageWithGroups_shouldReturnPageWitAllGroups_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        List<String> allNamesOfGroups = getAllNamesOfGroupsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAllNamesOfGroups()).thenReturn(allNamesOfGroups);
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
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupsPage(pageable);
    }

    @Test
    void getPageWithGroups_shouldSetHasErrorToTrue_whenServiceExceptionIsThrown() throws Exception {
        List<String> allNamesOfGroups = getAllNamesOfGroupsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(groupServiceMock.getAllNamesOfGroups()).thenReturn(allNamesOfGroups);
        when(groupServiceMock.getGroupsPage(pageable)).thenThrow(ServiceException.class);

        mockMvc.perform(get("/ui/v1/groups"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attribute("hasError", true))
            .andExpect(view().name("groups/all-groups"));

        verify(groupServiceMock, times(1)).getGroupsPage(pageable);
    }

    private List<String> getAllNamesOfGroupsForTest() {
        return Arrays.asList("GroupName1", "GroupName2", "GroupName1");
    }

    private Page<GroupDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
