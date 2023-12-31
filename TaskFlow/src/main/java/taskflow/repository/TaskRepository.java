package taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import taskflow.entities.Task;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedTo(User user);
    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.deadline < :currentDate AND t.status <> 'EXPIRED'")
    List<Task> findTasksToExpire(LocalDate currentDate);

}
