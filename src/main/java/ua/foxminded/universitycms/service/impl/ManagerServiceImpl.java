package ua.foxminded.universitycms.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.exception.UserNotFoundException;
import ua.foxminded.universitycms.mapper.ManagerMapper;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.repository.ManagerRepository;
import ua.foxminded.universitycms.service.ManagerService;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code ManagerServiceImpl} class is a concrete implementation of the {@link ManagerService} interface.
 * It extends the abstract {@link AbstractService} class and provides functionalities for managing {@link Manager} entities
 * using JPA repositories and a {@link Mapper} for data transfer between entities and DTOs (Data Transfer Objects).
 * This service layer implementation handles password management securely using a {@link PasswordEncoder}.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see PasswordEncoder
 */
@Service
@Validated
@Transactional
public class ManagerServiceImpl extends AbstractService<Manager, ManagerDto> implements ManagerService {

    /**
     * The repository used for interacting with {@link Manager} entities in the database.
     */
    private final ManagerRepository managerRepository;

    /**
     * Mapper for converting between {@link Manager} entities and {@link ManagerDto} objects.
     */
    private final ManagerMapper managerMapper;

    /**
     * An instance of {@link PasswordEncoder} used for secure password hashing.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code ManagerServiceImpl} instance.
     *
     * @param repository        the JPA repository for {@link Manager} entities, providing basic CRUD operations
     * @param mapper            the {@link Mapper} instance used for converting between {@link Manager} and {@link ManagerDto} objects
     * @param passwordEncoder   the {@link PasswordEncoder} instance for securely hashing manager passwords
     * @param managerRepository the specialized repository for performing custom queries related to {@link Manager} entities
     */
    public ManagerServiceImpl(JpaRepository<Manager, Long> repository, Mapper<Manager, ManagerDto> mapper, PasswordEncoder passwordEncoder, ManagerRepository managerRepository) {
        super(repository, mapper);
        this.passwordEncoder = passwordEncoder;
        this.managerRepository = managerRepository;
        this.managerMapper = (ManagerMapper) mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagerDto save(ManagerDto dto, String password) {
        Manager manager = managerMapper.toEntity(dto);
        manager.setPasswordHash(passwordEncoder.encode(password));
        return managerMapper.toDto(managerRepository.save(manager));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagerDto update(ManagerDto dto) {
        Manager existingManager = managerRepository.findById(dto.getId())
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, String.format("Manager not found with id: %d", dto.getId())));
        Manager updatedManager = managerMapper.partialUpdate(dto, existingManager);
        return managerMapper.toDto(managerRepository.save(updatedManager));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ManagerDto> getManagersPage(Pageable pageable) {
        return managerRepository.findAll(pageable).map(managerMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ManagerDto> getManagerInPageByName(String fullName, Pageable pageable) {
        List<String> names = getSeparateFirstNameAndLastName(fullName.strip());
        return managerRepository.findByName_FirstNameAndName_LastNameIgnoreCase(names.get(0), names.get(1), pageable).map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllNamesOfManagers() {
        return managerRepository.findAll().stream()
            .map(m -> m.getName().getFirstName() + " " + m.getName().getLastName())
            .toList();
    }

    private List<String> getSeparateFirstNameAndLastName(String fullName) {
        String[] firstNameAndLastName = fullName.split(" ");

        if (firstNameAndLastName.length >= 2) {
            return Arrays.asList(firstNameAndLastName);
        }

        throw new InvalidFullNameFormatException(HttpStatus.BAD_REQUEST, "Full name must contain at least two words");
    }

}
