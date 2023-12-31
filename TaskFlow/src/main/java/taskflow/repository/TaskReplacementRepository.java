package taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskflow.entities.TaskReplacement;

public interface TaskReplacementRepository extends JpaRepository<TaskReplacement, Long> {

}
