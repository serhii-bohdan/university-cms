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
 * Spring MVC Controller for handling topic-related requests under the {@code /ui/v1/topics} path.
 * Manages operations such as creating, updating, and deleting topics within courses. Uses
 * {@link TopicService} for business logic. Annotated with {@code @Controller} and
 * {@code @RequiredArgsConstructor}.
 *
 * @author Serhii Bohdan
 * @see TopicService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/topics"})
public class TopicController {

    /**
     * The name of the request parameter used to specify the course ID.
     */
    private static final String COURSE_ID_PARAM_NAME = "cid";

    /**
     * Redirect URL template to a specific course page, with a placeholder for course ID.
     */
    private static final String USER_SPECIFIC_COURSE_REDIRECT = "redirect:/ui/v1/courses/my/%s";

    /**
     * Service for interacting with topic data and performing business logic operations.
     */
    private final TopicService topicService;

    /**
     * Displays the form for creating a new topic within a course.
     * Handles GET requests to {@code /ui/v1/topics/new}. Prepares a {@link TopicDto} with the
     * specified course ID for the form. Requires {@code TOPICS_CREATE} authority.
     *
     * @param model    the {@link Model} to store form data
     * @param courseId the ID of the course for the new topic, from request parameter
     * @return view name {@link ViewNames#TOPIC_CREATION_FORM} for the creation form
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
     * Processes the submission of the topic creation form.
     * Handles POST requests to {@code /ui/v1/topics/create}. Validates {@link TopicDto} and saves
     * the topic via {@link TopicService#save}. Returns the form on errors. Requires
     * {@code TOPICS_CREATE}.
     *
     * @param topic         the {@link TopicDto} with form data
     * @param bindingResult validation results for the DTO
     * @return redirect to {@link #USER_SPECIFIC_COURSE_REDIRECT} or form view on errors
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('TOPICS_CREATE')")
    public String performTopicCreation(@ModelAttribute("topic") @Valid TopicDto topic, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TOPIC_CREATION_FORM;
        }

        topicService.save(topic);
        return USER_SPECIFIC_COURSE_REDIRECT.formatted(topic.getCourseId());
    }

    /**
     * Displays the form for updating a topic's information.
     * Handles GET requests to {@code /ui/v1/topics/{topicId}/edit}. Retrieves topic data via
     * {@link TopicService#getById} for the form. Requires {@code TOPICS_UPDATE}.
     *
     * @param model   the {@link Model} to store form data
     * @param topicId the ID of the topic to update
     * @return view name {@link ViewNames#TOPIC_UPDATE_FORM} for the update form
     */
    @GetMapping("/{topicId}/edit")
    @PreAuthorize("hasAuthority('TOPICS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("topicId") long topicId) {
        model.addAttribute(ModelAttributeNames.TOPIC_ATTRIBUTE, topicService.getById(topicId));
        return ViewNames.TOPIC_UPDATE_FORM;
    }

    /**
     * Processes the update of a topic's information.
     * Handles PUT requests to {@code /ui/v1/topics/update}. Validates {@link TopicDto} and updates
     * via {@link TopicService#update}. Returns form on errors. Requires {@code TOPICS_UPDATE}.
     *
     * @param topic         the {@link TopicDto} with updated data
     * @param bindingResult validation results for the DTO
     * @return redirect to {@link #USER_SPECIFIC_COURSE_REDIRECT} or form view on errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('TOPICS_UPDATE')")
    public String performTopicUpdate(@ModelAttribute("topic") @Valid TopicDto topic, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.TOPIC_UPDATE_FORM;
        }

        topicService.update(topic);
        return USER_SPECIFIC_COURSE_REDIRECT.formatted(topic.getCourseId());
    }

    /**
     * Deletes a topic from a course.
     * Handles DELETE requests to {@code /ui/v1/topics/{topicId}/delete}. Deletes topic via
     * {@link TopicService#deleteById} and redirects to the course page. Requires
     * {@code TOPICS_DELETE}.
     *
     * @param topicId  the ID of the topic to delete
     * @param courseId the ID of the course the topic belongs to, from request parameter
     * @return redirect to {@link #USER_SPECIFIC_COURSE_REDIRECT}
     */
    @DeleteMapping("/{topicId}/delete")
    @PreAuthorize("hasAuthority('TOPICS_DELETE')")
    public String performTopicDeletion(@PathVariable("topicId") long topicId,
                                       @RequestParam(COURSE_ID_PARAM_NAME) long courseId) {
        topicService.deleteById(topicId);
        return USER_SPECIFIC_COURSE_REDIRECT.formatted(courseId);
    }

}
