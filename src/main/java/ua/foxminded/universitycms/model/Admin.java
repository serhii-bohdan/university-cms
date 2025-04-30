package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents an administrator entity within the university management system.
 * <p>
 * This class extends {@link User} to inherit common user properties and adds attributes specific to
 * administrators, such as their assigned role. It is mapped to the {@code admins} table in the database
 * using JPA annotations. Instances of this class represent individual admin users with permissions
 * defined by their associated {@link Role}.
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
@Table(name = "admins")
public class Admin extends User {

    /**
     * The role assigned to the admin, defining their permissions within the system.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Role} entity and is mapped to
     * the {@code role_id} column in the {@code admins} table. The role is eagerly fetched and must not
     * be null, as it determines the admin's access rights and capabilities.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
