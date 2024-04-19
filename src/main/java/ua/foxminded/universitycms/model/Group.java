package ua.foxminded.universitycms.model;

import java.time.LocalDateTime;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = "students")
@SuperBuilder
@Entity
@Table(name = "groups")
public class Group extends AbstractEntity {

    /**
     * The name of the group, with a maximum length of 5 characters.
     */
    @EqualsAndHashCode.Include
    @Column(name = "group_name", length = 5)
    private String groupName;

    /**
     * The students enrolled in this group.
     */
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    /**
     * The date and time the group record was created in the system.
     * This field is automatically populated with the current timestamp before persisting the entity.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * The date and time the group record was last updated in the system.
     * This field is automatically populated with the current timestamp before updating the entity.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Constructs a new {@code Group} object with the given group name.
     *
     * @param groupName the name of the group
     */
    public Group(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Sets the {@code createdAt} field to the current time when the group is
     * created.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(LocalDateTime.now());
    }

    /**
     * Sets the {@code updatedAt} field to the current time when the group is
     * updated.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }

}
