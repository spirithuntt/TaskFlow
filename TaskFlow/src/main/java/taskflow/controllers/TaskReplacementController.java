package taskflow.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.request.ApproveTaskReplacementRequestDTO;
import taskflow.dto.request.RejectTaskReplacementRequestDTO;
import taskflow.dto.request.TaskReplacementRequestDTO;
import taskflow.dto.response.TaskReplacementResponseDTO;
import taskflow.service.TaskReplacementService;

@RestController
@RequestMapping("/api/v1/task-replacements")
public class TaskReplacementController {
    private final TaskReplacementService taskReplacementService;

    public TaskReplacementController(TaskReplacementService taskReplacementService) {
        this.taskReplacementService = taskReplacementService;
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

    @PostMapping("/status/approve")
    public ResponseEntity<TaskReplacementResponseDTO> approveTaskReplacement(
            @RequestBody ApproveTaskReplacementRequestDTO requestDTO) {
        TaskReplacementResponseDTO responseDTO = taskReplacementService.approveTaskReplacement(requestDTO);
        HttpStatus status = responseDTO.getStatus().equals("success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(responseDTO, status);
    }

    @PostMapping("/status/reject")
    public ResponseEntity<TaskReplacementResponseDTO> rejectTaskReplacement(
            @RequestBody RejectTaskReplacementRequestDTO requestDTO) {
        TaskReplacementResponseDTO responseDTO = taskReplacementService.rejectTaskReplacement(requestDTO);
        HttpStatus status = responseDTO.getStatus().equals("success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(responseDTO, status);
    }

}
