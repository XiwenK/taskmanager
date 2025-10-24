package com.theawesomeengineer.taskmanager.service;

import com.theawesomeengineer.taskmanager.entity.TaskEntity;
import com.theawesomeengineer.taskmanager.exception.EntityNotFoundException;
import com.theawesomeengineer.taskmanager.openapi.model.Task;
import com.theawesomeengineer.taskmanager.openapi.model.TaskRequest;
import com.theawesomeengineer.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testCreateTask() {
        TaskRequest req = new TaskRequest();
        req.setTitle("Test Task");
        req.setDescription("Test Description");

        TaskEntity savedEntity = TaskEntity.builder()
                .id(1L)
                .title(req.getTitle())
                .description(req.getDescription())
                .completed(false)
                .build();

        // Mock repository behavior
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(savedEntity);

        Task result = taskService.createTask(req);

        // value validation
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertFalse(result.getCompleted());

        // behavior validation
        verify(taskRepository, times(1)).save(any(TaskEntity.class));

        // mapping validation
        verify(taskRepository).save(argThat(entity ->
                entity.getTitle().equals("Test Task") &&
                        entity.getDescription().equals("Test Description") &&
                        !entity.getCompleted()
        ));

        // duplicate calls validation
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testGetAllTasks() {
        TaskEntity e1 = TaskEntity.builder().id(1L).title("A").completed(false).build();
        TaskEntity e2 = TaskEntity.builder().id(2L).title("B").completed(true).build();

        when(taskRepository.findAll()).thenReturn(List.of(e1, e2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getTitle());
        assertTrue(result.get(1).getCompleted());

        verify(taskRepository, times(1)).findAll();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testGetAllTasks_Empty_ThrowsException() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> taskService.getAllTasks());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        TaskEntity e = TaskEntity.builder().id(1L).title("Task1").description("Desc1").completed(false).build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(e));

        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Task1", result.getTitle());
        assertFalse(result.getCompleted());

        verify(taskRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(1L));

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTask_Success() {
        Long id = 10L;
        TaskEntity existing = TaskEntity.builder().id(id).title("Old").description("OldDesc").completed(false).build();

        TaskRequest req = new TaskRequest();
        req.setTitle("Updated");
        req.setDescription("UpdatedDesc");
        req.setCompleted(true);

        TaskEntity updatedEntity = TaskEntity.builder()
                .id(id).title(req.getTitle()).description(req.getDescription()).completed(true).build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(updatedEntity);

        Task result = taskService.updateTask(id, req);

        assertEquals("Updated", result.getTitle());
        assertEquals("UpdatedDesc", result.getDescription());
        assertTrue(result.getCompleted());

        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).save(argThat(e ->
                e.getTitle().equals("Updated") &&
                        e.getDescription().equals("UpdatedDesc") &&
                        e.getCompleted()
        ));

        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testUpdateTask_NotFound() {
        when(taskRepository.findById(123L)).thenReturn(Optional.empty());

        TaskRequest req = new TaskRequest();
        req.setTitle("DoesNotMatter");

        assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(123L, req));

        verify(taskRepository, times(1)).findById(123L);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testDeleteTask_Success() {
        Long id = 50L;
        TaskEntity e = TaskEntity.builder().id(id).title("ToDelete").build();
        when(taskRepository.findById(id)).thenReturn(Optional.of(e));

        taskService.deleteTask(id);

        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).delete(e);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(77L));
        
        verify(taskRepository, times(1)).findById(77L);
        verifyNoMoreInteractions(taskRepository);
    }
}
