package faizal.project.todo_list.services;

import faizal.project.todo_list.dto.ActivityRequest;
import faizal.project.todo_list.exception.ResourceNotFoundException;
import faizal.project.todo_list.model.Activity;
import faizal.project.todo_list.model.User;
import faizal.project.todo_list.repository.ActivityRepository;
import faizal.project.todo_list.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public Activity createActivity(ActivityRequest activityRequest) {
        Activity activity = new Activity();
        activity.setName(activityRequest.getName());

        User user = userRepository.findById(activityRequest.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        activity.setUser(user);
        activity.setDescription(activityRequest.getDescription());
        activity.setStatus(activityRequest.getStatus());
        activity.setStart_date(activityRequest.getStart_date());
        activity.setEnd_date(activityRequest.getEnd_date());

        return activityRepository.save(activity);
    }

    public Activity updateActivity(ActivityRequest activityRequest, Long id) {
        return activityRepository.findById(id).map(activity -> {
            activity.setUser(activityRequest.getUser());
            activity.setName(activityRequest.getName());
            activity.setDescription(activityRequest.getDescription());
            activity.setStart_date(activityRequest.getStart_date());
            activity.setEnd_date(activityRequest.getEnd_date());
            activity.setStatus(activityRequest.getStatus());
            return activityRepository.save(activity);
        }).orElseThrow(() -> new ResourceNotFoundException("Activity not found with id " + id));
    }
}
