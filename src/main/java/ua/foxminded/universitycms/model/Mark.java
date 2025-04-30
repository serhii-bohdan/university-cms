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
 * Represents a mark (grade) assigned to a student for a specific topic in the university management system.
 * <p>
 * This class extends {@link AbstractEntity} to inherit a unique identifier and defines attributes
 * specific to a mark, such as its numerical value, an optional comment, the associated student, and
 * the topic it pertains to. It is mapped to the {@code marks} table in the database using JPA
 * annotations. Instances of this class represent individual grades received by a {@link Student} for
 * a {@link Topic}, providing a record of academic performance.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Student
 * @see Topic
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
     * The numerical value of the mark (grade) assigned to the student.
     * <p>
     * This field is mapped to the {@code mark_value} column in the {@code marks} table and represents
     * the student's performance score for the associated topic.
     */
    @Column(name = "mark_value")
    private Integer markValue;

    /**
     * An optional comment providing additional context or feedback about the mark.
     * <p>
     * This field is mapped to the {@code comment} column in the {@code marks} table and may include
     * instructor notes or observations related to the grade.
     */
    @Column(name = "comment")
    private String comment;

    /**
     * The student who received this mark.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Student} entity and is mapped
     * to the {@code student_id} column in the {@code marks} table. The student is lazily fetched
     * ({@code FetchType.LAZY}) and must not be null, identifying the recipient of the grade.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * The topic for which this mark was assigned.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Topic} entity and is mapped to
     * the {@code topic_id} column in the {@code marks} table. The topic is lazily fetched
     * ({@code FetchType.LAZY}) and must not be null, linking the grade to its specific subject matter.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

}
