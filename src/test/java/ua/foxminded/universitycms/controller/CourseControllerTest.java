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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.universitycms.config.SecurityConfig;
import ua.foxminded.universitycms.dto.CourseDto;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.CourseService;
import java.util.*;

@WebMvcTest(controllers = CourseController.class)
@Import(SecurityConfig.class)
class CourseControllerTest {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseServiceMock;

    @Mock
    private CustomUserDetails customUserDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(authorities = {"COURSES_READ"})
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
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getCourseByNameInPage(keyword, pageable);
    }

    @Test
    @WithMockUser(authorities = {"COURSES_READ"})
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
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getAllCoursesInPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"COURSES_READ"})
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
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getAllCoursesInPage(pageable);
    }

    @Test
    @WithMockUser(authorities = {"COURSES_READ"})
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
            .andExpect(view().name("courses/all-courses"));

        verify(courseServiceMock, times(1)).getAllCoursesInPage(pageable);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithTeacherCourseFoundByName_whenLoggedInUserHasTeacherRoleAndKeywordNotNullAndNotBlank() throws Exception {
        long teacherId = 1L;
        String keyword = "CourseName1";
        List<CourseDto> teacherCourses = getEmptyCoursesListForTest();
        when(customUserDetails.getId()).thenReturn(teacherId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.TEACHER);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(courseServiceMock.getTeacherCourseByCourseName(teacherId, keyword)).thenReturn(teacherCourses);
        when(courseServiceMock.getCoursesNames(teacherCourses)).thenReturn(getNamesOfCoursesForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .param("keyword", keyword)
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("courses/user-courses"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getTeacherCourseByCourseName(teacherId, keyword);
        verify(courseServiceMock, times(1)).getCoursesNames(teacherCourses);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithAllTeacherCourses_whenLoggedInUserHasTeacherRoleAndKeywordIsNull() throws Exception {
        long teacherId = 1L;
        List<CourseDto> teacherCourses = getEmptyCoursesListForTest();
        when(customUserDetails.getId()).thenReturn(teacherId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.TEACHER);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(courseServiceMock.getTeacherCourses(teacherId)).thenReturn(teacherCourses);
        when(courseServiceMock.getCoursesNames(teacherCourses)).thenReturn(getNamesOfCoursesForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(view().name("courses/user-courses"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getTeacherCourses(teacherId);
        verify(courseServiceMock, times(1)).getCoursesNames(teacherCourses);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithAllTeacherCourses_whenLoggedInUserHasTeacherRoleAndKeywordIsBlank() throws Exception {
        long teacherId = 1L;
        String keyword = "";
        List<CourseDto> teacherCourses = getEmptyCoursesListForTest();
        when(customUserDetails.getId()).thenReturn(teacherId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.TEACHER);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(courseServiceMock.getTeacherCourses(teacherId)).thenReturn(teacherCourses);
        when(courseServiceMock.getCoursesNames(teacherCourses)).thenReturn(getNamesOfCoursesForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .param("keyword", keyword)
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("courses/user-courses"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getTeacherCourses(teacherId);
        verify(courseServiceMock, times(1)).getCoursesNames(teacherCourses);
    }

    @Test
    void getPageWithCoursesForUser_shouldThrowCustomHttpException_whenLoggedInUserHasTeacherRoleAndUserNotFoundExceptionIsThrown() throws Exception {
        long teacherId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        UserNotFoundException userNotFoundExceptionMock = mock(UserNotFoundException.class);
        when(customUserDetails.getId()).thenReturn(teacherId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.TEACHER);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(userNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(courseServiceMock.getTeacherCourses(teacherId)).thenThrow(userNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/courses/my")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attribute("httpStatus", httpStatus))
            .andExpect(view().name("error"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getTeacherCourses(teacherId);
        verify(userNotFoundExceptionMock, times(1)).getHttpStatus();
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithStudentsCourseFoundByName_whenLoggedInUserHasStudentRoleAndKeywordNotNullAndNotBlank() throws Exception {
        long studentId = 1L;
        String keyword = "CourseName1";
        List<CourseDto> studentCourses = getEmptyCoursesListForTest();
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(courseServiceMock.getStudentCourseByCourseName(studentId, keyword)).thenReturn(studentCourses);
        when(courseServiceMock.getCoursesNames(studentCourses)).thenReturn(getNamesOfCoursesForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .param("keyword", keyword)
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("courses/user-courses"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getStudentCourseByCourseName(studentId, keyword);
        verify(courseServiceMock, times(1)).getCoursesNames(studentCourses);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithAllStudentCourses_whenLoggedInUserHasStudentRoleAndKeywordIsNull() throws Exception {
        long studentId = 1L;
        List<CourseDto> studentCourses = getEmptyCoursesListForTest();
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(courseServiceMock.getStudentCourses(studentId)).thenReturn(studentCourses);
        when(courseServiceMock.getCoursesNames(studentCourses)).thenReturn(getNamesOfCoursesForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attributeDoesNotExist("keyword"))
            .andExpect(view().name("courses/user-courses"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getStudentCourses(studentId);
        verify(courseServiceMock, times(1)).getCoursesNames(studentCourses);
    }

    @Test
    void getPageWithCoursesForUser_shouldReturnPageWithAllStudentCourses_whenLoggedInUserHasStudentRoleAndKeywordIsBlank() throws Exception {
        long studentId = 1L;
        String keyword = "";
        List<CourseDto> studentCourses = getEmptyCoursesListForTest();
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(courseServiceMock.getStudentCourses(studentId)).thenReturn(studentCourses);
        when(courseServiceMock.getCoursesNames(studentCourses)).thenReturn(getNamesOfCoursesForTest());

        mockMvc.perform(get("/ui/v1/courses/my")
                .param("keyword", keyword)
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userCoursesNames"))
            .andExpect(model().attributeExists("userCourses"))
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(view().name("courses/user-courses"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getStudentCourses(studentId);
        verify(courseServiceMock, times(1)).getCoursesNames(studentCourses);
    }

    @Test
    void getPageWithCoursesForUser_shouldThrowCustomHttpException_whenLoggedInUserHasStudentRoleAndUserNotFoundExceptionIsThrown() throws Exception {
        long studentId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        UserNotFoundException userNotFoundExceptionMock = mock(UserNotFoundException.class);
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(RoleName.STUDENT);
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("COURSES_READ")));
        when(userNotFoundExceptionMock.getHttpStatus()).thenReturn(httpStatus);
        when(courseServiceMock.getStudentCourses(studentId)).thenThrow(userNotFoundExceptionMock);

        mockMvc.perform(get("/ui/v1/courses/my")
                .with(user(customUserDetails)))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attribute("httpStatus", httpStatus))
            .andExpect(view().name("error"));

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(courseServiceMock, times(1)).getStudentCourses(studentId);
        verify(userNotFoundExceptionMock, times(1)).getHttpStatus();
    }

    @Test
    void getPageWithCoursesForUser_shouldForbiddenError_whenLoggedInUserDoesNotHaveRequiredPermission() throws Exception {
        when(customUserDetails.getAuthorities()).thenReturn((Set) Collections.singleton(new SimpleGrantedAuthority("SOME_PERMISSION")));

        mockMvc.perform(get("/ui/v1/courses/my")
                .with(user(customUserDetails)))
            .andExpect(status().isForbidden());

        verify(customUserDetails, never()).getId();
        verify(customUserDetails, never()).getRoleName();
        verify(customUserDetails, times(1)).getAuthorities();
    }

    @Test
    @WithMockUser(authorities = {"COURSES_READ"})
    void getPageWithSpecificCourse_shouldReturnPageWithSpecificCourse_whenCourseWithProvidedIdExists() throws Exception {
        long courseId = 1L;
        CourseDto course = mock(CourseDto.class);
        when(courseServiceMock.getById(courseId)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}", courseId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("course"))
            .andExpect(view().name("courses/course"));

        verify(courseServiceMock, times(1)).getById(courseId);
    }

    @Test
    @WithMockUser(authorities = {"COURSES_READ"})
    void getPageWithSpecificCourse_shouldThrowCustomHttpException_whenCourseWithProvidedIdDoesNotExist() throws Exception {
        long courseId = 1L;
        when(courseServiceMock.getById(courseId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/v1/courses/my/{courseId}", courseId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attribute("httpStatus", HttpStatus.NOT_FOUND))
            .andExpect(view().name("error"));

        verify(courseServiceMock, times(1)).getById(courseId);
    }

    private List<String> getNamesOfCoursesForTest() {
        return new ArrayList<>();
    }

    private List<CourseDto> getEmptyCoursesListForTest() {
        return new ArrayList<>();
    }

    private Page<CourseDto> getEmptyPageForTest() {
        return new PageImpl<>(new ArrayList<>());
    }

}
