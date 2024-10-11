package faizal.project.todo_list.repository;

import faizal.project.todo_list.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
