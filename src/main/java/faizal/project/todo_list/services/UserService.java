package faizal.project.todo_list.services;


import faizal.project.todo_list.dto.UserRequest;
import faizal.project.todo_list.exception.ResourceNotFoundException;
import faizal.project.todo_list.model.User;
import org.springframework.stereotype.Service;
import faizal.project.todo_list.repository.UserRepository;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ActivityService activityService;

    public UserService(UserRepository userRepository, ActivityService activityService) {
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        return userRepository.save(user);
    }

    public User updateUser(UserRequest userRequest, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }
}

