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
import ua.foxminded.universitycms.dto.StudentDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.StudentService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentServiceMock;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Test
    void getPageWithStudents_shouldReturnPageWithStudentsThatFoundByKeyword_whenKeywordNotNullAndNotBlank() throws Exception {
        String keyword = "FirstName1 LastName1";
        List<String> allNamesOfStudents = getAllNamesOfStudentsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAllNamesOfStudents()).thenReturn(allNamesOfStudents);
        when(studentServiceMock.getStudentInPageByName(keyword, pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getStudentInPageByName(keyword, pageable);
    }

    @Test
    void getPageWithStudents_shouldReturnPageWithAllStudents_whenKeywordIsNull() throws Exception {
        List<String> allNamesOfStudents = getAllNamesOfStudentsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAllNamesOfStudents()).thenReturn(allNamesOfStudents);
        when(studentServiceMock.getStudentsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getStudentsPage(pageable);
    }

    @Test
    void getPageWithStudents_shouldReturnPageWithAllStudents_whenKeywordIsBlank() throws Exception {
        String keyword = "               ";
        List<String> allNamesOfStudents = getAllNamesOfStudentsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAllNamesOfStudents()).thenReturn(allNamesOfStudents);
        when(studentServiceMock.getStudentsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("page"))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getStudentsPage(pageable);
    }

    @Test
    void getPageWithStudents_shouldReturnPageWithAllStudents_whenInvalidPageNumberAndPageSizeProvided() throws Exception {
        String invalidPageNumber = "-1";
        String invalidPageSize = "0";
        List<String> allNamesOfStudents = getAllNamesOfStudentsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAllNamesOfStudents()).thenReturn(allNamesOfStudents);
        when(studentServiceMock.getStudentsPage(pageable)).thenReturn(getEmptyPageForTest());

        mockMvc.perform(get("/ui/v1/students")
                .param("page", invalidPageNumber)
                .param("size", invalidPageSize))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attribute("page", DEFAULT_PAGE_NUMBER))
            .andExpect(model().attributeExists("totalItems"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("size", DEFAULT_PAGE_SIZE))
            .andExpect(model().attribute("hasError", false))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getStudentsPage(pageable);
    }

    @Test
    void getPageWithStudents_shouldSetHasErrorToTrue_whenServiceExceptionIsThrown() throws Exception {
        List<String> allNamesOfStudents = getAllNamesOfStudentsForTest();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        when(studentServiceMock.getAllNamesOfStudents()).thenReturn(allNamesOfStudents);
        when(studentServiceMock.getStudentsPage(pageable)).thenThrow(ServiceException.class);

        mockMvc.perform(get("/ui/v1/students"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attribute("hasError", true))
            .andExpect(view().name("students/all-students"));

        verify(studentServiceMock, times(1)).getStudentsPage(pageable);
    }

    private List<String> getAllNamesOfStudentsForTest() {
        return Arrays.asList("FirstName1 LastName1", "FirstName2 LastName2", "FirstName3 LastName3");
    }

    private Page<StudentDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
