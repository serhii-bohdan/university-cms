package ua.foxminded.universitycms.dto;

/**
 * The {@code UserDto} class is an abstract data transfer object (DTO) for user
 * entities.
 * <p>
 * This class includes fields for user ID, first name, last name, email,
 * password, and active status. It also includes getter and setter methods for
 * these fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the user DTO.
 *
 * @author Serhii Bohdan
 */
public abstract class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isActive;

    /**
     * Constructs a new {@code UserDto} with the specified first name, last name,
     * email, password, and active status.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     * @param password  the password of the user
     * @param isActive  the active status of the user
     */
    protected UserDto(String firstName, String lastName, String email, String password, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    /**
     * Constructs a new {@code UserDto} with no initial values.
     */
    protected UserDto() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Returns a string representation of the user DTO.
     *
     * @return a string representation of the user DTO
     */
    @Override
    public String toString() {
        return "UserDto [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", password=" + password + ", isActive=" + isActive + "]";
    }

}
