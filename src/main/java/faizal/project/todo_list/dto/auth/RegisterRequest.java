package faizal.project.todo_list.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull(message = "username is required")
    private String username;

    @NotNull(message = "password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "password must contain at least one lowercase letter, one uppercase letter, one number, and one special character")
    private String password;

    @NotNull(message = "email is required")
    @Email(message = "email is not valid")
    private String email;

}
