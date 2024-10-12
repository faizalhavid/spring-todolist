package faizal.project.todo_list.controller;


import faizal.project.todo_list.dto.UserRequest;
import faizal.project.todo_list.dto.UserResponse;
import faizal.project.todo_list.model.User;
import faizal.project.todo_list.repository.UserRepository;
import faizal.project.todo_list.services.UserService;
import faizal.project.todo_list.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>("success", "Users retrieved successfully", users));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", errorMessages, null));
        }
        User createdUser = userService.createUser(userRequest);
        return ResponseEntity.ok(new ApiResponse<>("success", "User created successfully", createdUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "User retrieved successfully", user));
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
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "User deleted successfully", null));
    }
}
