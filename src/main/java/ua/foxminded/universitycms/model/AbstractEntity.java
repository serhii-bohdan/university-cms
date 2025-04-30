package ua.foxminded.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base class for entities mapped to database tables using JPA (Java Persistence API).
 * <p>
 * This class provides a common identifier field ({@code id}) and is not intended for direct instantiation.
 * Subclasses should extend this class to inherit the identifier and other shared behavior.
 * The {@code @MappedSuperclass} annotation ensures that fields defined here are included in the persistence
 * mapping of subclasses, without this class being mapped to its own table.
 *
 * @author Serhii Bohdan
 * @see jakarta.persistence.MappedSuperclass
 * @see jakarta.persistence.Entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@MappedSuperclass
public abstract class AbstractEntity {

    /**
     * Unique identifier and primary key for the entity.
     * <p>
     * This field is mapped to the {@code id} column in the database table and is automatically generated
     * using the {@code GenerationType.IDENTITY} strategy, which relies on an auto-incrementing database column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

}
