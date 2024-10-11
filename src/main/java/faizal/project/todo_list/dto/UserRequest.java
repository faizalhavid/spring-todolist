package faizal.project.todo_list.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull(message = "username is required")
    @Size(min = 6, max = 20, message = "username must be between 6 and 20 characters")
    private String username;

    @NotNull(message = "password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d).+$", message = "password must contain at least one letter and one number")
    private String password;

    @NotNull(message = "email is required")
    @Email(message = "email is not valid")
    private String email;

}
