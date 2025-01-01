package ua.foxminded.universitycms.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.service.GroupService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying groups.
 * It maps GET requests to the {@code /ui/v1/groups} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/groups")
public class GroupController {

    /**
     * The URL used to redirect to the page displaying all groups.
     */
    private static final String ALL_GROUPS_REDIRECT_URL = "redirect:/ui/v1/groups";

    /**
     * The URL template used to redirect to a specific group page, with the group ID dynamically inserted.
     */
    private static final String SPECIFIC_GROUP_REDIRECT_URL = "redirect:/ui/v1/groups/%s";

    /**
     * The {@link GroupService} used to interact with group data.
     */
    private final GroupService groupService;

    /**
     * Retrieves a page of group data for display and populates the model with necessary attributes.
     * <p>
     * This method handles GET requests to the endpoint responsible for displaying a paginated list of groups.
     * It utilizes the {@code groupService} to retrieve group data based on a provided keyword (optional)
     * and pagination information.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional search keyword for filtering groups by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return the logical view name {@code groups/all-groups} representing the group list template
     */
    @GetMapping
    @PreAuthorize("hasAuthority('GROUPS_READ')")
    public String getPageWithGroups(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                    @PageableDefault Pageable pageable) {
        Page<GroupDto> groupsPage = StringUtils.isBlank(keyword)
            ? groupService.getGroupsPage(pageable)
            : groupService.getGroupInPageByName(keyword, pageable);

        model.addAttribute(ModelAttributeNames.GROUPS_ALL_NAMES_ATTRIBUTE, getGroupNames(groupService.getAll()))
            .addAttribute(ModelAttributeNames.GROUPS_ATTRIBUTE, groupsPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, groupsPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, groupsPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_GROUPS_PAGE;
    }

    /**
     * Retrieves a page displaying groups whose students are not enrolled in a specific course.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/groups/for-enroll} endpoint and returns
     * a view containing a list of groups that are eligible for enrollment in a specified course.
     * It supports optional keyword-based filtering for group names.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional keyword for filtering group names (can be null or blank)
     * @param courseId the ID of the course to find groups for enrollment
     * @return the logical view name {@code ViewNames.GROUPS_FOR_ENROLL_IN_COURSE} representing the page with groups eligible for enrollment
     */
    @GetMapping("/for-enroll")
    @PreAuthorize("hasAuthority('GROUPS_READ')")
    public String getPageWithGroupsForEnrollInCourse(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                                     @RequestParam("cid") long courseId) {
        List<GroupDto> groupsWhoseStudentsNotEnrolledInCourse = groupService.getListOfGroupsWhoseStudentsNotEnrolledInCourse(courseId);
        List<GroupDto> selectedGroups = StringUtils.isBlank(keyword)
            ? groupsWhoseStudentsNotEnrolledInCourse
            : findGroupByName(groupsWhoseStudentsNotEnrolledInCourse, keyword);

        model.addAttribute(ModelAttributeNames.GROUPS_ALL_NAMES_ATTRIBUTE, getGroupNames(groupsWhoseStudentsNotEnrolledInCourse))
            .addAttribute(ModelAttributeNames.GROUPS_ATTRIBUTE, selectedGroups)
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.GROUPS_FOR_ENROLL_IN_COURSE;
    }

    /**
     * Retrieves a specific group by its ID.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/groups/{groupId}} endpoint. It retrieves the group
     * with the provided {@code groupId} using the {@code groupService}. If the group is found, it adds the group data
     * to the model for display in the specific group view.
     *
     * @param model   the Spring MVC Model object used to store data for the view
     * @param groupId the ID of the group to retrieve
     * @return the logical view name {@code ViewNames.SPECIFIC_GROUP} representing the specific group template
     */
    @GetMapping("/{groupId}")
    @PreAuthorize("hasAuthority('GROUPS_READ')")
    public String getPageWithSpecificGroup(Model model, @PathVariable("groupId") long groupId) {
        model.addAttribute(ModelAttributeNames.GROUP_ATTRIBUTE, groupService.getById(groupId));
        return ViewNames.SPECIFIC_GROUP;
    }

    /**
     * Retrieves the group creation form.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/groups/new} endpoint. It
     * creates a new empty {@link GroupDto} object and adds it to the model for display
     * in the group creation form template.
     *
     * @param model the Spring MVC Model object used to store data for the view
     * @return the group creation template
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('GROUPS_CREATE')")
    public String getCreationForm(Model model) {
        GroupDto newGroup = GroupDto.builder().build();
        model.addAttribute(ModelAttributeNames.GROUP_ATTRIBUTE, newGroup);
        return ViewNames.GROUP_CREATION_FORM;
    }

    /**
     * Processes group creation form submission and saves a new group.
     * <p>
     * Handles validation, delegation to groupService, and redirects on success.
     * Requires {@code GROUPS_CREATE} authority.
     *
     * @param group         the group data to be created (received from the form)
     * @param bindingResult the binding result containing any validation errors
     * @return a redirect URL on success, the creation form view name on validation errors
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('GROUPS_CREATE')")
    public String performGroupCreation(@ModelAttribute("group") @Valid GroupDto group,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.GROUP_CREATION_FORM;
        }

        groupService.save(group);
        return ALL_GROUPS_REDIRECT_URL;
    }

    /**
     * Retrieves the group update form for a specific group.
     * <p>
     * This method handles GET requests to the {@code /ui/v1/groups/{groupId}/edit} endpoint. It retrieves
     * the group with the provided {@code groupId} using the {@code groupService}. If the group is found, it adds
     * the group data to the model for display in the group update form.
     *
     * @param model   the Spring MVC Model object used to store data for the view
     * @param groupId the ID of the group to be updated
     * @return the group update template
     */
    @GetMapping("/{groupId}/edit")
    @PreAuthorize("hasAuthority('GROUPS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("groupId") long groupId) {
        model.addAttribute(ModelAttributeNames.GROUP_ATTRIBUTE, groupService.getById(groupId));
        return ViewNames.GROUP_UPDATE_FORM;
    }

    /**
     * Attempts to update an existing group.
     * <p>
     * This method handles PUT requests to the {@code /ui/v1/groups/update} endpoint. It binds
     * the request parameters to a {@link GroupDto} object and validates it. If there are
     * validation errors, it returns the group update form view name. Otherwise, it attempts
     * to update the group using the {@code groupService}. If successful, it redirects the user to
     * the all groups page.
     *
     * @param group         the group data to be updated (received from the form)
     * @param bindingResult the binding result containing any validation errors
     * @return the view name of the group update form if validation errors exist,
     * or a redirect URL to the specific group page on a successful update
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('GROUPS_UPDATE')")
    public String performGroupUpdate(@ModelAttribute("group") @Valid GroupDto group, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.GROUP_UPDATE_FORM;
        }

        groupService.update(group);
        return String.format(SPECIFIC_GROUP_REDIRECT_URL, group.getId());
    }

    /**
     * Deletes an existing group.
     * <p>
     * This method handles DELETE requests to the {@code /ui/v1/groups/{groupId}/delete} endpoint, where
     * {@code groupId} is the ID of the group to be deleted. It attempts to delete the group using the
     * {@code groupService}. If successful, it redirects the user to the all groups page.
     *
     * @param groupId the ID of the group to be deleted
     * @return a redirect URL on success
     */
    @DeleteMapping("/{groupId}/delete")
    @PreAuthorize("hasAuthority('GROUPS_DELETE')")
    public String performGroupDeletion(@PathVariable("groupId") long groupId) {
        groupService.deleteById(groupId);
        return ALL_GROUPS_REDIRECT_URL;
    }

    private List<String> getGroupNames(List<GroupDto> groups) {
        return groups.stream()
            .map(GroupDto::getGroupName)
            .toList();
    }

    private List<GroupDto> findGroupByName(List<GroupDto> groups, String groupName) {
        return groups.stream()
            .filter(g -> g.getGroupName().equals(groupName))
            .toList();
    }

}
