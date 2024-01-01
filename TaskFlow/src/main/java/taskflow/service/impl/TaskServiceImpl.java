package taskflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import taskflow.dto.request.*;
import taskflow.dto.response.TaskOverviewResponseDTO;
import taskflow.dto.response.TaskResponseDTO;
import taskflow.entities.Tags;
import taskflow.entities.Task;
import taskflow.entities.User;
import taskflow.entities.enums.Role;
import taskflow.entities.enums.TaskStatus;
import taskflow.repository.TagsRepository;
import taskflow.repository.TaskRepository;
import taskflow.repository.UserRepository;
import taskflow.service.TaskService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TagsRepository tagRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TagsRepository tagRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        try {
            Long createdById = taskRequestDTO.getCreatedById();
            if (createdById != null) {
                User createdBy = userRepository.findById(createdById)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createdById));

                if (!createdBy.getRole().equals(Role.ADMIN)) {
                    throw new IllegalArgumentException("User with id " + createdById + " does not have the role ADMIN");
                }

                Task task = modelMapper.map(taskRequestDTO, Task.class);

                // Set the creation date to the current date
                task.setCreationDate(LocalDate.now());

                // Set the task status to NOT_STARTED
                task.setStatus(TaskStatus.NOT_STARTED);

                // Set the createdBy user
                task.setCreatedBy(createdBy);

                // Validate the start date (ensure it is at least 3 days from now)
                LocalDate startDate = task.getStartDate();
                if (startDate != null && startDate.isBefore(LocalDate.now().plusDays(3))) {
                    throw new IllegalArgumentException("Start date must be at least 3 days from now");
                }

                // Validate the deadline (ensure it is at least 3 days from the start date and not more than 14 days)
                LocalDate deadline = task.getDeadline();
                if (deadline != null && startDate != null) {
                    if (deadline.isBefore(startDate.plusDays(3)) || deadline.isAfter(startDate.plusDays(14))) {
                        throw new IllegalArgumentException("Deadline must be between 3 and 14 days from the start date");
                    }
                }

                // Associate tags with the task
                List<Long> tagIds = taskRequestDTO.getTagIds();
                if (tagIds != null && !tagIds.isEmpty()) {
                    List<Tags> tags = tagRepository.findAllById(tagIds);
                    task.setTags(new HashSet<>(tags));
                }

                task = taskRepository.save(task);

                // Returning success message along with task details
                TaskResponseDTO responseDTO = modelMapper.map(task, TaskResponseDTO.class);
                responseDTO.setStatus("success");
                responseDTO.setMessage("Task created successfully");
                return responseDTO;

            } else {
                throw new IllegalArgumentException("CreatedById cannot be null");
            }
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error creating task: " + e.getMessage());
        }
    }

    @Override
    public TaskResponseDTO assignTaskToUser(Long id, TaskUpdateRequestDTO taskUpdateRequestDTO) {
        try {
            Task existingTask = taskRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

            // Check if the provided createdById is the same as the one in the existing task
            Long providedCreatedById = taskUpdateRequestDTO.getCreatedById();
            if (providedCreatedById == null || !Objects.equals(existingTask.getCreatedBy().getId(), providedCreatedById)) {
                throw new IllegalArgumentException("Invalid createdById provided");
            }

            // Check if the assignment is at least 3 days before the start date
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = existingTask.getStartDate();
            if (startDate != null && !currentDate.plusDays(3).isBefore(startDate)) {
                throw new IllegalArgumentException("Task must be assigned at least 3 days before the start date");
            }

            // Check if the task is in the NOT_STARTED status
            if (existingTask.getStatus() == TaskStatus.NOT_STARTED) {
                // Assign the task to the specified user
                Long assignedToId = taskUpdateRequestDTO.getAssignedToId();
                if (assignedToId != null) {
                    User assignedTo = userRepository.findById(assignedToId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assignedToId));
                    existingTask.setAssignedTo(assignedTo);

                    // Set the task status to IN_PROGRESS
                    existingTask.setStatus(TaskStatus.IN_PROGRESS);

                    existingTask = taskRepository.save(existingTask);

                    // Returning success message along with task details
                    TaskResponseDTO responseDTO = modelMapper.map(existingTask, TaskResponseDTO.class);
                    responseDTO.setStatus("success");
                    responseDTO.setMessage("Task assigned successfully");
                    return responseDTO;
                } else {
                    throw new IllegalArgumentException("AssignedToId cannot be null");
                }
            } else {
                throw new IllegalArgumentException("Task is not in the NOT_STARTED status");
            }
        } catch (EntityNotFoundException e) {
            return new TaskResponseDTO("error", "Task not found with id: " + id);
        } catch (IllegalArgumentException e) {
            return new TaskResponseDTO("error", e.getMessage());
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error assigning task: " + e.getMessage());
        }
    }

    @Override
    public TaskResponseDTO assignTaskToSelf(TaskAssignmentRequestDTO assignmentDTO) {
        try {
            Long taskId = assignmentDTO.getTaskId();
            Task existingTask = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

            // Check if the task is already assigned to someone else
            if (existingTask.getAssignedTo() != null) {
                throw new IllegalArgumentException("Task is already assigned to another user");
            }

            // Check if the task is in the NOT_STARTED status
            if (existingTask.getStatus() == TaskStatus.NOT_STARTED) {
                // Assign the task to the specified user
                Long userId = assignmentDTO.getUserId();
                if (userId != null) {
                    User assignedTo = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
                    existingTask.setAssignedTo(assignedTo);

                    // Set the task status to IN_PROGRESS
                    existingTask.setStatus(TaskStatus.IN_PROGRESS);

                    existingTask = taskRepository.save(existingTask);

                    // Returning success message along with task details
                    TaskResponseDTO responseDTO = modelMapper.map(existingTask, TaskResponseDTO.class);
                    responseDTO.setStatus("success");
                    responseDTO.setMessage("Task assigned successfully");
                    return responseDTO;
                } else {
                    throw new IllegalArgumentException("UserId cannot be null");
                }
            } else {
                throw new IllegalArgumentException("Task is not in the NOT_STARTED status");
            }
        } catch (EntityNotFoundException e) {
            return new TaskResponseDTO("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            return new TaskResponseDTO("error", e.getMessage());
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error assigning task: " + e.getMessage());
        }
    }


    @Override
    public TaskResponseDTO updateTaskStatusToDone(TaskStatusUpdateRequestDTO requestDTO) {
        try {
            Long taskId = requestDTO.getTaskId();
            Long userId = requestDTO.getUserId();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

            // Check if the provided userId matches the assignedToId of the task
            Long assignedToId = task.getAssignedTo() != null ? task.getAssignedTo().getId() : null;
            if (!Objects.equals(userId, assignedToId)) {
                throw new IllegalArgumentException("Invalid userId provided for Task with id: " + taskId);
            }

            // Check if the task is before the deadline
            LocalDate currentDate = LocalDate.now();
            LocalDate deadline = task.getDeadline();

            if (deadline != null && !currentDate.isAfter(deadline)) {
                // Check if the current status is IN_PROGRESS
                if (task.getStatus() == TaskStatus.IN_PROGRESS) {
                    // Update the task status to DONE
                    task.setStatus(TaskStatus.DONE);
                    task = taskRepository.save(task);

                    // Returning success message along with updated task details
                    TaskResponseDTO responseDTO = modelMapper.map(task, TaskResponseDTO.class);
                    responseDTO.setStatus("success");
                    responseDTO.setMessage("Task status updated to DONE");
                    return responseDTO;
                } else {
                    // Return an error message if the current status is not IN_PROGRESS
                    throw new IllegalArgumentException("Task status cannot be updated. Current status: " + task.getStatus());
                }
            } else {
                throw new IllegalArgumentException("Deadline was not respected for Task with id: " + taskId);
            }
        } catch (EntityNotFoundException e) {
            return new TaskResponseDTO("error", "Task not found with id: " + requestDTO.getTaskId());
        } catch (IllegalArgumentException e) {
            return new TaskResponseDTO("error", e.getMessage());
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error updating task status: " + e.getMessage());
        }
    }

    @Override
    public TaskResponseDTO deleteTaskCreatedByMe(TaskDeletionRequestDTO deletionRequestDTO) {
        try {
            Long taskId = deletionRequestDTO.getTaskId();
            Long userId = deletionRequestDTO.getUserId();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

            Long createdById = task.getCreatedBy() != null ? task.getCreatedBy().getId() : null;

            if (Objects.equals(userId, createdById)) {
                // Delete the task if createdById matches userId
                taskRepository.deleteById(taskId);
                return new TaskResponseDTO("success", "Task deleted successfully");
            } else {
                throw new IllegalArgumentException("Task can only be deleted by the creator");
            }
        } catch (EntityNotFoundException e) {
            return new TaskResponseDTO("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            return new TaskResponseDTO("error", e.getMessage());
        } catch (Exception e) {
            return new TaskResponseDTO("error", "Error deleting task: " + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<TaskOverviewResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskOverviewResponseDTO> taskOverviewResponseDTOList = tasks.stream()
                .map(task -> modelMapper.map(task, TaskOverviewResponseDTO.class))
                .collect(Collectors.toList());

        return taskOverviewResponseDTOList;
    }


}
