package com.theawesomeengineer.taskmanager.openapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Setter;

import java.util.Objects;

/**
 * TaskRequest
 */

@Setter
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-20T17:10:14.012255+08:00[Asia/Singapore]", comments = "Generator version: 7.16.0")
public class TaskRequest {

    private String title;

    private String description;

    private Boolean completed = false;

    public TaskRequest() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public TaskRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public TaskRequest title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Title of the task
     *
     * @return title
     */
    @NotNull
    @Size(max = 255)
    @Schema(name = "title", example = "Complete project documentation", description = "Title of the task", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public TaskRequest description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Detailed description of the task
     *
     * @return description
     */
    @NotNull
    @Size(max = 1000)
    @Schema(name = "description", example = "Write comprehensive documentation for the task management API", description = "Detailed description of the task", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public TaskRequest completed(Boolean completed) {
        this.completed = completed;
        return this;
    }

    /**
     * Whether the task is completed
     *
     * @return completed
     */

    @Schema(name = "completed", example = "false", description = "Whether the task is completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("completed")
    public Boolean getCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskRequest taskRequest = (TaskRequest) o;
        return Objects.equals(this.title, taskRequest.title) &&
                Objects.equals(this.description, taskRequest.description) &&
                Objects.equals(this.completed, taskRequest.completed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, completed);
    }

    @Override
    public String toString() {
        return "class TaskRequest {\n" +
                "    title: " + toIndentedString(title) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    completed: " + toIndentedString(completed) + "\n" +
                "}";
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

