package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a role within the application's authorization model.
 * <p>
 * Roles define a set of permissions that are granted to users who are assigned that role.
 * This entity is mapped to the "roles" table in the database.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"roleName"})
@ToString(callSuper = true, exclude = {"permissions"})
@SuperBuilder
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {

    /**
     * The unique name of the role, corresponding to the values in the {@link RoleName} enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    /**
     * The set of permissions associated with this role.
     * <p>
     * Note: The `FetchType.EAGER` strategy is used to eagerly fetch permissions when a role is loaded.
     * Consider if this is the desired behavior for your application's performance.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

}
