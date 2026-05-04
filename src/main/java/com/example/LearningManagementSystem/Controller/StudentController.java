package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Api.ApiResponse;
import com.example.LearningManagementSystem.Model.Student;
import com.example.LearningManagementSystem.Service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/lms/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/get")
    public ResponseEntity<?> getStudents(){
        return ResponseEntity.status(200).body(studentService.getStudents());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody @Valid Student student, Errors errors){
        if (errors.hasErrors()){
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(msg);
        }

        studentService.addStudent(student);
        return ResponseEntity.status(200).body(new ApiResponse("Student Added Successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody @Valid Student student, Errors errors){
        if (errors.hasErrors()){
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(msg);
        }

        if (studentService.updateStudent(id, student)){
            return ResponseEntity.status(200).body(new ApiResponse("Student Updated Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Student Not Found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id){
        if (studentService.deleteStudent(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Student Deleted Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Student Not Found"));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateStudent(@PathVariable String id){
        String result = studentService.deactivateStudent(id);
        switch (result){
            case "deactivated":
                return ResponseEntity.status(200).body(new ApiResponse("Student Deactivated"));

            case "already_inactive":
                return ResponseEntity.status(400).body(new ApiResponse("Student already inactive"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Student Not Found"));
        }
    }

    @PutMapping("/{studentId}/add-course/{courseId}")
    public ResponseEntity<?> addCourse(@PathVariable String studentId, @PathVariable String courseId){
        String result = studentService.addCourse(studentId, courseId);
        switch (result){
            case "added":
                return ResponseEntity.status(200).body(new ApiResponse("Course Added Successfully"));

            case "already_added":
                return ResponseEntity.status(400).body(new ApiResponse("Course already added"));

            case "inactive":
                return ResponseEntity.status(400).body(new ApiResponse("Student is inactive"));

            case "course_not_found":
                return ResponseEntity.status(400).body(new ApiResponse("Course Not Found"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Student Not Found"));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveStudents(){
        if (studentService.getActiveStudents().isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Active Students"));
        }

        return ResponseEntity.status(200).body(studentService.getActiveStudents());
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<?> getStudentCourses(@PathVariable String id) {
        ArrayList<String> courses = studentService.getStudentCourses(id);

        if (courses == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Student Not Found"));
        }

        if (courses.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("Student has no courses"));
        }

        return ResponseEntity.status(200).body(courses);
    }

}
