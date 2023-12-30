package taskflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taskflow.dto.request.TaskRequestDTO;
import taskflow.dto.response.TaskResponseDTO;
import taskflow.entities.Task;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;
import taskflow.repository.TaskRepository;
import taskflow.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        try {
            Task task = modelMapper.map(taskRequestDTO, Task.class);
            task = taskRepository.save(task);
            return modelMapper.map(task, TaskResponseDTO.class);
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error creating task: " + e.getMessage());
        }
    }

    @Override
    public TaskResponseDTO getTask(Long id) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
            return modelMapper.map(task, TaskResponseDTO.class);
        } catch (EntityNotFoundException e) {
            return new TaskResponseDTO("error", "Task not found with id: " + id);
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error getting task: " + e.getMessage());
        }
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        try {
            List<Task> taskList = taskRepository.findAll();
            return taskList.stream()
                    .map(task -> modelMapper.map(task, TaskResponseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of(new TaskResponseDTO("error", "Error getting tasks: " + e.getMessage()));
        }
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        try {
            Task existingTask = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
            modelMapper.map(taskRequestDTO, existingTask);
            existingTask = taskRepository.save(existingTask);
            return modelMapper.map(existingTask, TaskResponseDTO.class);
        } catch (EntityNotFoundException e) {
            return new TaskResponseDTO("error", "Task not found with id: " + id);
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error updating task: " + e.getMessage());
        }
    }

    @Override
    public void deleteTask(Long id) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
            taskRepository.delete(task);
        } catch (EntityNotFoundException e) {
            System.out.println("Task not found with id: " + id);
        } catch (Exception e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }

    @Override
    public List<TaskResponseDTO> getTasksByUser(User user) {
        try {
            List<Task> tasks = taskRepository.findByAssignedTo(user);
            return tasks.stream()
                    .map(task -> modelMapper.map(task, TaskResponseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of(new TaskResponseDTO("error", "Error getting tasks by user: " + e.getMessage()));
        }
    }

    @Override
    public List<TaskResponseDTO> getTasksByStatus(TaskStatus status) {
        try {
            List<Task> tasks = taskRepository.findByStatus(status);
            return tasks.stream()
                    .map(task -> modelMapper.map(task, TaskResponseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of(new TaskResponseDTO("error", "Error getting tasks by status: " + e.getMessage()));
        }
    }
}
