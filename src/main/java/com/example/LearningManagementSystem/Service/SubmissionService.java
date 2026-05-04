package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Model.Assignment;
import com.example.LearningManagementSystem.Model.Submission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    ArrayList<Submission> submissions = new ArrayList<>();

    private final AssignmentService assignmentService;
    private final StudentService studentService;

    public ArrayList<Submission> getSubmissions(){
        return submissions;
    }

    public String submit(Submission submission){

        if (!studentService.studentExists(submission.getStudentId())){
            return "student_not_found";
        }

        Assignment assignment = null;

        for (Assignment a : assignmentService.getAssignments()) {
            if (a.getId().equalsIgnoreCase(submission.getAssignmentId())) {
                assignment = a;
                break;
            }
        }

        if (assignment == null){
            return "assignment_not_found";
        }

        for (Submission s : submissions){
            if (s.getStudentId().equalsIgnoreCase(submission.getStudentId())
                    && s.getAssignmentId().equalsIgnoreCase(submission.getAssignmentId())){
                return "already_submitted";
            }
        }

        submission.setSubmittedAt(LocalDateTime.now());

        if (LocalDateTime.now().isAfter(assignment.getDueDate().atStartOfDay())){
            submission.setLate(true);
        } else {
            submission.setLate(false);
        }

        submission.setGraded(false);

        submissions.add(submission);
        return "submitted";
    }

    public boolean updateSubmission(String id, Submission submission){

        for (int i = 0; i < submissions.size(); i++){

            if (submissions.get(i).getId().equalsIgnoreCase(id)){

                Submission old = submissions.get(i);
                old.setContent(submission.getContent());
                return true;
            }
        }

        return false;
    }

    public boolean deleteSubmission(String id){
        for (int i = 0; i < submissions.size(); i++){
            if (submissions.get(i).getId().equalsIgnoreCase(id)){
                submissions.remove(i);
                return true;
            }
        }
        return false;
    }

    public String grade(String submissionId, int grade){

        for (Submission s : submissions){

            if (s.getId().equalsIgnoreCase(submissionId)){

                if (s.getGraded()){
                    return "already_graded";
                }

                if (grade < 0 || grade > 100){
                    return "invalid_grade";
                }

                s.setGrade(grade);
                s.setGraded(true);
                return "graded";
            }
        }

        return "not_found";
    }

    public String isLate(String id){

        for (Submission s : submissions){
            if (s.getId().equalsIgnoreCase(id)){
                return s.getLate() ? "late" : "on_time";
            }
        }

        return "not_found";
    }

    public String getStatus(String id){

        for (Submission s : submissions){

            if (s.getId().equalsIgnoreCase(id)){

                if (!s.getGraded()){
                    return "not_graded";
                }

                return s.getGrade() >= 60 ? "pass" : "fail";
            }
        }

        return "not_found";
    }

}
