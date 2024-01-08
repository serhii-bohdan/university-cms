package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.ScheduleDto;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.service.ScheduleService;

/**
 * The {@code ScheduleServiceImpl} class implements the {@link ScheduleService}
 * interface.
 * <p>
 * This class provides the functionality for managing schedules.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code ScheduleServiceImpl} with the specified schedule
     * repository and model mapper.
     *
     * @param scheduleRepository the schedule repository
     * @param modelMapper        the model mapper
     */
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, ModelMapper modelMapper) {
        this.scheduleRepository = scheduleRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addSchedule(ScheduleDto scheduleDto) {
        boolean isAdded = false;

        if (Objects.nonNull(scheduleDto)) {
            scheduleRepository.save(mapToEntity(scheduleDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ScheduleDto> getScheduleById(Long scheduleId) {
        ScheduleDto findedSchedule = null;

        if (Objects.nonNull(scheduleId)) {
            Optional<Schedule> optional = scheduleRepository.findById(scheduleId);

            if (optional.isPresent()) {
                findedSchedule = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedSchedule);
    }

    private ScheduleDto mapToDto(Schedule entity) {
        return modelMapper.map(entity, ScheduleDto.class);
    }

    private Schedule mapToEntity(ScheduleDto dto) {
        return modelMapper.map(dto, Schedule.class);
    }

}
