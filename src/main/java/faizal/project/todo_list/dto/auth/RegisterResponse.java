package faizal.project.todo_list.dto.auth;

import lombok.Data;

@Data
public class RegisterResponse {
    private Long id;
    private String username;
    private String email;
}
