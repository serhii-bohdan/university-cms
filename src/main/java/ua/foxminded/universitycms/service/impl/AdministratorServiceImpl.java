package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.AdministratorDto;
import ua.foxminded.universitycms.model.Administrator;
import ua.foxminded.universitycms.repository.AdministratorRepository;
import ua.foxminded.universitycms.service.AdministratorService;

/**
 * The {@code AdministratorServiceImpl} class implements the
 * {@link AdministratorService} interface.
 * <p>
 * This class provides the functionality for managing administrators.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code AdministratorServiceImpl} with the specified
     * administrator repository and model mapper.
     *
     * @param administratorRepository the administrator repository
     * @param modelMapper             the model mapper
     */
    public AdministratorServiceImpl(AdministratorRepository administratorRepository, ModelMapper modelMapper) {
        this.administratorRepository = administratorRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAdministrator(AdministratorDto administratorDto) {
        boolean isAdded = false;

        if (Objects.nonNull(administratorDto) && Objects.nonNull(administratorDto.getFirstName())
                && Objects.nonNull(administratorDto.getLastName()) && Objects.nonNull(administratorDto.getEmail())
                && Objects.nonNull(administratorDto.getPassword()) && Objects.nonNull(administratorDto.getIsActive())) {
            administratorRepository.save(mapToEntity(administratorDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AdministratorDto> getAdministratorById(Long administratorId) {
        AdministratorDto findedAdministrator = null;

        if (Objects.nonNull(administratorId)) {
            Optional<Administrator> optional = administratorRepository.findById(administratorId);

            if (optional.isPresent()) {
                findedAdministrator = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedAdministrator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteAdministratorById(Long administratorId) {
        boolean isDeleted = false;

        if (Objects.nonNull(administratorId)) {
            Optional<Administrator> optional = administratorRepository.findById(administratorId);

            if (optional.isPresent()) {
                administratorRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private AdministratorDto mapToDto(Administrator entity) {
        return modelMapper.map(entity, AdministratorDto.class);
    }

    private Administrator mapToEntity(AdministratorDto dto) {
        Administrator administrator = null;

        if (Objects.nonNull(dto.getUserId()) && dto.getUserId() >= 1L) {
            administrator = administratorRepository.findById(dto.getUserId()).get();
            administrator.setFirstName(dto.getFirstName());
            administrator.setLastName(dto.getLastName());
            administrator.setEmail(dto.getEmail());
            administrator.setPassword(dto.getPassword());
            administrator.setIsActive(dto.getIsActive());
        } else {
            administrator = modelMapper.map(dto, Administrator.class);
        }

        return administrator;
    }

}
