package ua.foxminded.universitycms.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.universitycms.dto.AdminDto;
import ua.foxminded.universitycms.service.AdminService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller for handling web requests related to managing and displaying admin users.
 * <p>
 * This controller provides endpoints for retrieving paginated lists of admins, with optional filtering
 * by name. It also handles errors that may occur during data retrieval.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/admins")
public class AdminController {

    /**
     * The service responsible for managing admin user data.
     */
    private final AdminService adminService;

    /**
     * Renders the {@code all-admins} view, displaying a paginated list of admin users.
     * <p>
     * This method supports optional filtering of admins by name using the {@code keyword} parameter. If no keyword
     * is provided, all admins are retrieved. The retrieved admins are then added to the model along with
     * pagination information for rendering in the view.
     * <p>
     * Additionally, it handles potential errors during data retrieval and sets a flag in the model to indicate
     * if an error occurred.
     *
     * @param model    the Spring MVC Model object for storing data to be passed to the view
     * @param keyword  an optional keyword for filtering admins by name (can be null or blank)
     * @param pageable the pagination information, including page number, page size, and sorting
     * @return the logical name of the view template ({@code admins/all-admins})
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINS_READ')")
    public String getPageWithAdmins(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                    @PageableDefault Pageable pageable) {
        Page<AdminDto> adminsPage = StringUtils.isBlank(keyword)
            ? adminService.getAdminsPage(pageable)
            : adminService.getAdminInPageByName(keyword, pageable);

        model.addAttribute(ModelAttributeNames.ADMINS_ALL_NAMES_ATTRIBUTE, adminService.getAllNamesOfAdmins())
            .addAttribute(ModelAttributeNames.ADMINS_ATTRIBUTE, adminsPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, adminsPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, adminsPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_ADMINS_PAGE;
    }

}
