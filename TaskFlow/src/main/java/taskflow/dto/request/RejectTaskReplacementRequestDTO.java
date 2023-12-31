package taskflow.dto.request;

import lombok.Data;

@Data
public class RejectTaskReplacementRequestDTO {
    private Long taskReplacementId;
    private Long userId;
}
