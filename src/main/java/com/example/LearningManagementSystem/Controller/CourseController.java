package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Api.ApiResponse;
import com.example.LearningManagementSystem.Model.Course;
import com.example.LearningManagementSystem.Service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lms/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/get")
    public ResponseEntity<?> getCourses(){
        return ResponseEntity.status(200).body(courseService.getCourses());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody @Valid Course course, Errors errors){
        if (errors.hasErrors()){
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(msg);
        }

        String result = courseService.addCourse(course);
        if (result.equals("teacher_not_found")){
            return ResponseEntity.status(400)
                    .body(new ApiResponse("Teacher Not Found"));
        }

        return ResponseEntity.status(200)
                .body(new ApiResponse("Course Added Successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody @Valid Course course, Errors errors){
        if (errors.hasErrors()){
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(msg);
        }

        if (courseService.updateCourse(id, course)){
            return ResponseEntity.status(200).body(new ApiResponse("Course Updated Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Course Not Found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id){
        if (courseService.deleteCourse(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Course Deleted Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Course Not Found"));
    }

    @PutMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<?> enrollStudent(@PathVariable String courseId, @PathVariable String studentId){

        String result = courseService.enrollStudent(courseId, studentId);

        switch (result){

            case "enrolled":
                return ResponseEntity.status(200).body(new ApiResponse("Student Enrolled Successfully"));

            case "already_enrolled":
                return ResponseEntity.status(400).body(new ApiResponse("Student already enrolled"));

            case "inactive_course":
                return ResponseEntity.status(400).body(new ApiResponse("Course is inactive"));

            case "full":
                return ResponseEntity.status(400).body(new ApiResponse("Course is full"));

            case "student_not_found":
                return ResponseEntity.status(400).body(new ApiResponse("Student Not Found"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Course Not Found"));
        }
    }

    @PutMapping("/{courseId}/unenroll/{studentId}")
    public ResponseEntity<?> unenrollStudent(@PathVariable String courseId, @PathVariable String studentId){

        String result = courseService.unenrollStudent(courseId, studentId);

        switch (result){
            case "removed":
                return ResponseEntity.status(200)
                        .body(new ApiResponse("Student removed successfully"));

            case "not_enrolled":
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Student is not enrolled"));

            default:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Course Not Found"));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAvailableCourses(){

        if (courseService.getAvailableCourses().isEmpty()){
            return ResponseEntity.status(400)
                    .body(new ApiResponse("No Active Courses"));
        }

        return ResponseEntity.status(200).body(courseService.getAvailableCourses());
    }

    @GetMapping("/{courseId}/students-count")
    public ResponseEntity<?> getStudentCount(@PathVariable String courseId){

        int count = courseService.getStudentCount(courseId);

        if (count == 0){
            return ResponseEntity.status(400).body(new ApiResponse("Course Not Found"));
        }

        return ResponseEntity.status(200).body(count);
    }
}
