package ua.foxminded.universitycms.service.impl;

import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.universitycms.dto.StudyDayDto;
import ua.foxminded.universitycms.mapper.Mapper;
import ua.foxminded.universitycms.model.StudyDay;
import ua.foxminded.universitycms.repository.StudyDayRepository;
import ua.foxminded.universitycms.service.StudyDayService;

/**
 * The {@code StudyDayServiceImpl} class implements the {@link StudyDayService} interface, providing concrete
 * implementations for managing study day entities. It extends the {@link AbstractService} class, inheriting common
 * service functionalities for basic CRUD operations and validation, and adds functionality specific to study days.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Mapper
 * @see StudyDayRepository
 */
@Service
@Validated
@Transactional
public class StudyDayServiceImpl extends AbstractService<StudyDay, StudyDayDto> implements StudyDayService {

    /**
     * The repository for managing {@link StudyDay} entities.
     */
    private final StudyDayRepository studyDayRepository;

    /**
     * Constructs a new {@code StudyDayServiceImpl} instance with the given dependencies.
     *
     * @param repository the repository for managing study day entities
     * @param mapper     the mapper for converting between study day entities and DTOs
     */
    public StudyDayServiceImpl(JpaRepository<StudyDay, Long> repository, Mapper<StudyDay, StudyDayDto> mapper) {
        super(repository, mapper);
        this.studyDayRepository = (StudyDayRepository) repository;
    }

    /**
     * Retrieves a {@link StudyDayDto} representing a study day associated with the given schedule ID and date,
     * or an empty {@link Optional} if no matching study day is found.
     *
     * @param scheduleId the ID of the schedule to which the study day belongs
     * @param date       the date of the study day to retrieve
     * @return an {@link Optional} potentially containing the retrieved {@link StudyDayDto}
     */
    @Override
    public Optional<StudyDayDto> getStudyDayByScheduleIdAndDate(long scheduleId, LocalDate date) {
        return studyDayRepository.findByScheduleIdAndDate(scheduleId, date).map(mapper::toDto);
    }

}
