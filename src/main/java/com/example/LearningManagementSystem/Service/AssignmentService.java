package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Model.Assignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    ArrayList<Assignment> assignments = new ArrayList<>();

    private final CourseService courseService;
    private final TeacherService teacherService;

    public ArrayList<Assignment> getAssignments(){
        return assignments;
    }

    public String addAssignment(Assignment assignment){

        if (!courseService.courseExists(assignment.getCourseId())){
            return "course_not_found";
        }

        if (!teacherService.teacherExists(assignment.getTeacherId())){
            return "teacher_not_found";
        }

        assignments.add(assignment);
        return "added";
    }

    public boolean updateAssignment(String id, Assignment assignment){
        for (int i = 0; i < assignments.size(); i++){
            if (assignments.get(i).getId().equalsIgnoreCase(id)){
                assignments.set(i, assignment);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAssignment(String id){
        for (int i = 0; i < assignments.size(); i++){
            if (assignments.get(i).getId().equalsIgnoreCase(id)){
                assignments.remove(i);
                return true;
            }
        }
        return false;
    }

    public String isAssignmentOpen(String id){

        for (Assignment a : assignments){
            if (a.getId().equalsIgnoreCase(id)){

                if (!a.getActive()){
                    return "inactive";
                }

                if (LocalDate.now().isAfter(a.getDueDate())){
                    return "closed";
                }

                return "open";
            }
        }

        return "not_found";
    }

    public ArrayList<Assignment> getAssignmentsByCourse(String courseId){

        ArrayList<Assignment> list = new ArrayList<>();

        for (Assignment a : assignments){
            if (a.getCourseId().equalsIgnoreCase(courseId)){
                list.add(a);
            }
        }

        return list;
    }

    public ArrayList<Assignment> getActiveAssignments(){

        ArrayList<Assignment> list = new ArrayList<>();

        for (Assignment a : assignments){
            if (a.getActive()){
                list.add(a);
            }
        }

        return list;
    }

    public Long getDaysLeft(String id){

        for (Assignment a : assignments){
            if (a.getId().equalsIgnoreCase(id)){
                return (long) LocalDate.now().until(a.getDueDate()).getDays();
            }
        }

        return null;
    }
}
