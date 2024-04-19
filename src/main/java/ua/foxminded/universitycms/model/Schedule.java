package ua.foxminded.universitycms.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The {@code Schedule} class represents a structured schedule of study days.
 * It holds information about the collection of associated {@link StudyDay} objects.
 * {@code Schedule} inherits from the {@link AbstractEntity} class.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * schedule's ID and the study days in the schedule. It also includes methods to
 * get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "studyDays")
@ToString(callSuper = true, exclude = "studyDays")
@SuperBuilder
@Entity
@Table(name = "schedules")
public class Schedule extends AbstractEntity {

    /**
     * The collection of {@link StudyDay} objects that make up this schedule.
     */
    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<StudyDay> studyDays = new HashSet<>();

}
