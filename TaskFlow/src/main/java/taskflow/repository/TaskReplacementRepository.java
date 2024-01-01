package taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import taskflow.entities.Task;
import taskflow.entities.TaskReplacement;
import taskflow.entities.User;
import taskflow.entities.enums.TaskAction;
import taskflow.entities.enums.TaskReplacementStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskReplacementRepository extends JpaRepository<TaskReplacement, Long> {

    boolean existsByTaskAndActionAndStatus(Task task, TaskAction action, TaskReplacementStatus status);

    boolean existsByTaskAndStatus(Task task, TaskReplacementStatus status);

    @Query("SELECT CASE WHEN COUNT(tr) > 0 THEN true ELSE false END " +
            "FROM TaskReplacement tr " +
            "WHERE tr.oldUser = :user " +
            "AND tr.action = :action " +
            "AND tr.status = :status " +
            "AND tr.dateTime > :dateTime")
    boolean existsByOldUserAndActionAndStatusAndDateTimeAfter(
            @Param("user") User user,
            @Param("action") TaskAction action,
            @Param("status") TaskReplacementStatus status,
            @Param("dateTime") LocalDateTime dateTime
    );

    List<TaskReplacement> findByStatusAndDateTimeBefore(TaskReplacementStatus status, LocalDateTime dateTime);

}
