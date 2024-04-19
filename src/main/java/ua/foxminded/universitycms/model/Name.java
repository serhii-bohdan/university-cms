package ua.foxminded.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * This class represents a person's name, which can be embedded in other entities.
 * <p>
 * {@code @Embeddable} this annotation is used to indicate that this class can be embedded
 * in other entities.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class Name {

    /**
     * The first name of the person.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * The last name of the person.
     */
    @Column(name = "last_name")
    private String lastName;

}
