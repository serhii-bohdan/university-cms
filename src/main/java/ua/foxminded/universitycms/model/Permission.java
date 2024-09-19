package ua.foxminded.universitycms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.universitycms.model.enumeration.PermissionName;

/**
 * Represents a permission within the application's authorization model.
 * <p>
 * Permissions are fine-grained access controls that define what actions a user with a particular
 * role is allowed to perform. This entity is mapped to the "permissions" table in the database.
 *
 * @author Serhii Bohdan
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
     * The unique name of the permission, corresponding to the values in the {@link PermissionName} enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "permission_name")
    private PermissionName permissionName;

}
