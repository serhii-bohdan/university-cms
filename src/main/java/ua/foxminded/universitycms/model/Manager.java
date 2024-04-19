package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

/**
 * The {@code Manager} class represents a manager user in the system and inherits
 * from the abstract {@link AbstractEntity} class.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA entity.
 * This means that instances of this class can be persisted to a database table named "managers"
 * as specified by the {@code @Table} annotation.
 * <p>
 *  Additionally, it defines JPA lifecycle methods {@code onCreate()} and {@code onUpdate()}
 * to automatically set timestamps before persisting or updating the entity.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    @EqualsAndHashCode.Include
    @Column(name = "email")
    private String email;

    /**
     * The hashed password for secure storage.
     */
    @EqualsAndHashCode.Include
    @Column(name = "password_hash")
    private String passwordHash;

    /**
     * The date and time the manager record was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * The date and time the manager record was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Constructs a new {@code Manager} object with all fields provided.
     *
     * @param name         the manager's full name
     * @param email        the manager's email address
     * @param passwordHash the hashed password
     * @param createdAt    the creation date and time (optional)
     * @param updatedAt    the last update date and time (optional)
     */
    public Manager(Name name, String email, String passwordHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * This method is called before persisting the entity. It sets the {@code createdAt} field
     * to the current timestamp.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(LocalDateTime.now());
    }

    /**
     * This method is called before updating the entity. It sets the {@code updatedAt} field
     * to the current timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }

}
