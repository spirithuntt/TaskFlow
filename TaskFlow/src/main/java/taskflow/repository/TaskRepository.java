package taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import taskflow.entities.Task;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedTo(User user);
    List<Task> findByStatus(TaskStatus status);

    //! find if date is passed
    @Query(value = "SELECT * FROM task WHERE deadline < :currentDate AND status <> 'EXPIRED'", nativeQuery = true)
    List<Task> findTasksToExpire(@Param("currentDate") LocalDate currentDate);


}
