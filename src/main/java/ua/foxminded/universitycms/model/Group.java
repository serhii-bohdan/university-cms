package ua.foxminded.universitycms.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * The {@code Group} class represents a group in the system.
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
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name", length = 5)
    private String groupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

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
     * Constructs a new {@code Group} object with default values.
     */
    public Group() {
    }

    /**
     * Sets the {@code createdAt} field to the current time when the group is
     * created.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Sets the {@code updatedAt} field to the current time when the group is
     * updated.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<Student> getStudents() {
        return Collections.unmodifiableSet(students);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns a hash code value for the group.
     *
     * @return a hash code value for this group
     */
    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * group names.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Group)) {
            return false;
        }
        Group other = (Group) obj;
        return Objects.equals(groupName, other.groupName);
    }

    /**
     * Returns a string representation of the group.
     *
     * @return a string representation of this group
     */
    @Override
    public String toString() {
        return "Group [groupId=" + groupId + ", groupName=" + groupName + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

}
