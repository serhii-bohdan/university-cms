package ua.foxminded.universitycms.model;

import java.time.ZonedDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base class representing a user entity in the university management system.
 * <p>
 * This class extends {@link AbstractEntity} and defines common properties for users, such as full name,
 * email, and password. It is not intended for direct instantiation; instead, concrete subclasses should
 * extend this class to represent specific user types. The {@code @MappedSuperclass} annotation ensures
 * that its fields are included in the persistence mapping of subclasses without being mapped to a standalone
 * table.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see FullName
 * @see jakarta.persistence.MappedSuperclass
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"email", "passwordHash"})
@ToString(callSuper = true)
@SuperBuilder
@MappedSuperclass
public abstract class User extends AbstractEntity {

    /**
     * The user's full name, represented as an embedded {@link FullName} object.
     */
    @Embedded
    private FullName fullName;

    /**
     * The user's email address, used as a unique identifier for login.
     */
    @Column(name = "email")
    private String email;

    /**
     * The hashed representation of the user's password, stored for authentication security.
     */
    @Column(name = "password_hash")
    private String passwordHash;

    /**
     * The user's time zone offset from UTC, e.g., "+02:00" or "-05:00".
     */
    @Column(name = "location_zone_offset")
    private String locationZoneOffset;

    /**
     * The date and time when the user record was created, including time zone information.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the user record was last updated, including time zone information.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * Lifecycle callback invoked by JPA before the entity is persisted.
     * <p>
     * Sets the {@code createdAt} field to the current timestamp automatically.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * Lifecycle callback invoked by JPA before the entity is updated.
     * <p>
     * Sets the {@code updatedAt} field to the current timestamp automatically.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

}
