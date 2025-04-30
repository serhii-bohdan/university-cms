package ua.foxminded.universitycms.service.impl;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.universitycms.dto.*;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.*;

@SpringBootTest(classes = {AuthorizedUserServiceImpl.class})
class AuthorizedUserServiceImplTest {

    @MockBean
    private AdminService adminService;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private StudentService studentService;

    @Mock
    private CustomUserDetails customUserDetails;

    @Autowired
    private AuthorizedUserService authorizedUserService;

    @Test
    void findUserByUserDetails_shouldCallMethodFromAdminServiceAndReturnObjectOfTypeAdminDto_whenCustomUserDetailsObjectHasAdminRole() {
        long adminId = 1;
        RoleName role = RoleName.ADMIN;
        AdminDto admin = mock(AdminDto.class);
        when(customUserDetails.getId()).thenReturn(adminId);
        when(customUserDetails.getRoleName()).thenReturn(role);
        when(adminService.getById(adminId)).thenReturn(admin);

        authorizedUserService.findUserByUserDetails(customUserDetails);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(adminService, times(1)).getById(adminId);
    }

    @Test
    void findUserByUserDetails_shouldCallMethodFromManagerServiceAndReturnObjectOfTypeManagerDto_whenCustomUserDetailsObjectHasManagerRole() {
        long managerId = 1;
        RoleName role = RoleName.MANAGER;
        ManagerDto manager = mock(ManagerDto.class);
        when(customUserDetails.getId()).thenReturn(managerId);
        when(customUserDetails.getRoleName()).thenReturn(role);
        when(managerService.getById(managerId)).thenReturn(manager);

        authorizedUserService.findUserByUserDetails(customUserDetails);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(managerService, times(1)).getById(managerId);
    }

    @Test
    void findUserByUserDetails_shouldCallMethodFromTeacherServiceAndReturnObjectOfTypeTeacherDto_whenCustomUserDetailsObjectHasTeacherRole() {
        long teacherId = 1;
        RoleName role = RoleName.TEACHER;
        TeacherDto teacher = mock(TeacherDto.class);
        when(customUserDetails.getId()).thenReturn(teacherId);
        when(customUserDetails.getRoleName()).thenReturn(role);
        when(teacherService.getById(teacherId)).thenReturn(teacher);

        authorizedUserService.findUserByUserDetails(customUserDetails);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(teacherService, times(1)).getById(teacherId);
    }

    @Test
    void findUserByUserDetails_shouldCallMethodFromStudentServiceAndReturnObjectOfTypeStudentDto_whenCustomUserDetailsObjectHasStudentRole() {
        long studentId = 1;
        RoleName role = RoleName.STUDENT;
        StudentDto teacher = mock(StudentDto.class);
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(role);
        when(studentService.getById(studentId)).thenReturn(teacher);

        authorizedUserService.findUserByUserDetails(customUserDetails);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(studentService, times(1)).getById(studentId);
    }

    @Test
    void updateUserPassword_shouldCallPasswordUpdateMethodFromAdminService_whenPasswordUpdateRequestDtoObjectHasAdminRole() {
        RoleName role = RoleName.ADMIN;
        PasswordUpdateRequestDto passwordUpdateRequestDto = mock(PasswordUpdateRequestDto.class);
        when(passwordUpdateRequestDto.getRoleName()).thenReturn(role);

        authorizedUserService.updateUserPassword(passwordUpdateRequestDto);

        verify(passwordUpdateRequestDto, times(1)).getRoleName();
        verify(adminService, times(1)).updateAdminPassword(passwordUpdateRequestDto);
    }

    @Test
    void updateUserPassword_shouldCallPasswordUpdateMethodFromManagerService_whenPasswordUpdateRequestDtoObjectHasManagerRole() {
        RoleName role = RoleName.MANAGER;
        PasswordUpdateRequestDto passwordUpdateRequestDto = mock(PasswordUpdateRequestDto.class);
        when(passwordUpdateRequestDto.getRoleName()).thenReturn(role);

        authorizedUserService.updateUserPassword(passwordUpdateRequestDto);

        verify(passwordUpdateRequestDto, times(1)).getRoleName();
        verify(managerService, times(1)).updateManagerPassword(passwordUpdateRequestDto);
    }

