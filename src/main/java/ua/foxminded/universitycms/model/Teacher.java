package ua.foxminded.universitycms.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a teacher entity within the university management system.
 * <p>
 * This class extends {@link Educator} to inherit common educator properties and adds attributes
 * specific to teachers, such as their role, schedule, and associated courses. It is mapped to the
 * {@code teachers} table in the database using JPA annotations. Instances of this class represent
 * individual teachers with permissions defined by their {@link Role}, a personal {@link Schedule},
 * and a set of {@link Course} objects they author.
 *
 * @author Serhii Bohdan
 * @see Educator
 * @see Role
 * @see Schedule
 * @see Course
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"schedule", "courses"})
@SuperBuilder
@Entity
@Table(name = "teachers")
public class Teacher extends Educator {

    /**
     * The role assigned to the teacher, defining their permissions within the system.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Role} entity and is mapped to
     * the {@code role_id} column in the {@code teachers} table. The role is eagerly fetched
     * ({@code FetchType.EAGER}) and must not be null, determining the teacher's access rights and
     * responsibilities.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * The teacher's personal schedule.
     * <p>
     * This field defines a one-to-one relationship with the {@link Schedule} entity, mapped to the
     * {@code schedule_id} column in the {@code teachers} table. The schedule is lazily fetched
     * ({@code FetchType.LAZY}) and managed with a cascading policy ({@code CascadeType.ALL}), meaning
     * all operations on the teacher entity will propagate to its schedule.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    /**
     * The set of courses authored by the teacher.
     * <p>
     * This field establishes a one-to-many relationship with the {@link Course} entity, where the
     * teacher is referenced as the author. It is mapped by the {@code author} field in {@link Course},
     * lazily fetched ({@code FetchType.LAZY}), and managed with a cascading policy
     * ({@code CascadeType.ALL}), meaning all operations on the teacher entity will propagate to its
     * courses. The set is initialized as an empty {@code HashSet}.
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

}
