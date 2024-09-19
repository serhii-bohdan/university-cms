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
 * The {@code Group} class represents a student group in the system and inherits
 * from the {@link AbstractEntity} class.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * group's ID, name, students, and timestamps for when the group was created and
 * last updated. It also includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"groupName"})
@ToString(callSuper = true, exclude = "students")
@SuperBuilder
@Entity
@Table(name = "groups")
public class Group extends AbstractEntity {

    /**
     * The name of the group, with a maximum length of 5 characters.
     */
    @Column(name = "group_name", length = 5)
    private String groupName;

    /**
     * The students enrolled in this group.
     */
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    /**
     * The date and time when the group record was created in the database,
     * including time zone information.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the group record was last updated in the database,
     * including time zone information.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * Creates a new `Group` instance with a specified ID.
     * <p>
     * This constructor is typically used for internal purposes or in scenarios
     * where you need to create a `Group` object with a pre-defined ID.
     *
     * @param id the unique identifier for the group
     */
    public Group(Long id) {
        super(id);
    }

    /**
     * Sets the {@code createdAt} field to the current time when the group is
     * created.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * Sets the {@code updatedAt} field to the current time when the group is
     * updated.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

}
