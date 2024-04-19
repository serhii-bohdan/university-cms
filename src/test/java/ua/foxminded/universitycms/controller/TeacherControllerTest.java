package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
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
import ua.foxminded.universitycms.dto.TeacherDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.TeacherService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherServiceMock;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Test
    void getPageWithTeachers_shouldReturnPageWithTeachersThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "FirstName1 LastName1";
        List<String> allNamesOfTeachers = getAllNamesOfTeachersForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAllNamesOfTeachers()).thenReturn(allNamesOfTeachers);
        when(teacherServiceMock.getTeacherInPageByName(keyword, pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeacherInPageByName(keyword, pageable);
    }

    @Test
    void getPageWithTeachers_shouldReturnPageWithAllTeachers_whenKeywordIsNull() throws Exception {
        List<String> allNamesOfTeachers = getAllNamesOfTeachersForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAllNamesOfTeachers()).thenReturn(allNamesOfTeachers);
        when(teacherServiceMock.getTeachersPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeachersPage(pageable);
    }

    @Test
    void getPageWithTeachers_shouldReturnPageWithAllTeachers_whenKeywordIsBlank() throws Exception {
        String keyword = "               ";
        List<String> allNamesOfTeachers = getAllNamesOfTeachersForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAllNamesOfTeachers()).thenReturn(allNamesOfTeachers);
        when(teacherServiceMock.getTeachersPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeachersPage(pageable);
    }

    @Test
    void getPageWithTeachers_shouldReturnPageWithAllTeachers_whenValidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        List<String> allNamesOfTeachers = getAllNamesOfTeachersForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAllNamesOfTeachers()).thenReturn(allNamesOfTeachers);
        when(teacherServiceMock.getTeachersPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/teachers")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeachersPage(pageable);
    }

    @Test
    void getPageWithTeachers_shouldSetHasErrorToTrue_whenServiceExceptionIsThrown() throws Exception {
        List<String> allNamesOfTeachers = getAllNamesOfTeachersForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(teacherServiceMock.getAllNamesOfTeachers()).thenReturn(allNamesOfTeachers);
        when(teacherServiceMock.getTeachersPage(pageable)).thenThrow(ServiceException.class);

        mockMvc.perform(get("/ui/v1/teachers"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attribute("hasError", true))
            .andExpect(view().name("teachers/all-teachers"));

        verify(teacherServiceMock, times(1)).getTeachersPage(pageable);
    }

    private List<String> getAllNamesOfTeachersForTest() {
        return Arrays.asList("FirstName1 LastName1", "FirstName2 LastName2", "FirstName3 LastName3");
    }

    private Page<TeacherDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
