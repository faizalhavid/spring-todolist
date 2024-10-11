package faizal.project.todo_list.controller;


import faizal.project.todo_list.dto.ActivityRequest;
import faizal.project.todo_list.model.Activity;
import faizal.project.todo_list.services.ActivityService;
import faizal.project.todo_list.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import faizal.project.todo_list.repository.ActivityRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityRepository activityRepository;
    private final ActivityService activityService;

    public ActivityController(ActivityRepository activityRepository, ActivityService activityService) {
        this.activityRepository = activityRepository;
        this.activityService = activityService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<Activity>>> getAllActivity() {
        List<Activity> activities = activityRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "Activities retrieved successfully", activities));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Activity>> createActivity(@Valid @RequestBody ActivityRequest activityRequest, BindingResult bindingResult) {
        if (activityRequest.getUser() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "user_id is required", null));
        }

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", errorMessages, null));
        }
        Activity createdActivity = activityService.createActivity(activityRequest);
        return ResponseEntity.ok(new ApiResponse<>("success", "Activity created successfully", createdActivity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Activity>> getActivityById(@PathVariable Long id) {
        return activityRepository.findById(id)
                .map(activity -> ResponseEntity.ok(new ApiResponse<>("success", "Activity retrieved successfully", activity)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>("error", "Activity not found", null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Activity>> updateActivity(@PathVariable Long id, @Valid @RequestBody ActivityRequest activityRequest, BindingResult bindingResult){
      if(bindingResult.hasErrors()){
          String errorMessages = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
          return  ResponseEntity.badRequest().body(new ApiResponse<>("error", errorMessages,null));
      }
      Activity updatedActivity = activityService.updateActivity(activityRequest,id);
      return  ResponseEntity.ok(new ApiResponse<>("success", "Activity with id ".concat(id.toString()).concat("updated successfully") , updatedActivity));
    }


@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse<Object>> deleteActivity(@PathVariable Long id) {
    return activityRepository.findById(id)
            .map(activity -> {
                activityRepository.delete(activity);
                return ResponseEntity.ok(new ApiResponse<>("success", "Activity deleted successfully", null));
            })
            .orElse(ResponseEntity.status(404).body(new ApiResponse<>("error", "Activity not found", null)));
}

}
