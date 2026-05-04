package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Api.ApiResponse;
import com.example.LearningManagementSystem.Model.Teacher;
import com.example.LearningManagementSystem.Service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/lms/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/get")
    public ResponseEntity<?> getTeachers(){
        return ResponseEntity.status(200).body(teacherService.getTeachers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTeacher(@RequestBody @Valid Teacher teacher, Errors errors){
        if (errors.hasErrors()){
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(msg);
        }

        teacherService.addTeacher(teacher);
        return ResponseEntity.status(200).body(new ApiResponse("Teacher Added Successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable String id, @RequestBody @Valid Teacher teacher, Errors errors){
        if (errors.hasErrors()){
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(msg);
        }

        if (teacherService.updateTeacher(id, teacher)){
            return ResponseEntity.status(200).body(new ApiResponse("Teacher Updated Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Teacher Not Found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String id){
        if (teacherService.deleteTeacher(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Teacher Deleted Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Teacher Not Found"));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateTeacher(@PathVariable String id){
        String result = teacherService.deactivateTeacher(id);
        switch (result){
            case "deactivated":
                return ResponseEntity.status(200).body(new ApiResponse("Teacher Deactivated"));

            case "already_inactive":
                return ResponseEntity.status(400).body(new ApiResponse("Teacher already inactive"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Teacher Not Found"));
        }
    }

    @PutMapping("/{teacherId}/assign-course/{courseId}")
    public ResponseEntity<?> assignCourse(@PathVariable String teacherId, @PathVariable String courseId){

        String result = teacherService.assignCourse(teacherId, courseId);
        switch (result){
            case "assigned":
                return ResponseEntity.status(200).body(new ApiResponse("Course Assigned Successfully"));

            case "already_assigned":
                return ResponseEntity.status(400).body(new ApiResponse("Course already assigned"));

            case "inactive":
                return ResponseEntity.status(400).body(new ApiResponse("Teacher is inactive"));

            case "course_not_found":
                return ResponseEntity.status(400).body(new ApiResponse("Course Not Found"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Teacher Not Found"));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveTeachers(){
        if (teacherService.getActiveTeachers().isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Active Teachers"));
        }

        return ResponseEntity.status(200).body(teacherService.getActiveTeachers());
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<?> getTeacherCourses(@PathVariable String id){
        ArrayList<String> courses = teacherService.getTeacherCourses(id);
        if (courses == null){
            return ResponseEntity.status(400).body(new ApiResponse("Teacher Not Found"));
        }

        if (courses.isEmpty()){
            return ResponseEntity.status(200).body(new ApiResponse("Teacher has no courses"));
        }

        return ResponseEntity.status(200).body(courses);
    }
}
