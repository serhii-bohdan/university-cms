package ua.foxminded.universitycms.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.StudyDay;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.StudyDayRepository;
import ua.foxminded.universitycms.service.StudyDayService;

/**
 * The {@code StudyDayServiceImpl} class implements the {@link StudyDayService}
 * interface.
 * <p>
 * This class provides the functionality for managing study days.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class StudyDayServiceImpl implements StudyDayService {

    private final StudyDayRepository studyDayRepository;
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code StudyDayServiceImpl} with the specified study day
     * repository, schedule repository, and model mapper.
     *
     * @param studyDayRepository the study day repository
     * @param scheduleRepository the schedule repository
     * @param modelMapper        the model mapper
     */
    public StudyDayServiceImpl(StudyDayRepository studyDayRepository, ScheduleRepository scheduleRepository,
            ModelMapper modelMapper) {
        this.studyDayRepository = studyDayRepository;
        this.scheduleRepository = scheduleRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Sets up the model mapper after the bean has been initialized.
     */
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(StudyDay.class, StudyDayDto.class)
                .addMapping(src -> src.getSchedule().getScheduleId(), StudyDayDto::setScheduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addStudyDay(StudyDayDto studyDayDto) {
        boolean isAdded = false;

        if (Objects.nonNull(studyDayDto) && Objects.nonNull(studyDayDto.getDate())
                && Objects.nonNull(studyDayDto.getWeekDay()) && Objects.nonNull(studyDayDto.getScheduleId())) {
            studyDayRepository.save(mapToEntity(studyDayDto));
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StudyDayDto> getStudyDayById(Long studyDayId) {
        StudyDayDto findedStudyDay = null;

        if (Objects.nonNull(studyDayId)) {
            Optional<StudyDay> optional = studyDayRepository.findById(studyDayId);

            if (optional.isPresent()) {
                findedStudyDay = mapToDto(optional.get());
            }
        }

        return Optional.ofNullable(findedStudyDay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteStudyDayById(Long studyDayId) {
        boolean isDeleted = false;

        if (Objects.nonNull(studyDayId)) {
            Optional<StudyDay> optional = studyDayRepository.findById(studyDayId);

            if (optional.isPresent()) {
                studyDayRepository.delete(optional.get());
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    private StudyDayDto mapToDto(StudyDay entity) {
        return modelMapper.map(entity, StudyDayDto.class);
    }

    private StudyDay mapToEntity(StudyDayDto dto) {
        Schedule schedule = scheduleRepository.findById(dto.getScheduleId()).get();
        StudyDay studyDay = null;

        if (Objects.nonNull(dto.getStudyDayId()) && dto.getStudyDayId() >= 1L) {
            studyDay = studyDayRepository.findById(dto.getStudyDayId()).get();
            studyDay.setDate(dto.getDate());
            studyDay.setWeekDay(dto.getWeekDay());
        } else {
            studyDay = modelMapper.map(dto, StudyDay.class);
        }

        studyDay.setSchedule(schedule);
        return studyDay;
    }

}
