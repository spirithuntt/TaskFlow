package taskflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskflow.entities.enums.TaskAction;
import taskflow.entities.enums.TaskReplacementStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskReplacementResponseDTO {
    private Long id;
    private Long taskId;
    private LocalDateTime dateTime;
    private Long oldUserId;
    private Long newUserId;
    private TaskAction action;
    private TaskReplacementStatus taskReplacementStatus;
    private String status;
    private String message;

    public TaskReplacementResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
