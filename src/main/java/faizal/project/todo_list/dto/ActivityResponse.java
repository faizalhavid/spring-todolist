package faizal.project.todo_list.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityResponse {
    private Long id;
    private Long user_id;
    private String name;
    private String description;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private String status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}