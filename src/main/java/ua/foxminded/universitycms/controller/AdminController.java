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
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.service.AdminService;

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
     * Renders the "all-admins" view, displaying a paginated list of admin users.
     * <p>
     * This method supports optional filtering of admins by name using the `keyword` parameter. If no keyword
     * is provided, all admins are retrieved. The retrieved admins are then added to the model along with
     * pagination information for rendering in the view.
     * <p>
     * Additionally, it handles potential errors during data retrieval and sets a flag in the model to indicate
     * if an error occurred.
     *
     * @param model    the Spring MVC Model object for storing data to be passed to the view
     * @param keyword  an optional keyword for filtering admins by name (can be null or blank)
     * @param pageable the pagination information, including page number, page size, and sorting
     * @return the logical name of the view template ("admins/all-admins")
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINS_READ')")
    public String getPageWithAdmins(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                    @PageableDefault Pageable pageable) {
        Page<AdminDto> adminsPage;

        try {
            adminsPage = StringUtils.isBlank(keyword)
                ? adminService.getAdminsPage(pageable)
                : adminService.getAdminInPageByName(keyword, pageable);
        } catch (InvalidFullNameFormatException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Invalid full name format");
        }

        model.addAttribute("allNamesOfAdmins", adminService.getAllNamesOfAdmins())
            .addAttribute("admins", adminsPage.getContent())
            .addAttribute("page", pageable.getPageNumber())
            .addAttribute("totalItems", adminsPage.getTotalElements())
            .addAttribute("totalPages", adminsPage.getTotalPages())
            .addAttribute("size", pageable.getPageSize())
            .addAttribute("keyword", keyword);

        return "admins/all-admins";
    }

}
