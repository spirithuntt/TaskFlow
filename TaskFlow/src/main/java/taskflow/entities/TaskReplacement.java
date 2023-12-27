package taskflow.entities;

import jakarta.persistence.*;
import taskflow.entities.enums.TaskAction;
import taskflow.entities.enums.TaskReplacementStatus;

import java.time.LocalDateTime;

@Entity
public class TaskReplacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "old_user_id")
    private User oldUser;

    @ManyToOne
    @JoinColumn(name = "new_user_id")
    private User newUser;

    @Enumerated(EnumType.STRING)
    private TaskAction action;

    @Enumerated(EnumType.STRING)
    private TaskReplacementStatus status;

}
