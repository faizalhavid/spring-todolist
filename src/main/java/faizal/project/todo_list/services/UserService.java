package faizal.project.todo_list.services;


import faizal.project.todo_list.dto.UserRequest;
import faizal.project.todo_list.dto.UserResponse;
import faizal.project.todo_list.exception.ResourceNotFoundException;
import faizal.project.todo_list.model.Activity;
import faizal.project.todo_list.model.User;
import org.springframework.stereotype.Service;
import faizal.project.todo_list.repository.UserRepository;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToUserResponse).collect(Collectors.toList());
    }


    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return convertToUserResponse(user);
    }

    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        //need to hash the password before saving
        user.setPassword(userRequest.getPassword());
        return userRepository.save(user);
    }

    public User updateUser(UserRequest userRequest, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());

        // Hash the password before saving
        user.setPassword(userRequest.getPassword());

        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(user);
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setActivities(
                user.getActivities().stream()
                        .map(Activity::getId)
                        .collect(Collectors.toList())
        );
        return userResponse;
    }
}

