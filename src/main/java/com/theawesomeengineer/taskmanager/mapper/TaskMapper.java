package com.theawesomeengineer.taskmanager.mapper;

import com.theawesomeengineer.taskmanager.entity.TaskEntity;
import com.theawesomeengineer.taskmanager.openapi.model.Task;
import com.theawesomeengineer.taskmanager.openapi.model.TaskRequest;

import java.time.OffsetDateTime;

public class TaskMapper {

    public static TaskEntity toEntity(TaskRequest request) {
        return TaskEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.getCompleted())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public static Task toDTO(TaskEntity entity) {
        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .completed(entity.getCompleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static void updateEntityFromRequest(TaskEntity entity, TaskRequest req) {
        if (req == null || entity == null) return;
        if (req.getTitle() != null) entity.setTitle(req.getTitle());
        if (req.getDescription() != null) entity.setDescription(req.getDescription());
        if (req.getCompleted() != null) entity.setCompleted(req.getCompleted());
    }
}
