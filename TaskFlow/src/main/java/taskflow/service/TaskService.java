package taskflow.service;

import taskflow.dto.request.TaskAssignmentRequestDTO;
import taskflow.dto.request.TaskRequestDTO;
import taskflow.dto.request.TaskUpdateRequestDTO;
import taskflow.dto.response.TaskResponseDTO;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO assignTaskToUser(Long id, TaskUpdateRequestDTO taskUpdateRequestDTO);

    TaskResponseDTO assignTaskToSelf(TaskAssignmentRequestDTO assignmentDTO);

    TaskResponseDTO getTask(Long id);

    List<TaskResponseDTO> getAllTasks();

    TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO);

    void deleteTask(Long id);


}
