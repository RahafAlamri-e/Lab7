package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Api.ApiResponse;
import com.example.LearningManagementSystem.Model.Assignment;
import com.example.LearningManagementSystem.Service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/lms/assignment")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping("/get")
    public ResponseEntity<?> getAssignments(){
        return ResponseEntity.status(200).body(assignmentService.getAssignments());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAssignment(@RequestBody @Valid Assignment assignment, Errors errors){

        if (errors.hasErrors()){
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(msg);
        }

        String result = assignmentService.addAssignment(assignment);

        switch (result){

            case "added":
                return ResponseEntity.status(200).body(new ApiResponse("Assignment Added Successfully"));

            case "course_not_found":
                return ResponseEntity.status(400).body(new ApiResponse("Course Not Found"));

            case "teacher_not_found":
                return ResponseEntity.status(400).body(new ApiResponse("Teacher Not Found"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Error"));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable String id, @RequestBody @Valid Assignment assignment, Errors errors){

        if (errors.hasErrors()){
            return ResponseEntity.status(400)
                    .body(errors.getFieldError().getDefaultMessage());
        }

        if (assignmentService.updateAssignment(id, assignment)){
            return ResponseEntity.status(200).body(new ApiResponse("Updated Successfully"));
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Assignment Not Found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable String id){

        if (assignmentService.deleteAssignment(id)){
            return ResponseEntity.status(200)
                    .body(new ApiResponse("Deleted Successfully"));
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Assignment Not Found"));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getStatus(@PathVariable String id){

        String result = assignmentService.isAssignmentOpen(id);

        switch (result){

            case "open":
                return ResponseEntity.status(200).body("Assignment is Open");

            case "closed":
                return ResponseEntity.status(400).body("Assignment Closed");

            case "inactive":
                return ResponseEntity.status(400).body("Assignment Inactive");

            default:
                return ResponseEntity.status(400).body("Assignment Not Found");
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getByCourse(@PathVariable String courseId){

        ArrayList<Assignment> list = assignmentService.getAssignmentsByCourse(courseId);

        if (list.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Assignments Found"));
        }

        return ResponseEntity.status(200).body(list);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActive(){

        if (assignmentService.getActiveAssignments().isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Active Assignments"));
        }

        return ResponseEntity.status(200).body(assignmentService.getActiveAssignments());
    }

    @GetMapping("/{id}/days-left")
    public ResponseEntity<?> getDaysLeft(@PathVariable String id){

        Long days = assignmentService.getDaysLeft(id);

        if (days == null){
            return ResponseEntity.status(400).body(new ApiResponse("Assignment Not Found"));
        }

        return ResponseEntity.status(200).body(days);
    }
}
