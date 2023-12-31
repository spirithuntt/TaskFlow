package taskflow.dto.request;

import lombok.Data;

@Data
public class TaskStatusUpdateRequestDTO {
    private Long taskId;
    private Long userId;
}
