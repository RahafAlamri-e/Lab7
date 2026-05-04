package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Model.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class StudentService {
    ArrayList<Student> students = new ArrayList<>();
    private final CourseService courseService;

    public ArrayList<Student> getStudents(){
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public boolean updateStudent(String id, Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                students.set(i, student);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStudent(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                students.remove(student);
                return true;
            }
        }
        return false;
    }

    public String deactivateStudent(String id){
        for (Student student : students){
            if (student.getId().equalsIgnoreCase(id)){
                if (!student.getActive()){
                    return "already_inactive";
                }
                student.setActive(false);
                return "deactivated";
            }
        }
        return "not_found";
    }

    public String addCourse(String studentId, String courseId){
        if (!courseService.courseExists(courseId)){
            return "course_not_found";
        }
        for (Student student : students){
            if (student.getId().equalsIgnoreCase(studentId)){

                if (!student.getActive()){
                    return "inactive";
                }

                if (student.getCourseIds().contains(courseId)){
                    return "already_added";
                }

                student.getCourseIds().add(courseId);
                return "added";
            }
        }
        return "not_found";
    }

    public ArrayList<Student> getActiveStudents(){
        ArrayList<Student> activeStudents = new ArrayList<>();

        for (Student student : students){
            if (student.getActive()){
                activeStudents.add(student);
            }
        }
        return activeStudents;
    }

    public ArrayList<String> getStudentCourses(String id){
        for (Student student : students){
            if (student.getId().equalsIgnoreCase(id)){
                return student.getCourseIds();
            }
        }
        return null;
    }

    public boolean studentExists(String id){
        for (Student s : students){
            if (s.getId().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }
}
