package ua.foxminded.universitycms.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.AdminMapper;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Admin;
import ua.foxminded.universitycms.repository.AdminRepository;
import ua.foxminded.universitycms.service.AdminService;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@link AdminService} interface, providing business logic for managing
 * {@link Admin} entities.
 *
 * <p>This service class handles operations such as creating, retrieving, updating, and deleting admin users,
 * leveraging the {@link AdminRepository} for data access and a {@link Mapper} for converting between
 * entity and DTO representations. It also incorporates password encoding for security.
 *
 * @author Serhii Bohdan
 */
@Service
@Validated
@Transactional
public class AdminServiceImpl extends AbstractService<Admin, AdminDto> implements AdminService {

    /**
     * Repository for accessing and managing {@link Admin} entities in the database.
     */
    private final AdminRepository adminRepository;

    /**
     * Mapper for converting between {@link Admin} entities and {@link AdminDto} objects.
     */
    private final AdminMapper adminMapper;

    /**
     * Instance of {@link PasswordEncoder} used to securely hash admin passwords before storing them.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code AdminServiceImpl} instance.
     *
     * @param repository      the JPA repository for {@link Admin} entities, providing basic CRUD operations
     * @param mapper          the {@link Mapper} instance used for converting between {@link Admin} and {@link AdminDto} objects
     * @param passwordEncoder the {@link PasswordEncoder} instance for securely hashing admin passwords
     * @param adminRepository the specialized repository for performing custom queries related to {@link Admin} entities
     */
    public AdminServiceImpl(JpaRepository<Admin, Long> repository, Mapper<Admin, AdminDto> mapper,
                               PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        super(repository, mapper);
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.adminMapper = (AdminMapper) mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdminDto save(AdminDto dto, String password) {
        Admin admin = adminMapper.toEntity(dto);
        admin.setPasswordHash(passwordEncoder.encode(password));
        return adminMapper.toDto(adminRepository.save(admin));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdminDto update(AdminDto dto) {
        Admin existingAdmin = adminRepository.findById(dto.getId())
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND,
                String.format("Admin not found with id: %d", dto.getId())));

        Admin updatedAdmin = adminMapper.partialUpdate(dto, existingAdmin);
        return adminMapper.toDto(adminRepository.save(updatedAdmin));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AdminDto> getAdminsPage(Pageable pageable) {
        return adminRepository.findAll(pageable).map(adminMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AdminDto> getAdminInPageByName(String fullName, Pageable pageable) {
        List<String> names = getSeparateFirstNameAndLastName(fullName.strip());
        return adminRepository.findByName_FirstNameAndName_LastNameIgnoreCase(names.get(0), names.get(1), pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllNamesOfAdmins() {
        return adminRepository.findAll().stream()
            .map(a -> a.getName().getFirstName() + " " + a.getName().getLastName())
            .toList();
    }

    private List<String> getSeparateFirstNameAndLastName(String fullName) {
        String[] firstNameAndLastName = fullName.split(" ");

        if (firstNameAndLastName.length >= 2) {
            return Arrays.asList(firstNameAndLastName);
        }

        throw new InvalidFullNameFormatException(HttpStatus.BAD_REQUEST,
            "Full name must contain at least two words");
    }

}
