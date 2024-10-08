package ua.foxminded.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The {@code Mark} class represents a mark (grade) obtained by a student in
 * a particular topic and inherits from the {@link AbstractEntity} class.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * mark's ID, value, the student who received the mark, and the topic for which
 * the mark was given. It also includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"student", "topic"})
@ToString(callSuper = true, exclude = {"student", "topic"})
@SuperBuilder
@Entity
@Table(name = "marks")
public class Mark extends AbstractEntity {

    /**
     * The numerical value of the mark (grade).
     */
    @Column(name = "mark_value")
    private Integer markValue;

    /**
     * An optional comment about the mark, providing additional context or feedback.
     */
    @Column(name = "comment")
    private String comment;

    /**
     * The student who received the mark.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * The topic for which the mark was given.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

}
