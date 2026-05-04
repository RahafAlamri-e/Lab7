package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Api.ApiResponse;
import com.example.LearningManagementSystem.Model.Submission;
import com.example.LearningManagementSystem.Service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lms/submission")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(200).body(submissionService.getSubmissions());
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(@RequestBody @Valid Submission submission, Errors errors){

        if (errors.hasErrors()){
            return ResponseEntity.status(400)
                    .body(errors.getFieldError().getDefaultMessage());
        }

        String result = submissionService.submit(submission);

        switch (result){

            case "submitted":
                return ResponseEntity.status(200).body(new ApiResponse("Submitted Successfully"));

            case "already_submitted":
                return ResponseEntity.status(400).body(new ApiResponse("Already submitted"));

            case "student_not_found":
                return ResponseEntity.status(400).body(new ApiResponse("Student Not Found"));

            case "assignment_not_found":
                return ResponseEntity.status(400).body(new ApiResponse("Assignment Not Found"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Error"));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Submission submission, Errors errors){

        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        if (submissionService.updateSubmission(id, submission)){
            return ResponseEntity.status(200).body(new ApiResponse("Submission Updated Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Submission Not Found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){

        if (submissionService.deleteSubmission(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Deleted Successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Submission Not Found"));
    }

    @PutMapping("/grade/{id}/{grade}")
    public ResponseEntity<?> grade(@PathVariable String id, @PathVariable int grade){

        String result = submissionService.grade(id, grade);

        switch (result){

            case "graded":
                return ResponseEntity.status(200)
                        .body(new ApiResponse("Graded Successfully"));

            case "already_graded":
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Already graded"));

            case "invalid_grade":
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Grade must be between 0-100"));

            default:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Submission Not Found"));
        }
    }

    @GetMapping("/{id}/late")
    public ResponseEntity<?> isLate(@PathVariable String id){

        String result = submissionService.isLate(id);

        switch (result){

            case "late":
                return ResponseEntity.status(200).body("Late");

            case "on_time":
                return ResponseEntity.status(200).body("On Time");

            default:
                return ResponseEntity.status(400).body("Submission Not Found");
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> status(@PathVariable String id){

        String result = submissionService.getStatus(id);

        switch (result){

            case "pass":
                return ResponseEntity.status(200).body("Pass");

            case "fail":
                return ResponseEntity.status(200).body("Fail");

            case "not_graded":
                return ResponseEntity.status(400).body("Not graded yet");

            default:
                return ResponseEntity.status(400).body("Submission Not Found");
        }
    }
}
