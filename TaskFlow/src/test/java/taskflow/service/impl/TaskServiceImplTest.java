package taskflow.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import taskflow.dto.request.*;
import taskflow.dto.response.TaskResponseDTO;
import taskflow.entities.Task;
import taskflow.entities.User;
import taskflow.entities.enums.Role;
import taskflow.entities.enums.TaskStatus;
import taskflow.repository.TagsRepository;
import taskflow.repository.TaskRepository;
import taskflow.repository.UserRepository;

@SpringBootTest
public class TaskServiceImplTest {
    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final TagsRepository tagRepository = mock(TagsRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ModelMapper modelMapper = new ModelMapper();

    private final TaskServiceImpl taskService = new TaskServiceImpl(taskRepository, tagRepository, userRepository, modelMapper);

    @Test
    public void testCreateTaskSuccess() {
        // Arrange
        TaskRequestDTO requestDTO = createTaskRequestDTO();
        User adminUser = createUserWithRole(Role.ADMIN);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(adminUser));
        when(tagRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(taskRepository.save(any(Task.class))).thenReturn(createTask());

        // Act
        TaskResponseDTO responseDTO = taskService.createTask(requestDTO);

        // Assert
        assertEquals("success", responseDTO.getStatus());
        assertEquals("Task created successfully", responseDTO.getMessage());
    }

    @Test
    public void testCreateTaskNonAdminUser() {
        // Arrange
        TaskRequestDTO requestDTO = createTaskRequestDTO();
        User nonAdminUser = createUserWithRole(Role.USER);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(nonAdminUser));

        // Act
        TaskResponseDTO responseDTO = taskService.createTask(requestDTO);

        // Assert
        assertEquals("error", responseDTO.getStatus());
        assertTrue(responseDTO.getMessage().contains("does not have the role ADMIN"));
    }

    @Test
    public void testCreateTaskInvalidStartDate() {
        // Arrange
        TaskRequestDTO requestDTO = createTaskRequestDTO();
        User adminUser = createUserWithRole(Role.ADMIN);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(adminUser));
        requestDTO.setStartDate(LocalDate.now().plusDays(2)); // Set invalid start date

        // Act
        TaskResponseDTO responseDTO = taskService.createTask(requestDTO);

        // Assert
        assertEquals("error", responseDTO.getStatus());
        assertTrue(responseDTO.getMessage().contains("Start date must be at least 3 days from now"));
    }

    @Test
    public void testAssignTaskToSelfAlreadyAssigned() {
        // Arrange
        TaskAssignmentRequestDTO assignmentDTO = new TaskAssignmentRequestDTO();
        assignmentDTO.setTaskId(1L);

        Task existingTask = createTask();
        existingTask.setAssignedTo(createUserWithRole(Role.USER));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act
        TaskResponseDTO responseDTO = taskService.assignTaskToSelf(assignmentDTO);

        // Assert
        assertEquals("error", responseDTO.getStatus());
        assertTrue(responseDTO.getMessage().contains("Task is already assigned to another user"));
    }


    @Test
    public void testUpdateTaskStatusToDoneInvalidUser() {
        // Arrange
        TaskStatusUpdateRequestDTO requestDTO = new TaskStatusUpdateRequestDTO();
        requestDTO.setTaskId(1L);
        requestDTO.setUserId(99L); // Invalid user ID

        Task existingTask = createTask();
        existingTask.setStatus(TaskStatus.IN_PROGRESS);
        existingTask.setAssignedTo(createUserWithRole(Role.USER));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act
        TaskResponseDTO responseDTO = taskService.updateTaskStatusToDone(requestDTO);

        // Assert
        assertEquals("error", responseDTO.getStatus());
        assertTrue(responseDTO.getMessage().contains("Invalid userId provided for Task with id: 1"));
    }


    @Test
    public void testDeleteTaskCreatedByMeSuccess() {
        // Arrange
        TaskDeletionRequestDTO deletionRequestDTO = new TaskDeletionRequestDTO();
        deletionRequestDTO.setTaskId(1L);
        deletionRequestDTO.setUserId(1L);

        Task existingTask = createTask();
        existingTask.setCreatedBy(createUserWithRole(Role.ADMIN));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act
        TaskResponseDTO responseDTO = taskService.deleteTaskCreatedByMe(deletionRequestDTO);

        // Assert
        assertEquals("success", responseDTO.getStatus());
        assertEquals("Task deleted successfully", responseDTO.getMessage());
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTaskCreatedByMeInvalidUser() {
        // Arrange
        TaskDeletionRequestDTO deletionRequestDTO = new TaskDeletionRequestDTO();
        deletionRequestDTO.setTaskId(1L);
        deletionRequestDTO.setUserId(99L); // Invalid user ID

        Task existingTask = createTask();
        existingTask.setCreatedBy(createUserWithRole(Role.ADMIN));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act
        TaskResponseDTO responseDTO = taskService.deleteTaskCreatedByMe(deletionRequestDTO);

        // Assert
        assertEquals("error", responseDTO.getStatus());
        assertTrue(responseDTO.getMessage().contains("Task can only be deleted by the creator"));
        verify(taskRepository, times(0)).deleteById(1L);
    }

    // Helper methods
    private TaskRequestDTO createTaskRequestDTO() {
        TaskRequestDTO requestDTO = new TaskRequestDTO();
        requestDTO.setCreatedById(1L);
        return requestDTO;
    }

    private User createUserWithRole(Role role) {
        User user = new User();
        user.setId(1L);
        user.setRole(role);
        return user;
    }

    private Task createTask() {
        Task task = new Task();
        task.setId(1L);
        return task;
    }


}
