package com.theawesomeengineer.taskmanager.controller;

import com.theawesomeengineer.taskmanager.openapi.api.TasksApi;
import com.theawesomeengineer.taskmanager.openapi.model.Task;
import com.theawesomeengineer.taskmanager.openapi.model.TaskRequest;
import com.theawesomeengineer.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TasksApiController implements TasksApi {

    private final TaskService taskService;

    public TasksApiController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ResponseEntity<Task> createTask(TaskRequest taskRequest) {
        Task created = taskService.createTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Override
    public ResponseEntity<Task> getTaskById(Long id) {
        Task t = taskService.getTaskById(id);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }

    @Override
    public ResponseEntity<Task> updateTask(Long id, TaskRequest taskRequest) {
        Task updated = taskService.updateTask(id, taskRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
}
