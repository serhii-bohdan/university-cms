package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.model.enumeration.PermissionName;

/**
 * Represents a permission within the university management system's authorization model.
 * <p>
 * This entity defines fine-grained access controls that specify the actions a user with an assigned
 * role can perform, supporting role-based access control (RBAC). It extends {@link AbstractEntity}
 * to inherit a unique identifier and is mapped to the {@code permissions} table in the database.
 * Each permission is uniquely identified by its {@link PermissionName} value.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see PermissionName
 * @see Role
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"permissionName"})
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "permissions")
public class Permission extends AbstractEntity {

    /**
     * The unique name of the permission, derived from the {@link PermissionName} enumeration.
     * <p>
     * This field is stored as a string in the {@code permission_name} column of the {@code permissions}
     * table, ensuring that each permission corresponds to a predefined value in the {@link PermissionName}
     * enum, such as {@code ADMINS_CREATE} or {@code STUDENTS_READ}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "permission_name")
    private PermissionName permissionName;

}
