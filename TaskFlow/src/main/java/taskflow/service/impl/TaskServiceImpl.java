package taskflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import taskflow.dto.request.TaskRequestDTO;
import taskflow.dto.response.TaskResponseDTO;
import taskflow.entities.Tags;
import taskflow.entities.Task;
import taskflow.entities.User;
import taskflow.entities.enums.TaskStatus;
import taskflow.repository.TagsRepository;
import taskflow.repository.TaskRepository;
import taskflow.repository.UserRepository;
import taskflow.service.TaskService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
            Task task = modelMapper.map(taskRequestDTO, Task.class);
            // Set the creation date to the current date
            task.setCreationDate(LocalDate.now());

            // Set the task status to NOT_STARTED
            task.setStatus(TaskStatus.NOT_STARTED);

            // Set the createdBy user
            Long createdById = taskRequestDTO.getCreatedById();
            if (createdById != null) {
                User createdBy = userRepository.findById(createdById)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createdById));
                task.setCreatedBy(createdBy);
            }

            // Validate the start date (ensure it is at least 3 days from now)
            LocalDate startDate = task.getStartDate();
            if (startDate != null && startDate.isBefore(LocalDate.now().plusDays(3))) {
                throw new IllegalArgumentException("Start date must be at least 3 days from now");
            }

            // Validate the deadline (ensure it is at least 3 days from the start date)
            LocalDate deadline = task.getDeadline();
            if (deadline != null && startDate != null && deadline.isBefore(startDate.plusDays(3))) {
                throw new IllegalArgumentException("Deadline must be at least 3 days from the start date");
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
