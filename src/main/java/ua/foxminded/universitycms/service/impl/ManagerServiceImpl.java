package ua.foxminded.universitycms.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Manager;
import ua.foxminded.universitycms.service.ManagerService;

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
     * An instance of {@link PasswordEncoder} used for secure password hashing.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code ManagerServiceImpl} instance.
     *
     * @param repository      the JPA repository for {@link Manager} entities
     * @param mapper          the Mapper instance used for converting between Manager and ManagerDto objects
     * @param passwordEncoder the PasswordEncoder instance for secure password hashing
     */
    public ManagerServiceImpl(JpaRepository<Manager, Long> repository, Mapper<Manager, ManagerDto> mapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Updates an existing {@link Manager} entity from the provided {@link ManagerDto}.
     * Encodes a new password if it's provided in the DTO, otherwise keeps the existing password hash.
     * Throws a {@link ServiceException} if mandatory fields in the DTO are missing or the ID is null.
     *
     * @param dto the ManagerDto object containing updated manager information
     * @return the updated ManagerDto object
     */
    @Override
    public ManagerDto update(ManagerDto dto) {
        ManagerDto dtoWithUpdatedPasswordHash = getWithUpdatedPasswordHash(dto);
        return super.update(dtoWithUpdatedPasswordHash);
    }

    private ManagerDto getWithUpdatedPasswordHash(ManagerDto dto) {
        Manager userEntity = repository.findById(dto.getId())
            .orElseThrow(() -> new ServiceException("No manager with id: " + dto.getId()));
        String oldPasswordHash = userEntity.getPasswordHash();
        String newPassword = dto.getPassword();

        if (!oldPasswordHash.equals(newPassword)) {
            dto.setPassword(passwordEncoder.encode(newPassword));
        }

        return dto;
    }

}
