package com.example.LearningManagementSystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Student {

    @NotEmpty(message = "Id cannot be empty")
    @Size(min = 3, message = "Id must be at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Id must contain letters and numbers only")
    private String id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain letters only")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    private String email;

    @Min(value = 6, message = "Age must be at least 6")
    private int age;

    @NotEmpty(message = "Major cannot be empty")
    @Size(min = 2, max = 40, message = "Major must be between 2 and 40 characters")
    private String major;

    @NotNull(message = "Active status cannot be null")
    @AssertTrue(message = "Active must be true")
    private Boolean active;

    private ArrayList<String> courseIds;

}
