package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.model.enumeration.RoleName;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a role within the university management system's authorization model.
 * <p>
 * This entity defines a set of permissions granted to users assigned to the role, facilitating
 * role-based access control (RBAC). It extends {@link AbstractEntity} to inherit a unique identifier
 * and is mapped to the {@code roles} table in the database. Each role is uniquely identified by its
 * {@link RoleName} and can be associated with multiple {@link Permission} instances.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see RoleName
 * @see Permission
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
     * The unique name of the role, derived from the {@link RoleName} enumeration.
     * <p>
     * This field is stored as a string in the {@code role_name} column of the {@code roles} table,
     * ensuring that each role corresponds to a predefined value in the {@link RoleName} enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    /**
     * The set of permissions assigned to this role.
     * <p>
     * This field establishes a many-to-many relationship with the {@link Permission} entity, mapped
     * through the {@code roles_permissions} join table. Permissions are eagerly fetched
     * ({@code FetchType.EAGER}) when the role is loaded, which ensures immediate availability but may
     * impact performance in scenarios with large datasets. Consider using {@code FetchType.LAZY} if
     * optimization is required.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

}
