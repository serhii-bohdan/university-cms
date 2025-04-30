package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a manager user within the university management system.
 * <p>
 * This class extends {@link User} to inherit common user properties and adds attributes specific to
 * managers, such as their assigned role. It is mapped to the {@code managers} table in the database
 * using JPA annotations. Instances of this class represent individual manager users with permissions
 * defined by their associated {@link Role}. The class also inherits lifecycle callbacks from
 * {@link User} to automatically set creation and update timestamps.
 *
 * @author Serhii Bohdan
 * @see User
 * @see Role
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "managers")
public class Manager extends User {

    /**
     * The role assigned to the manager, defining their permissions within the system.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Role} entity and is mapped to
     * the {@code role_id} column in the {@code managers} table. The role is eagerly fetched
     * ({@code FetchType.EAGER}) and must not be null, as it determines the manager's access rights
     * and responsibilities.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
