package taskflow.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.request.TaskReplacementRequestDTO;
import taskflow.dto.response.TaskReplacementResponseDTO;
import taskflow.service.TaskReplacementService;
import taskflow.service.TaskService;

@RestController
@RequestMapping("/api/v1/task-replacements")
public class TaskReplacementController {
    private final TaskReplacementService taskReplacementService;

    public TaskReplacementController(TaskReplacementService taskReplacementService) {
        this.taskReplacementService = taskReplacementService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskReplacementById(@PathVariable Long id) {
        taskReplacementService.deleteTaskReplacementById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<TaskReplacementResponseDTO> createDeleteTaskReplacement(@RequestBody TaskReplacementRequestDTO requestDTO) {
        TaskReplacementResponseDTO responseDTO = taskReplacementService.createDeleteTaskReplacement(requestDTO);
        if ("error".equals(responseDTO.getStatus())) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(responseDTO);
        }
    }

    @PostMapping("/edit-replacement")
    public ResponseEntity<TaskReplacementResponseDTO> createEditTaskReplacement(@RequestBody TaskReplacementRequestDTO requestDTO) {
        TaskReplacementResponseDTO responseDTO = taskReplacementService.createEditTaskReplacement(requestDTO);
        if ("error".equals(responseDTO.getStatus())) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(responseDTO);
        }
    }


}
