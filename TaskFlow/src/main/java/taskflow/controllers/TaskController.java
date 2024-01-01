package taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.request.*;
import taskflow.dto.response.TaskOverviewResponseDTO;
import taskflow.dto.response.TaskResponseDTO;
import taskflow.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO createdTask = taskService.createTask(taskRequestDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDTO> assignTaskToUser(@PathVariable Long id, @RequestBody TaskUpdateRequestDTO taskUpdateRequestDTO) {
        TaskResponseDTO responseDTO = taskService.assignTaskToUser(id, taskUpdateRequestDTO);
        if ("error".equals(responseDTO.getStatus())) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(responseDTO);
        }
    }

    @PutMapping("/assign")
    public ResponseEntity<TaskResponseDTO> assignTaskToUser(@RequestBody TaskAssignmentRequestDTO assignmentDTO) {
        TaskResponseDTO responseDTO = taskService.assignTaskToSelf(assignmentDTO);

        if ("error".equals(responseDTO.getStatus())) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(responseDTO);
        }
    }

    @PutMapping("/status/done")
    public ResponseEntity<TaskResponseDTO> updateTaskStatusToDone(@RequestBody TaskStatusUpdateRequestDTO requestDTO) {
        TaskResponseDTO responseDTO = taskService.updateTaskStatusToDone(requestDTO);
        if ("error".equals(responseDTO.getStatus())) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(responseDTO);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<TaskResponseDTO> deleteTaskCreatedByMe(@RequestBody TaskDeletionRequestDTO deletionRequestDTO) {
        TaskResponseDTO responseDTO = taskService.deleteTaskCreatedByMe(deletionRequestDTO);
        if ("error".equals(responseDTO.getStatus())) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(responseDTO);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/overview")
    public ResponseEntity<List<TaskOverviewResponseDTO>> getAllTasksOverview() {
        List<TaskOverviewResponseDTO> taskOverviewList = taskService.getAllTasks();
        return ResponseEntity.ok(taskOverviewList);
    }


}
