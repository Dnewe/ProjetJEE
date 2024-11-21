package com.jeeproject.service;

import com.jeeproject.dao.CourseDAO;
import com.jeeproject.model.Course;

import java.util.List;

public class CourseService {

    private static final CourseDAO courseDAO = new CourseDAO();

    public static void addCourse(Course course) { courseDAO.save(course); }

    public static Course getCourseById(int id) { return courseDAO.findById(id); }

    public static List<Course> getCoursesByStudentId(int studentId) { return courseDAO.findByStudentId(studentId); }

    public static List<Course> getCoursesByProfessorId(int professorId) { return courseDAO.findByProfessorId(professorId); }

    public static List<Course> getAllCourses() { return courseDAO.findAll(); }

    public static void updateCourse(Course course) { courseDAO.update(course); }

    public static void deleteCourse(int id) { courseDAO.delete(id); }
}
