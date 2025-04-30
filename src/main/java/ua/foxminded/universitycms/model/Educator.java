package ua.foxminded.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base class representing an educator within the university management system.
 * <p>
 * This class extends {@link User} to inherit common user properties and adds educator-specific attributes.
 * It is not intended for direct instantiation; instead, concrete subclasses should extend this class to
 * define specific types of educators. The {@code @MappedSuperclass} annotation ensures that its fields are
 * included in the persistence mapping of subclasses without being mapped to a standalone table.
 *
 * @author Serhii Bohdan
 * @see User
 * @see jakarta.persistence.MappedSuperclass
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@MappedSuperclass
public abstract class Educator extends User {

    /**
     * Indicates whether the educator's account is currently active.
     * <p>
     * This field is mapped to the {@code is_active} column in the database. A value of {@code true}
     * means the educator is active and can perform their duties, while {@code false} indicates the
     * account is inactive or suspended.
     */
    @Column(name = "is_active")
    private Boolean isActive;

}
