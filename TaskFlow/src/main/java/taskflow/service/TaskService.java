package taskflow.service;

import taskflow.dto.request.*;
import taskflow.dto.response.TaskOverviewResponseDTO;
import taskflow.dto.response.TaskResponseDTO;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO assignTaskToUser(Long id, TaskUpdateRequestDTO taskUpdateRequestDTO);

    TaskResponseDTO assignTaskToSelf(TaskAssignmentRequestDTO assignmentDTO);

    TaskResponseDTO updateTaskStatusToDone(TaskStatusUpdateRequestDTO requestDTO);

    TaskResponseDTO deleteTaskCreatedByMe(TaskDeletionRequestDTO deletionRequestDTO);

    List<TaskOverviewResponseDTO> getAllTasks();



}
