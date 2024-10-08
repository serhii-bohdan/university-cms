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
import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.exception.CustomHttpException;
import ua.foxminded.universitycms.exception.InvalidFullNameFormatException;
import ua.foxminded.universitycms.service.ManagerService;
import ua.foxminded.universitycms.util.ModelAttributeNames;
import ua.foxminded.universitycms.util.ViewNames;

/**
 * Spring MVC Controller for handling web requests related to managing and displaying managers.
 * <p>
 * This controller provides endpoints for retrieving paginated lists of managers, with optional filtering
 * by name. It also handles errors that may occur during data retrieval.
 *
 * @author Serhii Bohdan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/v1/managers")
public class ManagerController {

    /**
     * The service responsible for managing manager data.
     */
    private final ManagerService managerService;

    /**
     * Renders the "all-managers" view, displaying a paginated list of managers.
     * <p>
     * This method supports optional filtering of managers by name using the `keyword` parameter. If no keyword
     * is provided, all managers are retrieved. The retrieved managers are then added to the model along with
     * pagination information for rendering in the view.
     * <p>
     * Additionally, it handles potential errors during data retrieval and sets a flag in the model to indicate
     * if an error occurred.
     *
     * @param model    the Spring MVC Model object for storing data to be passed to the view
     * @param keyword  an optional keyword for filtering managers by name (can be null or blank)
     * @param pageable the pagination information, including page number, page size, and sorting
     * @return the logical name of the view template ("managers/all-managers")
     */
    @GetMapping
    @PreAuthorize("hasAuthority('MANAGERS_READ')")
    public String getPageWithManagers(Model model, @RequestParam(name = "keyword", required = false) String keyword,
                                      @PageableDefault Pageable pageable) {
        Page<ManagerDto> managersPage;

        try {
            managersPage = StringUtils.isBlank(keyword)
                ? managerService.getManagersPage(pageable)
                : managerService.getManagerInPageByName(keyword, pageable);
        } catch (InvalidFullNameFormatException e) {
            throw new CustomHttpException(e.getHttpStatus(), "Invalid full name format");
        }

        model.addAttribute(ModelAttributeNames.MANAGERS_ALL_NAMES_ATTRIBUTE, managerService.getAllNamesOfManagers())
            .addAttribute(ModelAttributeNames.MANAGERS_ATTRIBUTE, managersPage.getContent())
            .addAttribute(ModelAttributeNames.PAGE_ATTRIBUTE, pageable.getPageNumber())
            .addAttribute(ModelAttributeNames.TOTAL_ITEMS_ATTRIBUTE, managersPage.getTotalElements())
            .addAttribute(ModelAttributeNames.TOTAL_PAGES_ATTRIBUTE, managersPage.getTotalPages())
            .addAttribute(ModelAttributeNames.SIZE_ATTRIBUTE, pageable.getPageSize())
            .addAttribute(ModelAttributeNames.KEYWORD_ATTRIBUTE, keyword);

        return ViewNames.ALL_MANAGERS_PAGE;
    }

}
