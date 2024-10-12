package faizal.project.todo_list.dto;

import faizal.project.todo_list.model.Activity;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    @NotNull(message = "username is required")
    @Size(min = 6, max = 20, message = "username must be between 6 and 20 characters")
    private String username;

    @NotNull(message = "password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d).+$", message = "password must contain at least one letter and one number")
    private String password;

    @Email(message = "email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    private List<ActivityRequest> activities;

}
