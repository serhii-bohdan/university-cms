package ua.foxminded.universitycms.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.universitycms.dto.MarkDto;
import ua.foxminded.universitycms.mapper.MarkMapper;
import ua.foxminded.universitycms.model.Mark;
import ua.foxminded.universitycms.model.Topic;
import ua.foxminded.universitycms.repository.MarkRepository;
import ua.foxminded.universitycms.repository.TopicRepository;
import ua.foxminded.universitycms.service.MarkService;

@SpringBootTest(classes = {MarkServiceImpl.class})
class MarkServiceImplTest {

    @MockBean
    private MarkRepository markRepositoryMock;

    @MockBean
    private TopicRepository topicRepositoryMock;

    @MockBean
    private MarkMapper markMapperMock;

    @Autowired
    private MarkService markService;

    @Test
    void getUnratedTopics_shouldMapWithCourseTopicThatHasNoMark_whenOnlyOneCourseTopicHasMark() {
        long studentId = 1L;
        long courseId = 1L;
        long firstTopicId = 1L;
        long secondTopicId = 2L;
        String firstTopicName = "FirstTopic";
        String secondTopicName = "SecondTopic";

        Topic firstTopic = Topic.builder()
            .id(firstTopicId)
            .topicName(firstTopicName)
            .build();

        Topic secondTopic = Topic.builder()
            .id(secondTopicId)
            .topicName(secondTopicName)
            .build();

        Mark mark = Mark.builder()
            .topic(firstTopic)
            .build();

        MarkDto markDto = MarkDto.builder()
            .topicId(firstTopicId)
            .build();

        when(markRepositoryMock.findMarksByStudentIdAndCourseId(studentId, courseId)).thenReturn(List.of(mark));
        when(markMapperMock.toDto(mark)).thenReturn(markDto);
        when(topicRepositoryMock.findByCourseId(courseId)).thenReturn(List.of(firstTopic, secondTopic));

        Map<String, Long> topicNamesWithIds = markService.getUnratedTopics(studentId, courseId);

        assertFalse(topicNamesWithIds.isEmpty());
        assertEquals(1, topicNamesWithIds.size());
        assertEquals(secondTopicId, topicNamesWithIds.get(secondTopicName));
    }

    @Test
    void getUnratedTopics_shouldEmptyMap_whenAllCourseTopicsHaveMarks() {
        long studentId = 1L;
        long courseId = 1L;
        long topicId = 1L;
        String topicName = "TopicName";

        Topic topic = Topic.builder()
            .id(topicId)
            .topicName(topicName)
            .build();

        Mark mark = Mark.builder()
            .topic(topic)
            .build();

        MarkDto markDto = MarkDto.builder()
            .topicId(topicId)
            .build();

        when(markRepositoryMock.findMarksByStudentIdAndCourseId(studentId, courseId)).thenReturn(List.of(mark));
        when(markMapperMock.toDto(mark)).thenReturn(markDto);
        when(topicRepositoryMock.findByCourseId(courseId)).thenReturn(List.of(topic));

        Map<String, Long> topicNamesWithIds = markService.getUnratedTopics(studentId, courseId);

        assertTrue(topicNamesWithIds.isEmpty());
    }

}
