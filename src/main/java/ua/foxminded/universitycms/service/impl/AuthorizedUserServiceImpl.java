package ua.foxminded.universitycms.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.model.*;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.security.userdetails.CustomUserDetails;
import ua.foxminded.universitycms.service.*;

/**
 * Implementation of {@link AuthorizedUserService} for managing authorized user operations in the university system.
 * <p>
 * This service delegates user-specific operations (finding users, updating passwords and full names) to role-based
 * services ({@link AdminService}, {@link ManagerService}, {@link TeacherService}, {@link StudentService}) based
 * on the user's role from {@link CustomUserDetails}. Uses {@code @Service} for Spring bean registration,
 * {@code @Validated} for validation, and {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @author Serhii Bohdan
 * @see AuthorizedUserService
 * @see AdminService
 * @see ManagerService
 * @see TeacherService
 * @see StudentService
 * @see CustomUserDetails
 */
@Service
@Validated
@RequiredArgsConstructor
public class AuthorizedUserServiceImpl implements AuthorizedUserService {

    /**
     * Service for managing admin-related operations.
     * <p>
     * Used to delegate admin-specific tasks like retrieving or updating admin data.
     */
    private final AdminService adminService;

    /**
     * Service for managing manager-related operations.
     * <p>
     * Used to delegate manager-specific tasks like retrieving or updating manager data.
     */
    private final ManagerService managerService;

    /**
     * Service for managing teacher-related operations.
     * <p>
     * Used to delegate teacher-specific tasks like retrieving or updating teacher data.
     */
    private final TeacherService teacherService;

    /**
     * Service for managing student-related operations.
     * <p>
     * Used to delegate student-specific tasks like retrieving or updating student data.
     */
    private final StudentService studentService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto findUserByUserDetails(CustomUserDetails customUserDetails) {
        long userId = customUserDetails.getId();
        RoleName userRole = customUserDetails.getRoleName();

        return switch (userRole) {
            case ADMIN -> adminService.getById(userId);
            case MANAGER -> managerService.getById(userId);
            case TEACHER -> teacherService.getById(userId);
            case STUDENT -> studentService.getById(userId);
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateUserPassword(PasswordUpdateRequestDto passwordUpdateRequestDto) {
        switch (passwordUpdateRequestDto.getRoleName()) {
            case ADMIN -> adminService.updateAdminPassword(passwordUpdateRequestDto);
            case MANAGER -> managerService.updateManagerPassword(passwordUpdateRequestDto);
            case TEACHER -> teacherService.updateTeacherPassword(passwordUpdateRequestDto);
            case STUDENT -> studentService.updateStudentPassword(passwordUpdateRequestDto);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateFullNameByUserDetails(CustomUserDetails customUserDetails, FullName fullName) {
        long userId = customUserDetails.getId();

        switch (customUserDetails.getRoleName()) {
            case ADMIN -> adminService.updateAdminFullName(userId, fullName);
            case MANAGER -> managerService.updateManagerFullName(userId, fullName);
            case TEACHER -> teacherService.updateTeacherFullName(userId, fullName);
            case STUDENT -> studentService.updateStudentFullName(userId, fullName);
        }

        customUserDetails.setFullName(fullName);
    }

}
