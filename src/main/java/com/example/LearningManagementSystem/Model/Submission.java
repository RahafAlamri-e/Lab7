package com.example.LearningManagementSystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Submission {
    @NotEmpty(message = "Id cannot be empty")
    @Size(min = 3, message = "Id must be at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Id must contain letters and numbers only")
    private String id;

    @NotEmpty(message = "Student id cannot be empty")
    @Size(min = 3, message = "Student id must be at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Student id must contain letters and numbers only")
    private String studentId;

    @NotEmpty(message = "Assignment id cannot be empty")
    @Size(min = 3, message = "Assignment id must be at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Assignment id must contain letters and numbers only")
    private String assignmentId;

    @NotEmpty(message = "Content cannot be empty")
    private String content;

    private Integer grade; // nullable

    private LocalDateTime submittedAt;

    private Boolean late;

    private Boolean graded;
}
