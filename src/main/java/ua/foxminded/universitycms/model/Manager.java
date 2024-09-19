package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.ZonedDateTime;

/**
 * The {@code Manager} class represents a manager user in the system and inherits
 * from the abstract {@link AbstractEntity} class.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA entity.
 * This means that instances of this class can be persisted to a database table named "managers"
 * as specified by the {@code @Table} annotation.
 * <p>
 * Additionally, it defines JPA lifecycle methods {@code onCreate()} and {@code onUpdate()}
 * to automatically set timestamps before persisting or updating the entity.
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
@Table(name = "managers")
public class Manager extends AbstractEntity {

    /**
     * The manager's full name.
     */
    @Embedded
    private Name name;

    /**
     * The manager's email address.
     */
    @Column(name = "email")
    private String email;

    /**
     * The hashed password for secure storage.
     */
    @Column(name = "password_hash")
    private String passwordHash;

    /**
     * The role assigned to the manager, defining their permissions within the system.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * The date and time when the manager record was created in the database,
     * including time zone information.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the manager record was last updated in the database,
     * including time zone information.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * Constructs a new {@code Manager} with the specified details.
     *
     * @param name         the manager's full name
     * @param email        the manager's email address
     * @param passwordHash the hashed password
     * @param role         the role assigned to the manager
     */
    public Manager(Name name, String email, String passwordHash, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    /**
     * This method is called before persisting the entity. It sets the {@code createdAt} field
     * to the current timestamp.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * This method is called before updating the entity. It sets the {@code updatedAt} field
     * to the current timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

}
