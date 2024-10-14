package faizal.project.todo_list.controller;

import faizal.project.todo_list.dto.ActivityRequest;
import faizal.project.todo_list.dto.ActivityResponse;
import faizal.project.todo_list.helpers.JWTHelper;
import faizal.project.todo_list.services.ActivityService;
import faizal.project.todo_list.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;


    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityResponse>>> getAllActivities() {
        List<ActivityResponse> activities = activityService.getAllActivities();
        return ResponseEntity.ok(new ApiResponse<>("success", "Activities retrieved successfully", activities));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ActivityResponse>> createActivity(@Valid @RequestBody ActivityRequest activityRequest, BindingResult bindingResult, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = JWTHelper.extractUserId(token);
        activityRequest.setUserId(userId);
        ActivityResponse createdActivity = activityService.createActivity(activityRequest);
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", errorMessages, null));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", "Activity created successfully", createdActivity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityResponse>> getActivityById(@PathVariable Long id) {
        ActivityResponse activity = activityService.getActivityById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Activity retrieved successfully", activity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityResponse>> updateActivity(@PathVariable Long id, @Valid @RequestBody ActivityRequest activityRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", errorMessages, null));
        }

        ActivityResponse updatedActivity = activityService.updateActivity(activityRequest, id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Activity updated successfully", updatedActivity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Activity deleted successfully", null));
    }
}