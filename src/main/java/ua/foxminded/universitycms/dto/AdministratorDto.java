package ua.foxminded.universitycms.dto;

/**
 * The {@code AdministratorDto} class is a data transfer object (DTO) for
 * administrator entities.
 * <p>
 * This class extends {@link UserDto} and inherits all its fields and methods.
 * It represents a specific type of user, namely an administrator.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the administrator DTO.
 *
 * @author Serhii Bohdan
 */
public class AdministratorDto extends UserDto {

    /**
     * Constructs a new {@code AdministratorDto} with the specified first name, last
     * name, email, password, and active status.
     *
     * @param firstName the first name of the administrator
     * @param lastName  the last name of the administrator
     * @param email     the email of the administrator
     * @param password  the password of the administrator
     * @param isActive  the active status of the administrator
     */
    public AdministratorDto(String firstName, String lastName, String email, String password, Boolean isActive) {
        super(firstName, lastName, email, password, isActive);
    }

    /**
     * Constructs a new {@code AdministratorDto} with no initial values.
     */
    public AdministratorDto() {
        super();
    }

    /**
     * Returns a string representation of the administrator DTO.
     *
     * @return a string representation of the administrator DTO
     */
    @Override
    public String toString() {
        return "AdministratorDto [userId=" + super.getUserId() + ", firstName=" + super.getFirstName() + ", lastName="
                + super.getLastName() + ", email=" + super.getEmail() + ", password=" + super.getPassword()
                + ", isActive=" + super.getIsActive() + "]";
    }

}
