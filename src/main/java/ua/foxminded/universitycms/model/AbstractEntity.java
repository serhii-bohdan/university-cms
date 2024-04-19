package ua.foxminded.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This abstract class serves as a base class for entities that will be mapped to database tables
 * using JPA (Java Persistence API). It provides a common identifier field ({@code id}) and shouldn't
 * be directly instantiated. Subclasses should extend this class to inherit the identifier field
 * and any other common behavior.
 * <p>
 * {@code @MappedSuperclass} This annotation indicates that this class is a base class for entities and
 * its fields will be included in the persistence mapping, but the class itself
 * won't be mapped to a table.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@MappedSuperclass
public abstract class AbstractEntity {

    /**
     * Unique identifier for the entity. This field is mapped to the {@code id} column in the database table.
     * The value is generated automatically using the {@code GenerationType.IDENTITY} strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

}
