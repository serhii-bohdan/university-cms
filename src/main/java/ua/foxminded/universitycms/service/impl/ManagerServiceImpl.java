package ua.foxminded.universitycms.service.impl;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.ManagerCreationDto;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.dto.PasswordUpdateRequestDto;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.ManagerMapper;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.FullName;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import ua.foxminded.universitycms.repository.ManagerRepository;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.service.ManagerService;

/**
 * Implementation of the {@link ManagerService} interface for managing {@link Manager} entities in the university
 * management system.
 * <p>
 * This service class extends {@link AbstractService} to leverage common CRUD operations and provides concrete
 * implementations for manager-specific operations such as creation from DTOs, paginated retrieval, email extraction,
 * and updates to passwords and full names. It uses {@link ManagerRepository} for data access, {@link ManagerMapper}
 * for entity-DTO conversions, and {@link PasswordEncoder} for secure password handling. The {@code @Service}
 * annotation marks it as a Spring-managed bean, and {@code @Validated} enables validation.
 *
 * @author Serhii Bohdan
 * @see ManagerService
 * @see AbstractService
 * @see ManagerRepository
 * @see ManagerMapper
 * @see RoleRepository
 * @see PasswordEncoder
 * @see EntityNotFoundException
 * @see UserNotFoundException
 */
@Service
@Validated
public class ManagerServiceImpl extends AbstractService<Manager, ManagerDto> implements ManagerService {

    /**
     * Error message template used when a role with the specified name cannot be found.
     */
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role with given role name is not found: %s.";

    /**
     * Error message template used when a manager with the specified ID cannot be found.
     */
    private static final String MANAGER_NOT_FOUND_MESSAGE = "Manager not found with id: %s.";

    /**
     * Repository for performing CRUD operations on {@link Manager} entities.
     * <p>
     * This {@link ManagerRepository} instance provides data access methods specific to managers,
     * extending {@link JpaRepository}.
     */
    private final ManagerRepository managerRepository;

    /**
     * Mapper for converting between {@link Manager} entities and {@link ManagerDto} DTOs.
     * <p>
     * This {@link ManagerMapper} instance handles transformations specific to manager entities, including
     * conversions from creation DTOs.
     */
    private final ManagerMapper managerMapper;

    /**
     * Repository for accessing and querying {@link Role} entities.
     * <p>
     * Used to retrieve the manager role during entity creation or updates.
     */
    private final RoleRepository roleRepository;

    /**
     * Encoder for hashing passwords during creation and update operations.
     * <p>
     * This {@link PasswordEncoder} instance ensures secure storage of manager passwords by encoding them
     * before persistence.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code ManagerServiceImpl} instance with the required dependencies.
     * <p>
     * Initializes the parent {@link AbstractService} with the provided repository and mapper, and sets up
     * specific dependencies for manager management, including the role repository and password encoder.
     *
     * @param repository      the {@link JpaRepository} for {@link Manager} entities, providing basic CRUD operations
     * @param mapper          the {@link Mapper} instance for converting between {@link Manager} and {@link ManagerDto}
     *                        objects
     * @param roleRepository  the {@link RoleRepository} for retrieving role information
     * @param passwordEncoder the {@link PasswordEncoder} for securing passwords
     */
    public ManagerServiceImpl(JpaRepository<Manager, Long> repository, Mapper<Manager, ManagerDto> mapper,
                              RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.managerRepository = (ManagerRepository) repository;
        this.managerMapper = (ManagerMapper) mapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ManagerDto save(ManagerCreationDto dto) {
        Manager manager = managerMapper.toEntity(dto);
        manager.setRole(getManagerRole());
        return managerMapper.toDto(managerRepository.save(manager));
    }

    private Role getManagerRole() {
        return roleRepository.findByRoleName(RoleName.MANAGER).orElseThrow(
            () -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                ROLE_NOT_FOUND_MESSAGE.formatted(RoleName.MANAGER)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ManagerDto> findManagers(Pageable pageable, String email) {
        return StringUtils.isBlank(email)
            ? managerRepository.findAll(pageable).map(managerMapper::toDto)
            : managerRepository.findByEmail(email, pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> extractManagerEmails(Collection<ManagerDto> managers) {
        return managers.stream()
            .map(ManagerDto::getEmail)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateManagerPassword(PasswordUpdateRequestDto passwordUpdateRequest) {
        long managerId = passwordUpdateRequest.getUserId();
        Manager manager = managerRepository.findById(managerId)
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                MANAGER_NOT_FOUND_MESSAGE.formatted(managerId)));

        manager.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        managerRepository.save(manager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateManagerFullName(long managerId, FullName fullName) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(
            () -> new UserNotFoundException(HttpStatus.NOT_FOUND, MANAGER_NOT_FOUND_MESSAGE.formatted(managerId)));

        manager.setFullName(fullName);
        managerRepository.save(manager);
    }

}
