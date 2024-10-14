package faizal.project.todo_list.services;

import faizal.project.todo_list.dto.auth.LoginRequest;
import faizal.project.todo_list.dto.auth.LoginResponse;
import faizal.project.todo_list.dto.auth.RegisterRequest;
import faizal.project.todo_list.dto.auth.RegisterResponse;
import faizal.project.todo_list.model.User;
import faizal.project.todo_list.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.getEmail();
        String username = request.getUsername();

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Username is already taken");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(username, email, hashedPassword);
        userRepository.save(user);
        RegisterResponse response = new RegisterResponse();
        response.setUsername(username);
        response.setEmail(email);
        return response;
    }

    public User login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatch) {
            throw new IllegalStateException("Invalid credentials");
        }
        return user;
    }
}