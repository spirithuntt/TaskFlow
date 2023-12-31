package taskflow.dto.request;

import lombok.Data;

@Data
public class TaskDeletionRequestDTO {
    private Long taskId;
    private Long userId;
}
