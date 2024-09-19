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
 * This abstract class represents a user entity in the system. It inherits from {@link AbstractEntity}
 * and provides additional properties specific to a user. Users should not be directly instantiated,
 * but rather through concrete subclasses that might extend this class to add specific user types.
 * <p>
 * {@code @MappedSuperclass} This annotation indicates that this class is a base class for user entities and
 * its fields will be included in the persistence mapping, but the class itself
 * won't be mapped to a table.
 *
 * @author Serhii Bohdan
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
     * The user's full name represented by an embedded {@link Name} object.
     */
    @Embedded
    private Name name;

    /**
     * The user's email address used for login.
     */
    @Column(name = "email")
    private String email;

    /**
     * A hashed representation of the user's password for security purposes.
     */
    @Column(name = "password_hash")
    private String passwordHash;

    /**
     * A flag indicating whether the user account is active or not.
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * The date and time when the user record was created in the database,
     * including time zone information.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * The date and time when the user record was last updated in the database,
     * including time zone information.
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    /**
     * Constructs a new {@code User} instance with essential user information.
     * This protected constructor is designed to be used by subclasses to create specific user types.
     *
     * @param name         the user's full name
     * @param email        the user's email address
     * @param passwordHash a securely hashed representation of the user's password
     * @param isActive     indicates whether the user's account is active
     */
    protected User(Name name, String email, String passwordHash, Boolean isActive) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isActive = isActive;
    }

    /**
     * This callback method is called before persisting the entity. It sets the {@code createdAt} field
     * to the current timestamp.
     */
    @PrePersist
    protected void onCreate() {
        setCreatedAt(ZonedDateTime.now());
    }

    /**
     * This callback method is called before updating the entity. It sets the {@code updatedAt} field
     * to the current timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

}