    @Test
    void updateUserPassword_shouldCallPasswordUpdateMethodFromTeacherService_whenPasswordUpdateRequestDtoObjectHasTeacherRole() {
        RoleName role = RoleName.TEACHER;
        PasswordUpdateRequestDto passwordUpdateRequestDto = mock(PasswordUpdateRequestDto.class);
        when(passwordUpdateRequestDto.getRoleName()).thenReturn(role);

        authorizedUserService.updateUserPassword(passwordUpdateRequestDto);

        verify(passwordUpdateRequestDto, times(1)).getRoleName();
        verify(teacherService, times(1)).updateTeacherPassword(passwordUpdateRequestDto);
    }

    @Test
    void updateUserPassword_shouldCallPasswordUpdateMethodFromStudentService_whenPasswordUpdateRequestDtoObjectHasStudentRole() {
        RoleName role = RoleName.STUDENT;
        PasswordUpdateRequestDto passwordUpdateRequestDto = mock(PasswordUpdateRequestDto.class);
        when(passwordUpdateRequestDto.getRoleName()).thenReturn(role);

        authorizedUserService.updateUserPassword(passwordUpdateRequestDto);

        verify(passwordUpdateRequestDto, times(1)).getRoleName();
        verify(studentService, times(1)).updateStudentPassword(passwordUpdateRequestDto);
    }

    @Test
    void updateFullNameByUserDetails_shouldCallFullNameUpdateMethodFromAdminService_whenCustomUserDetailsObjectHasAdminRole() {
        long adminId = 1;
        RoleName role = RoleName.ADMIN;
        FullName fullName = mock(FullName.class);
        when(customUserDetails.getId()).thenReturn(adminId);
        when(customUserDetails.getRoleName()).thenReturn(role);

        authorizedUserService.updateFullNameByUserDetails(customUserDetails, fullName);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(adminService, times(1)).updateAdminFullName(adminId, fullName);
        verify(customUserDetails, times(1)).setFullName(fullName);
    }

    @Test
    void updateFullNameByUserDetails_shouldCallFullNameUpdateMethodFromManagerService_whenCustomUserDetailsObjectHasManagerRole() {
        long managerId = 1;
        RoleName role = RoleName.MANAGER;
        FullName fullName = mock(FullName.class);
        when(customUserDetails.getId()).thenReturn(managerId);
        when(customUserDetails.getRoleName()).thenReturn(role);

        authorizedUserService.updateFullNameByUserDetails(customUserDetails, fullName);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(managerService, times(1)).updateManagerFullName(managerId, fullName);
        verify(customUserDetails, times(1)).setFullName(fullName);
    }

    @Test
    void updateFullNameByUserDetails_shouldCallFullNameUpdateMethodFromTeacherService_whenCustomUserDetailsObjectHasTeacherRole() {
        long teacherId = 1;
        RoleName role = RoleName.TEACHER;
        FullName fullName = mock(FullName.class);
        when(customUserDetails.getId()).thenReturn(teacherId);
        when(customUserDetails.getRoleName()).thenReturn(role);

        authorizedUserService.updateFullNameByUserDetails(customUserDetails, fullName);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(teacherService, times(1)).updateTeacherFullName(teacherId, fullName);
        verify(customUserDetails, times(1)).setFullName(fullName);
    }

    @Test
    void updateFullNameByUserDetails_shouldCallFullNameUpdateMethodFromStudentService_whenCustomUserDetailsObjectHasStudentRole() {
        long studentId = 1;
        RoleName role = RoleName.STUDENT;
        FullName fullName = mock(FullName.class);
        when(customUserDetails.getId()).thenReturn(studentId);
        when(customUserDetails.getRoleName()).thenReturn(role);

        authorizedUserService.updateFullNameByUserDetails(customUserDetails, fullName);

        verify(customUserDetails, times(1)).getId();
        verify(customUserDetails, times(1)).getRoleName();
        verify(studentService, times(1)).updateStudentFullName(studentId, fullName);
        verify(customUserDetails, times(1)).setFullName(fullName);
    }

}
