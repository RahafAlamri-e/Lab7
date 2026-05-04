package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Model.Course;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final StudentService studentService;
    private final TeacherService teacherService;


    ArrayList<Course> courses = new ArrayList<>();

    public ArrayList<Course> getCourses(){
        return courses;
    }

    public String addCourse(Course course){

        if (!teacherService.teacherExists(course.getTeacherId())){
            return "teacher_not_found";
        }

        courses.add(course);
        return "added";
    }

    public boolean updateCourse(String id, Course course){
        for (int i = 0; i < courses.size(); i++){
            if (courses.get(i).getId().equalsIgnoreCase(id)){
                courses.set(i, course);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCourse(String id){
        for (int i = 0; i < courses.size(); i++){
            if (courses.get(i).getId().equalsIgnoreCase(id)){
                courses.remove(i);
                return true;
            }
        }
        return false;
    }

    public String enrollStudent(String courseId, String studentId){

        if (!studentService.studentExists(studentId)){
            return "student_not_found";
        }

        for (Course course : courses){

            if (course.getId().equalsIgnoreCase(courseId)){

                if (!course.getActive()){
                    return "inactive_course";
                }

                if (course.getStudentIds().contains(studentId)){
                    return "already_enrolled";
                }

                if (course.getStudentIds().size() >= course.getCapacity()){
                    return "full";
                }

                course.getStudentIds().add(studentId);
                return "enrolled";
            }
        }

        return "course_not_found";
    }

    public String unenrollStudent(String courseId, String studentId){

        for (Course course : courses){

            if (course.getId().equalsIgnoreCase(courseId)){

                if (!course.getStudentIds().contains(studentId)){
                    return "not_enrolled";
                }

                course.getStudentIds().remove(studentId);
                return "removed";
            }
        }

        return "course_not_found";
    }

    public ArrayList<Course> getAvailableCourses(){

        ArrayList<Course> available = new ArrayList<>();

        for (Course course : courses){
            if (course.getActive()){
                available.add(course);
            }
        }

        return available;
    }

    public int getStudentCount(String courseId){

        for (Course course : courses){
            if (course.getId().equalsIgnoreCase(courseId)){
                return course.getStudentIds().size();
            }
        }

        return 0;
    }

    public boolean courseExists(String id){
        for (Course c : courses){
            if (c.getId().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }
}
