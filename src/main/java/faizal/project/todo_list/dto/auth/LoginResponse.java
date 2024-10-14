package faizal.project.todo_list.dto.auth;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponse {
    private LocalDateTime expiresAt;
    private String token;

    public LoginResponse(String token, LocalDateTime expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }
}
