package taskflow.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks_to_tags")
public class TasksToTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tags tag;


}
