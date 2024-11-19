package com.jeeproject.service;

import com.jeeproject.controller.StudentController;
import com.jeeproject.dao.StudentDAO;
import com.jeeproject.model.Student;
import com.jeeproject.model.User;
import jakarta.servlet.ServletException;

import java.util.List;

public class StudentService {

    private static final StudentDAO studentDAO = new StudentDAO();


    public static void addStudent(Student student) {
        studentDAO.save(student);
    }

    public static Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    public static List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    public static void updateStudent(Student student) {
        studentDAO.update(student);
    }

    public static void deleteStudent(int id) {
        studentDAO.delete(id);
    }
}
