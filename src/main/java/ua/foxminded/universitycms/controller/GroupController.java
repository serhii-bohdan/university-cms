package ua.foxminded.universitycms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.GroupDto;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.GroupService;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This Spring Boot Web Controller handles requests related to managing and displaying groups.
 * It maps GET requests to the {@code /ui/v1/groups} path.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequestMapping("/ui/v1/groups")
public class GroupController {

    /**
     * The {@link GroupService} used to interact with group data.
     */
    private final GroupService groupService;

    /**
     * Constructs a new {@code GroupController} instance with the given {@link GroupService}.
     *
     * @param groupService the {@link GroupService} to use for group-related operations
     */
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Retrieves a page of group data for display and populates the model with necessary attributes.
     * <p>
     * This method handles GET requests to the endpoint responsible for displaying a paginated list of groups.
     * It utilizes the `groupService` to retrieve group data based on a provided keyword (optional)
     * and pagination information.
     *
     * @param model    the Spring MVC Model object used to store data for the view
     * @param keyword  an optional search keyword for filtering groups by name (can be blank)
     * @param pageable the Pageable object containing pagination information (size, page number)
     * @return the logical view name "groups/all-groups" representing the group list template
     */
    @GetMapping
    public String getPageWithGroups(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                    @PageableDefault Pageable pageable) {
        Page<GroupDto> groupsPage = new PageImpl<>(new ArrayList<>());
        boolean hasError = false;

        try {
            if (Objects.nonNull(keyword) && !keyword.isBlank()) {
                groupsPage = groupService.getGroupInPageByName(keyword, pageable);
            } else {
                groupsPage = groupService.getGroupsPage(pageable);
            }
        } catch (ServiceException e) {
            hasError = true;
        }

        model.addAttribute("allNamesOfGroups", groupService.getAllNamesOfGroups())
            .addAttribute("groups", groupsPage.getContent())
            .addAttribute("page", pageable.getPageNumber())
            .addAttribute("totalItems", groupsPage.getTotalElements())
            .addAttribute("totalPages", groupsPage.getTotalPages())
            .addAttribute("size", pageable.getPageSize())
            .addAttribute("keyword", keyword)
            .addAttribute("hasError", hasError);

        return "groups/all-groups";
    }

}
