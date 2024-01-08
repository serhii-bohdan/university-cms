package ua.foxminded.universitycms.dto;

import java.util.Set;

/**
 * The {@code GroupDto} class is a data transfer object (DTO) for group
 * entities.
 * <p>
 * This class includes fields for group ID, group name, and a set of students.
 * It also includes getter and setter methods for these fields.
 * <p>
 * The {@code toString()} method is overridden to return a string representation
 * of the group DTO.
 *
 * @author Serhii Bohdan
 */
public class GroupDto {

    private Long groupId;
    private String groupName;
    private Set<StudentDto> students;

    /**
     * Constructs a new {@code GroupDto} with the specified group name.
     *
     * @param groupName the name of the group
     */
    public GroupDto(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Constructs a new {@code GroupDto} with no initial values.
     */
    public GroupDto() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<StudentDto> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentDto> students) {
        this.students = students;
    }

    /**
     * Returns a string representation of the group DTO.
     *
     * @return a string representation of the group DTO
     */
    @Override
    public String toString() {
        return "GroupDto [groupId=" + groupId + ", groupName=" + groupName + ", students=" + students + "]";
    }

}
