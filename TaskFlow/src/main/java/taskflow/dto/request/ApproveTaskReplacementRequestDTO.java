package taskflow.dto.request;

import lombok.Data;

@Data

public class ApproveTaskReplacementRequestDTO {
    private Long taskReplacementId;
    private Long userId;
    private Long newUserId;
}
