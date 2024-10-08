package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.ZonedDateTime;

/**
 * The {@code Admin} class represents an administrators in the system.
 * This entity is mapped to the "admins" table in the database.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"email", "passwordHash"})
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "admins")
public class Admin extends AbstractEntity {

    /**
     * The admin's full name, represented as an embedded {@link Name} object.
     */
    @Embedded
    private Name name;

    /**
     * The admin's unique email address used for authentication.
     */
    @Column(name = "email")
    private String email;

    /**
     * The hashed representation of the admin's password.
     */
    @Column(name = "password_hash")
    private String passwordHash;

    /**
     * The role assigned to the admin, defining their permissions within the system.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * The date and time when the admin record was created in the database,
     * including time zone information.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the admin record was last updated in the database,
     * including time zone information.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * JPA lifecycle callback method called before the entity is persisted.
     * This method sets the `createdAt` field to the current timestamp.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * JPA lifecycle callback method called before the entity is updated.
     * This method sets the `updatedAt` field to the current timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

}
