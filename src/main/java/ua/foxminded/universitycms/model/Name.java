package ua.foxminded.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * The {@code Name} class is an embeddable class that represents a name.
 * <p>
 * This class is annotated with {@code @Embeddable}, indicating that it's
 * intended to be used as a part of other entities. This class includes fields
 * for the first name and last name.
 *
 * @author Serhii Bohdan
 */
@Embeddable
public class Name {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    /**
     * Constructs a new {@code Name} object with the given parameters.
     *
     * @param firstName the first name
     * @param lastName  the last name
     */
    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Constructs a new {@code Name} object with default values.
     */
    public Name() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns a string representation of the name.
     *
     * @return a string representation of this name
     */
    @Override
    public String toString() {
        return "Name [firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
