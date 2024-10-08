package ua.foxminded.universitycms.controller;

import java.util.Optional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.TopicDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.EntityNotFoundException;
import ua.foxminded.universitycms.exception.ValidationException;
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
     * This method handles GET requests to the `/new` endpoint. It retrieves the course ID from the request parameter and
     * creates a new empty `TopicDto` object with that course ID set. The topic data is then added to the model for display
     * in the creation form template.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param courseId the ID of the course the new topic belongs to (from request parameter)
     * @return the logical view name `CREATION_FORM` representing the topic creation template
     * @throws CustomHttpException if an unexpected error occurs
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
     * Attempts to create a new topic within a course.
     * <p>
     * This method handles POST requests to the `/create` endpoint. It binds the request parameters to a {@link TopicDto} object
     * and validates it. If there are validation errors, it returns the creation form view name. Otherwise, it attempts to
     * save the topic using the `topicService`. If successful, it redirects the user to the specific course details page
     * for the course the topic belongs to. If a validation exception occurs, it adds an error message to the model and
     * returns the creation form view name.
     *
     * @param model         the Spring MVC Model object used to store data for the view
     * @param topic         the topic data to be created (received from the form)
     * @param bindingResult the binding result containing any validation errors
     * @return a redirect URL on success, the creation form view name on validation errors, or throws an exception
     * @throws CustomHttpException if an unexpected error occurs during topic creation
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('TOPICS_CREATE')")
    public String performTopicCreation(Model model, @ModelAttribute("topic") @Valid TopicDto topic,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TOPIC_CREATION_FORM;
        }

        try {
            topicService.save(topic);
            return String.format(USER_SPECIFIC_COURSE_REDIRECT, topic.getCourseId());
        } catch (ValidationException e) {
            model.addAttribute(ModelAttributeNames.ERROR_MESSAGE_ATTRIBUTE, """
                An error occurred while adding a new topic. The rules of uniqueness
                are violated. The topic name and its order must be unique within a
                particular course.""");
            return ViewNames.TOPIC_CREATION_FORM;
        }
    }

    /**
     * Retrieves the topic update form for an existing topic.
     * <p>
     * This method handles GET requests to the `/{topicId}/edit` endpoint. It attempts to retrieve the topic with the
     * provided `topicId` using the `topicService`. If the topic is found, it adds the topic data to the model for display
     * in the update form template. Otherwise, it throws a `CustomHttpException` with a not found status.
     *
     * @param model   the Spring MVC Model object used to store data for the view
     * @param topicId the ID of the topic to be updated
     * @return the logical view name `UPDATE_FORM` representing the topic update template
     * @throws CustomHttpException if the topic with the provided ID is not found
     */
    @GetMapping("/{topicId}/edit")
    @PreAuthorize("hasAuthority('TOPICS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("topicId") long topicId) {
        Optional<TopicDto> optional = topicService.getById(topicId);

        if (optional.isPresent()) {
            model.addAttribute(ModelAttributeNames.TOPIC_ATTRIBUTE, optional.get());
            return ViewNames.TOPIC_UPDATE_FORM;
        }

        throw new CustomHttpException(HttpStatus.NOT_FOUND, "Editing failed. Topic not found.");
    }

    /**
     * Attempts to update an existing topic.
     * <p>
     * This method handles PUT requests to the `/update` endpoint. It binds the request parameters to a {@link TopicDto} object
     * and validates it. If there are validation errors, it returns the update form view name. Otherwise, it attempts to
     * update the topic using the `topicService`. If successful, it redirects the user to the specific course details page
     * for the course the topic belongs to. If a validation exception occurs, it adds an error message to the model and
     * returns the update form view name. If the topic is not found, it throws a `CustomHttpException` with a not found status.
     *
     * @param model         the Spring MVC Model object used to store data for the view
     * @param topic         the topic data to be updated (received from the form)
     * @param bindingResult the binding result containing any validation errors
     * @return a redirect URL on success, the update form view name on validation errors, or throws an exception
     * @throws CustomHttpException if the topic with the provided ID is not found or an unexpected error occurs
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('TOPICS_UPDATE')")
    public String performTopicUpdate(Model model, @ModelAttribute("topic") @Valid TopicDto topic,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TOPIC_UPDATE_FORM;
        }

        try {
            topicService.update(topic);
            return String.format(USER_SPECIFIC_COURSE_REDIRECT, topic.getCourseId());
        } catch (ValidationException e) {
            model.addAttribute(ModelAttributeNames.ERROR_MESSAGE_ATTRIBUTE, """
                An error occurred while updating a topic. The rules of uniqueness are
                violated. The topic name and its order must be unique within a particular
                course.""");
            return ViewNames.TOPIC_UPDATE_FORM;
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Update failed. Topic not found.");
        }
    }

    /**
     * Deletes a specified topic from a course.
     * <p>
     * This method handles DELETE requests to the `/{topicId}/delete` endpoint. It attempts to delete the topic with the
     * provided `topicId` using the `topicService`. If successful, it redirects the user to the specific course details page
     * for the course the topic belonged to (using the provided `courseId`). If the topic is not found, it throws a
     * `CustomHttpException` with a not found status.
     *
     * @param topicId  the ID of the topic to be deleted
     * @param courseId the ID of the course the topic belongs to (from request parameter)
     * @return a redirect URL to the course details page on success
     * @throws CustomHttpException if the topic with the provided ID is not found
     */
    @DeleteMapping("/{topicId}/delete")
    @PreAuthorize("hasAuthority('TOPICS_DELETE')")
    public String performTopicDeletion(@PathVariable("topicId") long topicId,
                                       @RequestParam(COURSE_ID_PARAM_NAME) long courseId) {
        try {
            topicService.deleteById(topicId);
            return String.format(USER_SPECIFIC_COURSE_REDIRECT, courseId);
        } catch (EntityNotFoundException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Deletion failed. Topic not found.");
        }
    }

}
