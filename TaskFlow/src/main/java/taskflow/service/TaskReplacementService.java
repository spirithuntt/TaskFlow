package taskflow.service;

import taskflow.dto.request.ApproveTaskReplacementRequestDTO;
import taskflow.dto.request.RejectTaskReplacementRequestDTO;
import taskflow.dto.request.TaskReplacementRequestDTO;
import taskflow.dto.response.TaskReplacementResponseDTO;

public interface TaskReplacementService {
    void deleteTaskReplacementById(Long id);
    TaskReplacementResponseDTO createDeleteTaskReplacement(TaskReplacementRequestDTO requestDTO);
    TaskReplacementResponseDTO createEditTaskReplacement(TaskReplacementRequestDTO requestDTO);
    TaskReplacementResponseDTO approveTaskReplacement(ApproveTaskReplacementRequestDTO requestDTO);
    TaskReplacementResponseDTO rejectTaskReplacement(RejectTaskReplacementRequestDTO requestDTO);
}
