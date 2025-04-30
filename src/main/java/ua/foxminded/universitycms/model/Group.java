package ua.foxminded.universitycms.model;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a student group within the university management system.
 * <p>
 * This class extends {@link AbstractEntity} to inherit a unique identifier and defines attributes
 * specific to a student group, such as its name, enrolled students, and timestamps for creation and
 * updates. It is mapped to the {@code groups} table in the database using JPA annotations. Instances
 * of this class represent academic cohorts of students, with lifecycle callbacks to automatically
 * manage creation and update timestamps.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Student
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"groupName"})
@ToString(callSuper = true, exclude = {"students"})
@SuperBuilder
@Entity
@Table(name = "groups")
public class Group extends AbstractEntity {

    /**
     * The name of the group, uniquely identifying it within the system.
     * <p>
     * This field is mapped to the {@code group_name} column in the {@code groups} table and has a
     * maximum length of 5 characters, ensuring concise group identifiers.
     */
    @Column(name = "group_name", length = 5)
    private String groupName;

    /**
     * The set of students enrolled in this group.
     * <p>
     * This field establishes a one-to-many relationship with the {@link Student} entity, where the
     * group is referenced by the {@code group} field in {@link Student}. It is lazily fetched
     * ({@code FetchType.LAZY}) and managed with a cascading policy ({@code CascadeType.ALL}), meaning
     * all operations on the group entity will propagate to its students. The set is initialized as an
     * empty {@code HashSet}.
     */
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    /**
     * The date and time when the group record was created, including time zone information.
     * <p>
     * This field is mapped to the {@code created_at} column in the {@code groups} table and is
     * automatically set by the {@link #onCreate()} lifecycle callback when the group is persisted.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the group record was last updated, including time zone information.
     * <p>
     * This field is mapped to the {@code updated_at} column in the {@code groups} table and is
     * automatically updated by the {@link #onUpdate()} lifecycle callback when the group is modified.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * Lifecycle callback invoked by JPA before the group is persisted.
     * <p>
     * Sets the {@code createdAt} field to the current timestamp when the group is first saved to the
     * database.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * Lifecycle callback invoked by JPA before the group is updated.
     * <p>
     * Sets the {@code updatedAt} field to the current timestamp when the group is modified in the
     * database.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

}
