package taskflow.dto.request;

import lombok.Data;

@Data
public class TaskAssignmentRequestDTO {
    private Long taskId;
    private Long userId;
}
