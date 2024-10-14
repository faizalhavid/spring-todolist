package faizal.project.todo_list.controller;


import faizal.project.todo_list.dto.auth.LoginRequest;
import faizal.project.todo_list.dto.auth.LoginResponse;
import faizal.project.todo_list.dto.auth.RegisterRequest;
import faizal.project.todo_list.dto.auth.RegisterResponse;
import faizal.project.todo_list.dto.UserResponse;
import faizal.project.todo_list.helpers.JWTHelper;
import faizal.project.todo_list.model.User;
import faizal.project.todo_list.services.AuthService;
import faizal.project.todo_list.services.UserService;
import faizal.project.todo_list.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }
        User user = authService.login(loginRequest);
        String token = JWTHelper.generateToken(loginRequest.getEmail() , user.getId());
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(1200);
        authService.login(loginRequest);
        LoginResponse loginResponse = new LoginResponse(token, expiresAt);
        return ResponseEntity.ok(new ApiResponse<>("success", "User logged in successfully", loginResponse));
    }

@PostMapping("/register")
public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return ResponseEntity.badRequest().body(new ApiResponse<>("error", bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
    }
    RegisterResponse registerResponse = authService.register(registerRequest);
    return ResponseEntity.ok(new ApiResponse<>("success", "User registered successfully", registerResponse));
}
}
