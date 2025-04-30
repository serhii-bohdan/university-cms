package ua.foxminded.universitycms.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a topic entity within a course in the university management system.
 * <p>
 * This class extends {@link AbstractEntity} to inherit a unique identifier and defines attributes
 * specific to a topic, such as its name, description, order within the course, the associated course,
 * and related student marks. It is mapped to the {@code topics} table in the database using JPA
 * annotations. Instances of this class represent individual topics covered within a {@link Course},
 * with associated {@link Mark} entities reflecting student performance.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Course
 * @see Mark
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"topicName", "course"})
@ToString(callSuper = true, exclude = {"course", "marks"})
@SuperBuilder
@Entity
@Table(name = "topics")
public class Topic extends AbstractEntity {

    /**
     * The name of the topic, identifying it within the course.
     * <p>
     * This field is mapped to the {@code topic_name} column in the {@code topics} table.
     */
    @Column(name = "topic_name")
    private String topicName;

    /**
     * A description of the topic's content and scope.
     * <p>
     * This field is mapped to the {@code topic_description} column in the {@code topics} table.
     */
    @Column(name = "topic_description")
    private String topicDescription;

    /**
     * The order of the topic within the course curriculum.
     * <p>
     * This field is mapped to the {@code topic_order} column in the {@code topics} table. Lower values
     * indicate topics covered earlier in the course sequence, enabling a structured progression of
     * material.
     */
    @Column(name = "topic_order")
    private Integer topicOrder;

    /**
     * The course to which this topic belongs.
     * <p>
     * This field establishes a many-to-one relationship with the {@link Course} entity and is mapped
     * to the {@code course_id} column in the {@code topics} table. The course is lazily fetched
     * ({@code FetchType.LAZY}) and must not be null, linking the topic to its parent course.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The set of student marks associated with this topic.
     * <p>
     * This field defines a one-to-many relationship with the {@link Mark} entity, mapped by the
     * {@code topic} field in {@link Mark}. Marks are lazily fetched ({@code FetchType.LAZY}) and
     * managed with a cascading removal policy ({@code CascadeType.REMOVE}), meaning marks are deleted
     * when the topic is removed. The set is initialized as an empty {@code HashSet}.
     */
    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Mark> marks = new HashSet<>();

}
