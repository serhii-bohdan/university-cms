package ua.foxminded.universitycms.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * Spring MVC Controller for handling group-related requests under the {@code /ui/v1/groups} path.
 * Manages operations such as displaying group lists, creating, updating, and deleting groups.
 * Uses {@link GroupService} for business logic. Annotated with {@code @Controller} for MVC
 * handling and {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @author Serhii Bohdan
 * @see GroupService
 * @see ModelAttributeNames
 * @see ViewNames
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/ui/v1/groups"})
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
     * Service for interacting with group data and performing business logic operations.
     */
    private final GroupService groupService;

    /**
     * Displays a paginated list of groups, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/groups}. Retrieves groups via
     * {@link GroupService#findGroups} and adds pagination data and names to the model.
     * Requires {@code GROUPS_READ} authority.
     *
     * @param model    the {@link Model} to store view data
     * @param keyword  optional keyword to filter groups by name; may be blank
     * @param pageable pagination info from {@link PageableDefault}
     * @return view name {@link ViewNames#ALL_GROUPS_PAGE} for the group list
     */
    @GetMapping
    @PreAuthorize("hasAuthority('GROUPS_READ')")
    public String getPageWithGroups(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                    @PageableDefault Pageable pageable) {
        Page<GroupDto> groupsPage = groupService.findGroups(pageable, keyword);

        model.addAttribute(ModelAttributeNames.GROUPS_ALL_NAMES_ATTRIBUTE, groupService.extractGroupNames(groupService.getAll()))
            .addAttribute(ModelAttributeNames.GROUPS_ATTRIBUTE, groupsPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, groupsPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, groupsPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_GROUPS_PAGE;
    }

    /**
     * Displays groups with students not enrolled in a specific course, optionally filtered by keyword.
     * Handles GET requests to {@code /ui/v1/groups/for-enroll}. Retrieves groups via
     * {@link GroupService#getGroupsWithUnEnrolledStudents} and filters them. Requires
     * {@code GROUPS_READ}.
     *
     * @param model    the {@link Model} to store view data
     * @param keyword  optional keyword to filter groups by name; may be blank
     * @param courseId the ID of the course to check enrollment against
     * @return view name {@link ViewNames#GROUPS_FOR_ENROLL_IN_COURSE} for the list
     */
    @GetMapping("/for-enroll")
    @PreAuthorize("hasAuthority('GROUPS_READ')")
    public String getPageWithGroupsForEnrollInCourse(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                                     @RequestParam("cid") long courseId) {
        List<GroupDto> allUnEnrolledGroups = groupService.getGroupsWithUnEnrolledStudents(courseId);
        List<GroupDto> filteredGroups = groupService.filterGroupsByName(allUnEnrolledGroups, keyword);

        model.addAttribute(ModelAttributeNames.GROUPS_ALL_NAMES_ATTRIBUTE, groupService.extractGroupNames(allUnEnrolledGroups))
            .addAttribute(ModelAttributeNames.GROUPS_ATTRIBUTE, filteredGroups)
            .addAttribute(ModelAttributeNames.COURSE_ID_ATTRIBUTE, courseId)
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.GROUPS_FOR_ENROLL_IN_COURSE;
    }

    /**
     * Displays details of a specific group.
     * Handles GET requests to {@code /ui/v1/groups/{groupId}}. Retrieves group data via
     * {@link GroupService#getById} and adds it to the model. Requires {@code GROUPS_READ}.
     *
     * @param model   the {@link Model} to store view data
     * @param groupId the ID of the group to display
     * @return view name {@link ViewNames#SPECIFIC_GROUP} for group details
     */
    @GetMapping("/{groupId}")
    @PreAuthorize("hasAuthority('GROUPS_READ')")
    public String getPageWithSpecificGroup(Model model, @PathVariable("groupId") long groupId) {
        model.addAttribute(ModelAttributeNames.GROUP_ATTRIBUTE, groupService.getById(groupId));
        return ViewNames.SPECIFIC_GROUP;
    }

    /**
     * Displays the form for creating a new group.
     * Handles GET requests to {@code /ui/v1/groups/new}. Prepares a {@link GroupDto} for the form.
     * Requires {@code GROUPS_CREATE} authority.
     *
     * @param model the {@link Model} to store form data
     * @return view name {@link ViewNames#GROUP_CREATION_FORM} for the creation form
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('GROUPS_CREATE')")
    public String getCreationForm(Model model) {
        GroupDto newGroup = GroupDto.builder().build();
        model.addAttribute(ModelAttributeNames.GROUP_ATTRIBUTE, newGroup);
        return ViewNames.GROUP_CREATION_FORM;
    }

    /**
     * Processes the submission of the group creation form.
     * Handles POST requests to {@code /ui/v1/groups/create}. Validates {@link GroupDto} and saves
     * the group via {@link GroupService#save}. Returns the form on errors. Requires
     * {@code GROUPS_CREATE}.
     *
     * @param group         the {@link GroupDto} with form data
     * @param bindingResult validation results for the DTO
     * @return redirect to {@link #ALL_GROUPS_REDIRECT_URL} or form view on errors
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
     * Displays the form for updating a group's information.
     * Handles GET requests to {@code /ui/v1/groups/{groupId}/edit}. Retrieves group data via
     * {@link GroupService#getById} for the form. Requires {@code GROUPS_UPDATE}.
     *
     * @param model   the {@link Model} to store form data
     * @param groupId the ID of the group to update
     * @return view name {@link ViewNames#GROUP_UPDATE_FORM} for the update form
     */
    @GetMapping("/{groupId}/edit")
    @PreAuthorize("hasAuthority('GROUPS_UPDATE')")
    public String getUpdateForm(Model model, @PathVariable("groupId") long groupId) {
        model.addAttribute(ModelAttributeNames.GROUP_ATTRIBUTE, groupService.getById(groupId));
        return ViewNames.GROUP_UPDATE_FORM;
    }

    /**
     * Processes the update of a group's information.
     * Handles PUT requests to {@code /ui/v1/groups/update}. Validates {@link GroupDto} and updates
     * via {@link GroupService#update}. Returns form on errors. Requires {@code GROUPS_UPDATE}.
     *
     * @param group         the {@link GroupDto} with updated data
     * @param bindingResult validation results for the DTO
     * @return redirect to group page or form view on errors
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('GROUPS_UPDATE')")
    public String performGroupUpdate(@ModelAttribute("group") @Valid GroupDto group, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.GROUP_UPDATE_FORM;
        }

        groupService.update(group);
        return SPECIFIC_GROUP_REDIRECT_URL.formatted(group.getId());
    }

    /**
     * Deletes a group from the system.
     * Handles DELETE requests to {@code /ui/v1/groups/{groupId}/delete}. Deletes group via
     * {@link GroupService#deleteById} and redirects. Requires {@code GROUPS_DELETE}.
     *
     * @param groupId the ID of the group to delete
     * @return redirect to {@link #ALL_GROUPS_REDIRECT_URL}
     */
    @DeleteMapping("/{groupId}/delete")
    @PreAuthorize("hasAuthority('GROUPS_DELETE')")
    public String performGroupDeletion(@PathVariable("groupId") long groupId) {
        groupService.deleteById(groupId);
        return ALL_GROUPS_REDIRECT_URL;
    }

}
