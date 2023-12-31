package taskflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import taskflow.dto.request.TaskReplacementRequestDTO;
import taskflow.dto.response.TaskReplacementResponseDTO;
import taskflow.entities.Task;
import taskflow.entities.TaskReplacement;
import taskflow.entities.User;
import taskflow.entities.enums.TaskAction;
import taskflow.entities.enums.TaskReplacementStatus;
import taskflow.repository.TaskReplacementRepository;
import taskflow.repository.TaskRepository;
import taskflow.repository.UserRepository;
import taskflow.service.TaskReplacementService;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class TaskReplacementServiceImpl implements TaskReplacementService {
    private final ModelMapper modelMapper;
    private final TaskReplacementRepository taskReplacementRepository;
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;


    public TaskReplacementServiceImpl(ModelMapper modelMapper, TaskReplacementRepository taskReplacementRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.taskReplacementRepository = taskReplacementRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deleteTaskReplacementById(Long id) {
        taskReplacementRepository.deleteById(id);
    }

    @Override
    public TaskReplacementResponseDTO createDeleteTaskReplacement(TaskReplacementRequestDTO requestDTO) {
        try {
            Long taskId = requestDTO.getTaskId();
            Long userId = requestDTO.getUserId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            // Check if the user has enough tokens
            if (user.getToken() <= 0) {
                throw new IllegalArgumentException("User does not have enough tokens");
            }

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

            // Check if the provided userId matches the assignedToId of the task
            Long assignedToId = task.getAssignedTo() != null ? task.getAssignedTo().getId() : null;
            if (!Objects.equals(userId, assignedToId)) {
                throw new IllegalArgumentException("Invalid userId provided for Task with id: " + taskId);
            }

            // Check if the user has already performed a DELETE action this month
            boolean hasPerformedDeleteAction = taskReplacementRepository.existsByOldUserAndActionAndStatusAndDateTimeAfter(
                    user,
                    TaskAction.DELETE,
                    TaskReplacementStatus.APPROVED,
                    LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
            );

            if (hasPerformedDeleteAction) {
                // If the user has already performed a DELETE action this month
                return new TaskReplacementResponseDTO("error", "User can only perform one DELETE action per month");
            }

            // Check if the task has already been approved for DELETE by any user
            boolean hasApprovedDeleteAction = taskReplacementRepository.existsByTaskAndActionAndStatus(
                    task,
                    TaskAction.DELETE,
                    TaskReplacementStatus.APPROVED
            );

            if (hasApprovedDeleteAction) {
                // If the task has already been approved for DELETE, return an error response
                return new TaskReplacementResponseDTO("error", "Task has already been approved for deletion by another user");
            }

            // Store the assigned user before setting assignedTo to NULL
            User assignedToUser = task.getAssignedTo();

            // Set the assignedTo to NULL
            task.setAssignedTo(null);
            taskRepository.save(task);

            // Decrement user's token count by 1
            user.setToken(user.getToken() - 1);
            userRepository.save(user);

            // Create TaskReplacement with DELETE action
            TaskReplacement taskReplacement = new TaskReplacement();
            taskReplacement.setTask(task);
            taskReplacement.setDateTime(LocalDateTime.now());
            taskReplacement.setOldUser(assignedToUser); // Set OldUser as the user to whom the task was assigned before the change
            taskReplacement.setNewUser(null); // Set to NULL for DELETE action
            taskReplacement.setAction(TaskAction.DELETE);
            taskReplacement.setStatus(TaskReplacementStatus.APPROVED);

            taskReplacement = taskReplacementRepository.save(taskReplacement);

            // Returning success message along with created task replacement details
            TaskReplacementResponseDTO responseDTO = modelMapper.map(taskReplacement, TaskReplacementResponseDTO.class);
            responseDTO.setStatus("success");
            responseDTO.setMessage("TaskReplacement created successfully");
            return responseDTO;
        } catch (EntityNotFoundException e) {
            return new TaskReplacementResponseDTO("error", "Task not found with id: " + requestDTO.getTaskId());
        } catch (IllegalArgumentException e) {
            return new TaskReplacementResponseDTO("error", e.getMessage());
        } catch (Exception e) {
            return new TaskReplacementResponseDTO("error", "Error creating task replacement: " + e.getMessage());
        }
    }

    @Override
    public TaskReplacementResponseDTO createEditTaskReplacement(TaskReplacementRequestDTO requestDTO) {
        try {
            Long taskId = requestDTO.getTaskId();
            Long userId = requestDTO.getUserId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            // Check if the user has enough tokens
            if (user.getToken() <= 0) {
                throw new IllegalArgumentException("User does not have enough tokens");
            }

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

            // Check if the provided userId matches the assignedToId of the task
            Long assignedToId = task.getAssignedTo() != null ? task.getAssignedTo().getId() : null;
            if (!Objects.equals(userId, assignedToId)) {
                throw new IllegalArgumentException("Invalid userId provided for Task with id: " + taskId);
            }

            // Check if the user has an approved TaskReplacement for this task
            boolean hasApprovedTaskReplacement = taskReplacementRepository.existsByTaskAndStatus(task, TaskReplacementStatus.APPROVED);

            if (hasApprovedTaskReplacement) {
                // If there is an approved TaskReplacement, show an error
                return new TaskReplacementResponseDTO("error", "This task has already been edited or deleted once");
            }

            // Create TaskReplacement with EDIT action
            TaskReplacement taskReplacement = new TaskReplacement();
            taskReplacement.setTask(task);
            taskReplacement.setDateTime(LocalDateTime.now());

            // Set OldUser as the user to whom the task is currently assigned
            taskReplacement.setOldUser(task.getAssignedTo());
            taskReplacement.setNewUser(null); // Set to NULL for EDIT action
            taskReplacement.setAction(TaskAction.EDIT);
            taskReplacement.setStatus(TaskReplacementStatus.OPEN);

            taskReplacement = taskReplacementRepository.save(taskReplacement);

            // Returning success message along with created task replacement details
            TaskReplacementResponseDTO responseDTO = modelMapper.map(taskReplacement, TaskReplacementResponseDTO.class);
            responseDTO.setStatus("success");
            responseDTO.setMessage("TaskReplacement created successfully");
            return responseDTO;
        } catch (EntityNotFoundException e) {
            return new TaskReplacementResponseDTO("error", "Task not found with id: " + requestDTO.getTaskId());
        } catch (IllegalArgumentException e) {
            return new TaskReplacementResponseDTO("error", e.getMessage());
        } catch (Exception e) {
            return new TaskReplacementResponseDTO("error", "Error creating task replacement: " + e.getMessage());
        }
    }



}
