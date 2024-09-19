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
 * The {@code Topic} class represents a topic covered within a course and
 * inherits from the {@link AbstractEntity} class.
 * <p>
 * This class is annotated with {@code @Entity}, indicating that it's a JPA
 * entity. This means that instances of this class can be persisted to the
 * database. The {@code @Table} annotation specifies the name of the database
 * table that corresponds to this entity. This class includes fields for the
 * topic's ID, name, description, the course it belongs to, and the marks
 * associated with it. It also includes methods to get and set these fields.
 *
 * @author Serhii Bohdan
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
     * The name of the topic.
     */
    @Column(name = "topic_name")
    private String topicName;

    /**
     * A description of the topic content.
     */
    @Column(name = "topic_description")
    private String topicDescription;

    /**
     * The order (position) of the topic within the course curriculum.
     * Lower numbers indicate topics covered earlier in the course.
     */
    @Column(name = "topic_order")
    private Integer topicOrder;

    /**
     * The course that this topic belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The student marks (grades) associated with this topic.
     */
    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Mark> marks = new HashSet<>();

    /**
     * Constructs a new {@code Topic} object with the given parameters.
     *
     * @param topicName        the name of the topic
     * @param topicDescription the description of the topic
     * @param topicOrder       the order (position) of the topic within the course
     * @param course           the course that the topic belongs to
     */
    public Topic(String topicName, String topicDescription, Integer topicOrder, Course course) {
        this.topicName = topicName;
        this.topicDescription = topicDescription;
        this.topicOrder = topicOrder;
        this.course = course;
    }

}
