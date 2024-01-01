package taskflow.dto.response;

import lombok.Data;
import taskflow.entities.Tags;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;

import java.time.LocalDate;
import java.util.Set;

@Data
public class TaskOverviewResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate creationDate;
    private LocalDate startDate;
    private LocalDate deadline;
    private TaskStatus status;
    private User createdBy;
    private User assignedTo;
    private Set<Tags> tags;
}
