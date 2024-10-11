package faizal.project.todo_list.repository;

import faizal.project.todo_list.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
