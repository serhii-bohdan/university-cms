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
 * Entity representing a schedule in the university management system.
 * Extends {@link AbstractEntity} to inherit an ID and contains a collection of {@link Lesson}
 * objects defining its structure. Mapped to the {@code schedules} table via JPA annotations.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Lesson
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"lessons"})
@ToString(callSuper = true, exclude = {"lessons"})
@SuperBuilder
@Entity
@Table(name = "schedules")
public class Schedule extends AbstractEntity {

    /**
     * Collection of lessons associated with this schedule.
     * Defined as a one-to-many relationship with {@link Lesson}, mapped by the {@code schedule}
     * field in {@link Lesson}. Uses lazy fetching and cascades removal to associated lessons.
     */
    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Lesson> lessons = new HashSet<>();

}
