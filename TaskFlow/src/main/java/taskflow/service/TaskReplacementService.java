package taskflow.service;

import taskflow.dto.request.TaskReplacementRequestDTO;
import taskflow.dto.response.TaskReplacementResponseDTO;

public interface TaskReplacementService {
    void deleteTaskReplacementById(Long id);
    TaskReplacementResponseDTO createDeleteTaskReplacement(TaskReplacementRequestDTO requestDTO);

}
