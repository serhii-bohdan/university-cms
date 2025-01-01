package ua.foxminded.universitycms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.service.TopicService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Controller class responsible for handling topic-related functionalities within the application.
 * <p>
 * This controller provides endpoints for creating, updating, deleting, and managing topics associated with courses.
 * It utilizes the {@link TopicService} to interact with topic data and provides a user interface for topic management.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/topics")
public class TopicController {

    /**
     * The name of the request parameter used to specify the course ID.
     */
    private static final String COURSE_ID_PARAM_NAME = "cid";

    /**
     * The redirect URL format used to redirect the user to the specific course details page.
     */
    private static final String USER_SPECIFIC_COURSE_REDIRECT = "redirect:/ui/v1/courses/my/%s";

    /**
     * The injected {@link TopicService} used to interact with topic data.
     */
    private final TopicService topicService;

    /**
     * Retrieves the topic creation form for a new topic within a specific course.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/topics/new} endpoint. It retrieves the course ID from
     * the request parameter and creates a new empty `{@link TopicDto}` object with that course ID set. The topic data
     * is then added to the model for display in the creation form template.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param courseId the ID of the course the new topic belongs to (from request parameter)
     * @return the logical view name `CREATION_FORM` representing the topic creation template
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('TOPICS_CREATE')")
    public String getCreationForm(Model model, @RequestParam(COURSE_ID_PARAM_NAME) long courseId) {
        TopicDto topic = TopicDto.builder()
            .courseId(courseId)
            .build();

        model.addAttribute(ModelAttributeNames.TOPIC_ATTRIBUTE, topic);
        return ViewNames.TOPIC_CREATION_FORM;
    }

    /**
     * Handles the creation of a new topic within a course.
     * <p>
     * This method processes the topic creation request by first validating the input using the {@link TopicDto}.
     * If validation fails, it returns the topic creation form with error messages. If the input is valid, it
     * proceeds to save the new topic using the {@link TopicService}. Upon successful creation, it redirects
     * to the specific course page associated with the newly created topic.
     *
     * @param topic         the {@link TopicDto} containing the details of the topic to be created
     * @param bindingResult the result of validating the {@link TopicDto}
     * @return a redirection URL to the course page that the new topic is associated with, or the topic creation form
     * if validation fails
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('TOPICS_CREATE')")
    public String performTopicCreation(@ModelAttribute("topic") @Valid TopicDto topic, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TOPIC_CREATION_FORM;
        }

        topicService.save(topic);
        return String.format(USER_SPECIFIC_COURSE_REDIRECT, topic.getCourseId());
    }

    /**
     * Retrieves the topic update form for an existing topic.
     * <p>
     * This method handles GET requests to the `{@code /ui/v1/topics/{topicId}/edit}` endpoint. It attempts to
     * retrieve the topic with the provided {@code topicId} using the {@code topicService}. If the topic is found,
     * it adds the topic data to the model for display in the update form template.
     *
     * @param model   the Spring MVC Model object used to store data for the view
     * @param topicId the ID of the topic to be updated
     * @return the logical view name {@code UPDATE_FORM} representing the topic update template
     */
    @GetMapping("/{topicId}/edit")
    @PreAuthorize("hasAuthority('TOPICS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("topicId") long topicId) {
        model.addAttribute(ModelAttributeNames.TOPIC_ATTRIBUTE, topicService.getById(topicId));
        return ViewNames.TOPIC_UPDATE_FORM;
    }

    /**
     * Handles the update of an existing topic.
     * <p>
     * This method processes the topic update request by first validating the input using the {@link TopicDto}.
     * If validation fails, it returns the topic update form with error messages. If the input is valid, it proceeds
     * to update the existing topic using the {@link TopicService}. Upon successful update, it redirects to the
     * specific course page that the updated topic belongs to.
     *
     * @param topic         the {@link TopicDto} containing the updated details of the topic
     * @param bindingResult the result of validating the {@link TopicDto}
     * @return a redirection URL to the course page that the updated topic is associated with, or the topic update form
     * if validation fails or the topic cannot be found
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('TOPICS_UPDATE')")
    public String performTopicUpdate(@ModelAttribute("topic") @Valid TopicDto topic, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TOPIC_UPDATE_FORM;
        }

        topicService.update(topic);
        return String.format(USER_SPECIFIC_COURSE_REDIRECT, topic.getCourseId());
    }

    /**
     * Deletes a specified topic from a course.
     * <p>
     * This method handles DELETE requests to the {@code /ui/v1/topics/{topicId}/delete} endpoint. It attempts to
     * delete the topic with the provided {@code topicId} using the {@code topicService}. If successful, it redirects
     * the user to the specific course details page for the course the topic belonged to (using the provided
     * {@code courseId}).
     *
     * @param topicId  the ID of the topic to be deleted
     * @param courseId the ID of the course the topic belongs to (from request parameter)
     * @return a redirect URL to the course details page on success
     */
    @DeleteMapping("/{topicId}/delete")
    @PreAuthorize("hasAuthority('TOPICS_DELETE')")
    public String performTopicDeletion(@PathVariable("topicId") long topicId,
                                       @RequestParam(COURSE_ID_PARAM_NAME) long courseId) {
        topicService.deleteById(topicId);
        return String.format(USER_SPECIFIC_COURSE_REDIRECT, courseId);
    }

}
