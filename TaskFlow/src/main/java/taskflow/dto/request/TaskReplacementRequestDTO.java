package taskflow.dto.request;

import lombok.Data;

@Data
public class TaskReplacementRequestDTO {
    private Long taskId;
    private Long userId;
}
