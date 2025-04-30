package ua.foxminded.universitycms.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.AdminCreationDto;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.AdminMapper;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.AdminRepository;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.service.AdminService;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of the {@link AdminService} interface for managing {@link Admin} entities in the university
 * management system.
 * <p>
 * This service class extends {@link AbstractService} to provide CRUD operations and additional functionality specific
 * to admin users, such as creation from DTOs, paginated retrieval, email extraction, and updates to passwords and full
 * names. It uses {@link AdminRepository} for persistence, {@link AdminMapper} for entity-DTO conversions, and
 * {@link PasswordEncoder} for secure password handling. The {@code @Service} annotation marks it as a Spring-managed
 * bean, {@code @Validated} enables validation, and {@code @RequiredArgsConstructor} ensures dependency injection.
 *
 * @author Serhii Bohdan
 * @see AdminService
 * @see AbstractService
 * @see AdminRepository
 * @see AdminMapper
 * @see PasswordEncoder
 * @see EntityNotFoundException
 * @see UserNotFoundException
 */
@Service
@Validated
public class AdminServiceImpl extends AbstractService<Admin, AdminDto> implements AdminService {

    /**
     * Error message template used when an admin with the specified ID cannot be found.
     */
    private static final String ADMIN_NOT_FOUND_MESSAGE = "Admin not found with id: %s.";

    /**
     * Error message template used when a role with the specified name cannot be found.
     */
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role with given role name is not found: %s.";

    /**
     * Repository for performing CRUD operations on {@link Admin} entities.
     * <p>
     * This {@link AdminRepository} instance provides data access methods specific to admins, extending
     * the functionality of {@link JpaRepository}.
     */
    private final AdminRepository adminRepository;

    /**
     * Mapper for converting between {@link Admin} entities and {@link AdminDto} DTOs.
     * <p>
     * This {@link AdminMapper} instance handles transformations specific to admin entities, including
     * conversions from creation DTOs.
     */
    private final AdminMapper adminMapper;

    /**
     * Repository for accessing and querying {@link Role} entities.
     * <p>
     * Used to retrieve the admin role during entity creation or updates.
     */
    private final RoleRepository roleRepository;

    /**
     * Encoder for hashing passwords during creation and update operations.
     * <p>
     * This {@link PasswordEncoder} instance ensures secure storage of admin passwords by encoding them
     * before persistence.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code AdminServiceImpl} instance with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up specific
     * dependencies for admin management, including the role repository and password encoder.
     *
     * @param repository      the {@link JpaRepository} for {@link Admin} entities, providing basic CRUD operations
     * @param mapper          the {@link Mapper} instance for converting between {@link Admin} and {@link AdminDto} objects
     * @param roleRepository  the {@link RoleRepository} for retrieving role information
     * @param passwordEncoder the {@link PasswordEncoder} for securing passwords
     */
    public AdminServiceImpl(JpaRepository<Admin, Long> repository, Mapper<Admin, AdminDto> mapper,
                            RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.adminRepository = (AdminRepository) repository;
        this.adminMapper = (AdminMapper) mapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdminDto save(AdminCreationDto dto) {
        Admin admin = adminMapper.toEntity(dto);
        admin.setRole(getAdminRole());
        return mapper.toDto(adminRepository.save(admin));
    }

    private Role getAdminRole() {
        return roleRepository.findByRoleName(RoleName.ADMIN)
            .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ROLE_NOT_FOUND_MESSAGE.formatted(RoleName.ADMIN)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminDto> findAdmins(Pageable pageable, String email) {
        return StringUtils.isBlank(email)
            ? adminRepository.findAll(pageable).map(adminMapper::toDto)
            : adminRepository.findByEmail(email, pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> extractAdminEmails(Collection<AdminDto> admins) {
        return admins.stream()
            .map(AdminDto::getEmail)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateAdminPassword(PasswordUpdateRequestDto passwordUpdateRequest) {
        long adminId = passwordUpdateRequest.getUserId();
        Admin admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                ADMIN_NOT_FOUND_MESSAGE.formatted(adminId)));

        admin.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        adminRepository.save(admin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateAdminFullName(long adminId, FullName fullName) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
            () -> new UserNotFoundException(HttpStatus.NOT_FOUND, ADMIN_NOT_FOUND_MESSAGE.formatted(adminId)));

        admin.setFullName(fullName);
        adminRepository.save(admin);
    }

}
