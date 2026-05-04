package com.example.LearningManagementSystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Assignment {
    @NotEmpty(message = "Id cannot be empty")
    @Size(min = 3, message = "Id must be at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Id must contain letters and numbers only")
    private String id;

    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Title must contain letters and numbers only")
    private String title;

    @NotEmpty(message = "Course id cannot be empty")
    @Size(min = 3, message = "Course id must be at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Course id must contain letters and numbers only")
    private String courseId;

    @NotEmpty(message = "Teacher id cannot be empty")
    @Size(min = 3, message = "Teacher id must be at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Teacher id must contain letters and numbers only")
    private String teacherId;

    @Min(value = 1, message = "Max grade must be at least 1")
    private int maxGrade;

    @NotNull(message = "Due date cannot be null")
    @Future(message = "Due Date must be in future")
    private LocalDate dueDate;

    @NotNull(message = "Active cannot be null")
    private Boolean active;
}
