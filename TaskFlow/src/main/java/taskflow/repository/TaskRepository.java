package taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskflow.entities.Task;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedTo(User user);
    List<Task> findByStatus(TaskStatus status);

}
