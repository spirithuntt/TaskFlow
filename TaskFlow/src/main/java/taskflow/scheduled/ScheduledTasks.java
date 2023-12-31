package taskflow.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taskflow.entities.User;
import taskflow.entities.Task;
import taskflow.entities.enums.TaskStatus;
import taskflow.repository.UserRepository;
import taskflow.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ScheduledTasks(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run every midnight
    public void updateTokensAndExpireTasks() {
        // Update tokens for all users
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setToken(2);
        }
        userRepository.saveAll(users);

        // Expire tasks with a deadline before today
        LocalDate currentDate = LocalDate.now();
        List<Task> expiredTasks = taskRepository.findTasksToExpire(currentDate);
        for (Task task : expiredTasks) {
            task.setStatus(TaskStatus.EXPIRED);
        }
        taskRepository.saveAll(expiredTasks);
    }

}
