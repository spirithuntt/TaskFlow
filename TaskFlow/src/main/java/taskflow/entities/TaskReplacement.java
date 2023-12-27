package taskflow.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import taskflow.entities.enums.TaskAction;
import taskflow.entities.enums.TaskReplacementStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TaskReplacement that = (TaskReplacement) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
