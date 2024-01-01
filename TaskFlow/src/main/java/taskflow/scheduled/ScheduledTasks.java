package taskflow.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taskflow.entities.TaskReplacement;
import taskflow.entities.User;
import taskflow.entities.Task;
import taskflow.entities.enums.TaskReplacementStatus;
import taskflow.entities.enums.TaskStatus;
import taskflow.repository.TaskReplacementRepository;
import taskflow.repository.UserRepository;
import taskflow.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskReplacementRepository taskReplacementRepository;

    public ScheduledTasks(UserRepository userRepository, TaskRepository taskRepository, TaskReplacementRepository taskReplacementRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskReplacementRepository = taskReplacementRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run every midnight
    public void updateTokensAndExpireTasks() {
        // Update tokens for all users
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setToken(2);
        }
        userRepository.saveAll(users);

        // Expire the tasks that has passed the deadline
        LocalDate currentDate = LocalDate.now();
        List<Task> expiredTasks = taskRepository.findTasksToExpire(currentDate);
        for (Task task : expiredTasks) {
            task.setStatus(TaskStatus.EXPIRED);
        }
        taskRepository.saveAll(expiredTasks);
    }

    @Scheduled(cron = "0 0 0,12 * * *") // Run every 12 hours
    public void checkAndCloseTaskReplacements() {
        // Find TaskReplacements with status OPEN and DateTime older than 12 hours
        List<TaskReplacement> openTaskReplacements = taskReplacementRepository.findByStatusAndDateTimeBefore(
                TaskReplacementStatus.OPEN, LocalDateTime.now().minusHours(12));

        for (TaskReplacement taskReplacement : openTaskReplacements) {
            // Change status to CLOSED
            taskReplacement.setStatus(TaskReplacementStatus.CLOSED);
            taskReplacementRepository.save(taskReplacement);

            // Give the user 2 tokens as bonus
            User oldUser = taskReplacement.getOldUser();
            if (oldUser != null) {
                oldUser.setToken(oldUser.getToken() + 2);
                userRepository.save(oldUser);
            }
        }
    }
}
