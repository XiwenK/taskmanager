package com.theawesomeengineer.taskmanager.openapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Error
 */

@Setter
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-20T17:10:14.012255+08:00[Asia/Singapore]", comments = "Generator version: 7.16.0")
public class Error {

    private String message;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime timestamp;

    private @Nullable String details;

    public Error() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public Error(String message, OffsetDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public Error message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Error message
     *
     * @return message
     */
    @NotNull
    @Schema(name = "message", example = "Task not found", description = "Error message", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public Error timestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Timestamp when the error occurred
     *
     * @return timestamp
     */
    @NotNull
    @Valid
    @Schema(name = "timestamp", example = "2024-01-01T10:00Z", description = "Timestamp when the error occurred", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("timestamp")
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public Error details(@Nullable String details) {
        this.details = details;
        return this;
    }

    /**
     * Additional error details
     *
     * @return details
     */

    @Schema(name = "details", example = "Task with ID 123 does not exist", description = "Additional error details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("details")
    public @Nullable String getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Error error = (Error) o;
        return Objects.equals(this.message, error.message) &&
                Objects.equals(this.timestamp, error.timestamp) &&
                Objects.equals(this.details, error.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, timestamp, details);
    }

    @Override
    public String toString() {
        return "class Error {\n" +
                "    message: " + toIndentedString(message) + "\n" +
                "    timestamp: " + toIndentedString(timestamp) + "\n" +
                "    details: " + toIndentedString(details) + "\n" +
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

