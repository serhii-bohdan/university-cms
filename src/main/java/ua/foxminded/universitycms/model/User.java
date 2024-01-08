package ua.foxminded.universitycms.model;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * The {@code User} class is an abstract class that represents a user in the
 * system.
 * <p>
 * This class is annotated with {@code @MappedSuperclass}, indicating that it's
 * intended to be used as a base class for other entities. This class includes
 * fields for the user's ID, name (represented by the {@link Name} class),
 * email, password, activity status, and timestamps for when the user was
 * created and last updated.
 *
 * @author Serhii Bohdan
 */
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private Name name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Constructs a new {@code User} object with the given parameters.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     * @param password  the password of the user
     * @param isActive  the activity status of the user
     */
    protected User(String firstName, String lastName, String email, String password, Boolean isActive) {
        this.name = new Name(firstName, lastName);
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    /**
     * Constructs a new {@code User} object with default values.
     */
    protected User() {
        this.name = new Name();
    }

    /**
     * Sets the {@code createdAt} field to the current time when the user is
     * created.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Sets the {@code updatedAt} field to the current time when the user is
     * updated.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return this.name.getFirstName();
    }

    public void setFirstName(String firstName) {
        this.name.setFirstName(firstName);
    }

    public String getLastName() {
        return this.name.getLastName();
    }

    public void setLastName(String lastName) {
        this.name.setLastName(lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns a hash code value for the user.
     *
     * @return a hash code value for this user
     */
    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their
     * {@code email} and {@code password} fields.
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
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(email, other.email) && Objects.equals(password, other.password);
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of this user
     */
    @Override
    public String toString() {
        return "User [userId=" + userId + ", firstName=" + name.getFirstName() + ", lastName=" + name.getLastName()
                + ", email=" + email + ", password=" + password + ", isActive=" + isActive + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + "]";
    }

}
