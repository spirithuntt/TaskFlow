package taskflow.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taskflow.entities.User;
import taskflow.repository.UserRepository;

import java.util.List;

@Component
public class ScheduledTasks {

    private final UserRepository userRepository;

    public ScheduledTasks(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run every midnight
    public void updateTokens() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            user.setToken(2);
        }

        userRepository.saveAll(users);
    }
}
