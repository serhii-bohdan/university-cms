package ua.foxminded.universitycms.service;

import ua.foxminded.universitycms.dto.ManagerDto;
import ua.foxminded.universitycms.model.Manager;

/**
 * The {@code ManagerService} interface extends the {@link Service} interface and defines methods for managing {@link Manager} entities.
 * It provides functionality for creating, retrieving, updating, and deleting managers, likely using DTOs (Data Transfer Objects)
 * for data transfer between the service layer and other application layers.
 *
 * @author Serhii Bohdan
 */
public interface ManagerService extends Service<Manager, ManagerDto> {
}
