package faizal.project.todo_list.controller;


import faizal.project.todo_list.dto.UserRequest;
import faizal.project.todo_list.model.User;
import faizal.project.todo_list.repository.UserRepository;
import faizal.project.todo_list.services.UserService;
import faizal.project.todo_list.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "Users retrieved successfully", users));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }
        User createdUser = userService.createUser(userRequest);
        return ResponseEntity.ok(new ApiResponse<>("success", "User created successfully", createdUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(new ApiResponse<>("success", "User retrieved successfully", user)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>("error", "User not found", null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }
        User updatedUser = userService.updateUser(userRequest, id);
        return ResponseEntity.ok(new ApiResponse<>("success", "User updated successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id){
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok(new ApiResponse<>("success", "User deleted successfully", null));
                })
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>("error", "User not found", null)));
    }
}
