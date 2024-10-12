package faizal.project.todo_list.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityRequest {

    @NotNull(message = "user_id is required")
    private Long user_id;

    @NotBlank(message = "activity name is required")
    @Size(min = 4, max = 20, message = "Activity name must be between 4 and 20 characters")
    private String name;

    @Size(min = 4, max = 200, message = "Description must be between 4 and 200 characters")
    private String description;

    @NotBlank(message = "status is required")
    private String status;

    @NotNull(message = "Start date must not be null")
    @FutureOrPresent(message = "Start date must be future or present")
    private LocalDateTime start_date;

    @NotNull(message = "End date must not be null")
    @FutureOrPresent(message = "End date must be future or present")
    private LocalDateTime end_date;
}