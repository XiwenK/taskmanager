package com.theawesomeengineer.taskmanager.service;

import com.theawesomeengineer.taskmanager.entity.TaskEntity;
import com.theawesomeengineer.taskmanager.exception.EntityNotFoundException;
import com.theawesomeengineer.taskmanager.mapper.TaskMapper;
import com.theawesomeengineer.taskmanager.openapi.model.Task;
import com.theawesomeengineer.taskmanager.openapi.model.TaskRequest;
import com.theawesomeengineer.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(TaskRequest req) {
        TaskEntity entity = TaskMapper.toEntity(req);
        TaskEntity saved = taskRepository.save(entity);
        return TaskMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        List<TaskEntity> taskEntityList = taskRepository.findAll();
        if (taskEntityList.isEmpty()) {
            throw new EntityNotFoundException("No tasks found");
        }

        return taskEntityList.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        TaskEntity e = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        return TaskMapper.toDTO(e);
    }

    public Task updateTask(Long id, TaskRequest req) {
        TaskEntity e = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        TaskMapper.updateEntityFromRequest(e, req);
        TaskEntity saved = taskRepository.save(e);
        return TaskMapper.toDTO(saved);
    }

    public void deleteTask(Long id) {
        TaskEntity e = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        taskRepository.delete(e);
    }
}
