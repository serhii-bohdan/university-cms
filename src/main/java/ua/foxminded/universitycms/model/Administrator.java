package ua.foxminded.universitycms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * The {@code Administrator} class represents an administrator in the system.
 * <p>
 * This class is a subclass of the {@link User} class. It represents a specific
 * type of user who has administrative privileges. This class is annotated with
 * {@code @Entity}, indicating that it's a JPA entity. This means that instances
 * of this class can be persisted to the database. The {@code @Table} annotation
 * specifies the name of the database table that corresponds to this entity.
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "administrators")
public class Administrator extends User {

    /**
     * Constructs a new {@code Administrator} object with the given parameters.
     *
     * @param firstName the first name of the administrator
     * @param lastName  the last name of the administrator
     * @param email     the email of the administrator
     * @param password  the password of the administrator
     * @param isActive  the activity status of the administrator
     */
    public Administrator(String firstName, String lastName, String email, String password, Boolean isActive) {
        super(firstName, lastName, email, password, isActive);
    }

    /**
     * Constructs a new {@code Administrator} object with default values.
     */
    public Administrator() {
        super();
    }

}
