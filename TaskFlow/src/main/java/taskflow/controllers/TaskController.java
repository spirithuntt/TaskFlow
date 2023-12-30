package taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.request.TaskRequestDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        TaskResponseDTO task = taskService.getTask(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> taskList = taskService.getAllTasks();
        return ResponseEntity.ok(taskList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO updatedTask = taskService.updateTask(id, taskRequestDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping("/byUser/{userId}")
//    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser(@PathVariable Long userId) {
////         Assume you have a User entity and userId can be used to retrieve the user from the database
//         User user = userService.getUserById(userId);
//         List<TaskResponseDTO> tasks = taskService.getTasksByUser(user);
//
//        // For the sake of simplicity, returning an empty list
//        return ResponseEntity.ok(List.of());
//    }
//
//    @GetMapping("/byStatus/{status}")
//    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@PathVariable String status) {
//        // Assuming TaskStatus is an enum and can be parsed from the string
//         TaskStatus taskStatus = TaskStatus.valueOf(status);
//         List<TaskResponseDTO> tasks = taskService.getTasksByStatus(taskStatus);
//
//        // For the sake of simplicity, returning an empty list
//        return ResponseEntity.ok(List.of());
//    }
}
