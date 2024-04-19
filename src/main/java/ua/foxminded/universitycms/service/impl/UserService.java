package ua.foxminded.universitycms.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.UserDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.repository.ScheduleRepository;

/**
 * The {@code UserService} class provides specialized services for managing {@link User} entities and their
 * corresponding {@link UserDto} representations. It extends the {@link AbstractService} class, incorporating
 * additional logic for password encoding and handling user schedules.
 *
 * @param <E> the specific type of {@link User} entity being managed
 * @param <D> the specific type of {@link UserDto} representing the entity
 * @author Serhii Bohdan
 */
@Transactional
public abstract class UserService<E extends User, D extends UserDto> extends AbstractService<E, D> {

    /**
     * The {@link ScheduleRepository} used for managing user schedules.
     */
    protected final ScheduleRepository scheduleRepository;

    /**
     * The {@link PasswordEncoder} used for securely encoding user passwords.
     */
    protected final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code UserService} instance with the given repositories, mapper, and password encoder.
     *
     * @param repository         the {@link JpaRepository} for managing user entities
     * @param mapper             the {@link Mapper} for converting between user entities and DTOs
     * @param scheduleRepository the {@link ScheduleRepository} for managing user schedules
     * @param passwordEncoder    the {@link PasswordEncoder} for encoding user passwords
     */
    protected UserService(JpaRepository<E, Long> repository, Mapper<E, D> mapper,
                          ScheduleRepository scheduleRepository, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.scheduleRepository = scheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new user based on the provided DTO representation.
     *
     * @param dto the DTO containing the data for the new user
     * @return a new DTO representing the saved user with its generated ID (if applicable)
     */
    @Override
    public D save(D dto) {
        dto.setScheduleId(getScheduleIdForUser());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return super.save(dto);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates an existing user entity based on the provided DTO, handling password updates:
     * <ol>
     *   <li>Validates that all mandatory fields in the DTO are initialized and the ID is present.</li>
     *   <li>Re-encodes the user's password using {@link PasswordEncoder} if it has changed.</li>
     *   <li>Delegates to the superclass {@code update} method for persistence.</li>
     * </ol>
     *
     * @param dto the DTO containing the updated data for the user
     * @return a DTO representing the updated user
     */
    @Override
    public D update(D dto) {
        D dtoWithUpdatedPasswordHash = getWithUpdatedPasswordHash(dto);
        return super.update(dtoWithUpdatedPasswordHash);
    }

    private Long getScheduleIdForUser() {
        Schedule schedule = scheduleRepository.save(new Schedule());
        return schedule.getId();
    }

    private D getWithUpdatedPasswordHash(D dto) {
        E userEntity = repository.findById(dto.getId()).orElseThrow(
            () -> new ServiceException("No user with id: " + dto.getId()));
        String oldPasswordHash = userEntity.getPasswordHash();
        String newPassword = dto.getPassword();

        if (!oldPasswordHash.equals(newPassword)) {
            dto.setPassword(passwordEncoder.encode(newPassword));
        }

        return dto;
    }

}
