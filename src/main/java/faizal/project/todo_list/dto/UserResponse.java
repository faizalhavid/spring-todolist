package faizal.project.todo_list.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private List<Long> activities;
}