package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TeacherService {
    ArrayList<Teacher> teachers = new ArrayList<>();
    private final CourseService courseService;

    public ArrayList<Teacher> getTeachers(){
        return teachers;
    }

    public void addTeacher(Teacher teacher){
        teachers.add(teacher);
    }

    public boolean updateTeacher(String id, Teacher teacher){
        for (int i = 0; i < teachers.size(); i++){
            if (teachers.get(i).getId().equalsIgnoreCase(id)){
                teachers.set(i, teacher);
                return true;
            }
        }
        return false;
    }

    public boolean deleteTeacher(String id){
        for (int i = 0; i < teachers.size(); i++){
            if (teachers.get(i).getId().equalsIgnoreCase(id)){
                teachers.remove(i);
                return true;
            }
        }
        return false;
    }

    public String deactivateTeacher(String id){
        for (Teacher teacher : teachers){
            if (teacher.getId().equalsIgnoreCase(id)){
                if (!teacher.getActive()){
                    return "already_inactive";
                }
                teacher.setActive(false);
                return "deactivated";
            }
        }
        return "not_found";
    }

    public String assignCourse(String teacherId, String courseId){

        if (!courseService.courseExists(courseId)){
            return "course_not_found";
        }

        for (Teacher teacher : teachers){

            if (teacher.getId().equalsIgnoreCase(teacherId)){

                if (!teacher.getActive()){
                    return "inactive";
                }

                if (teacher.getCourseIds().contains(courseId)){
                    return "already_assigned";
                }

                teacher.getCourseIds().add(courseId);
                return "assigned";
            }
        }

        return "not_found";
    }

    public ArrayList<Teacher> getActiveTeachers(){
        ArrayList<Teacher> activeTeachers = new ArrayList<>();

        for (Teacher teacher : teachers){
            if (teacher.getActive()){
                activeTeachers.add(teacher);
            }
        }
        return activeTeachers;
    }

    public ArrayList<String> getTeacherCourses(String id){
        for (Teacher teacher : teachers){
            if (teacher.getId().equalsIgnoreCase(id)){
                return teacher.getCourseIds();
            }
        }
        return null;
    }

    public boolean teacherExists(String id){
        for (Teacher t : teachers){
            if (t.getId().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }
}
