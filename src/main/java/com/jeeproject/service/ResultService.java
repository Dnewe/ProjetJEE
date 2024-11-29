package com.jeeproject.service;

import com.jeeproject.dao.ResultDAO;
import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultService {

    private static final ResultDAO resultDAO = new ResultDAO();

    public static void addResult(Result result) {
        resultDAO.save(result);
    }

    public static Result getResultById(int id) {
        return resultDAO.findById(id);
    }

    public static Map<Course, List<Result>> getResultsByStudentIdGroupedByCourse(int studentId) {
        // get results with courses
        List<Object[]> resultsWithCourses = resultDAO.findResultsWithCourseByStudentId(studentId);

        Map<Course, List<Result>> resultsByCourse = new HashMap<>();
        for (Object[] resultAndCourse : resultsWithCourses) {
            Result result = (Result) resultAndCourse[0];
            Course course = (Course) resultAndCourse[1];

            // Group by course name
            resultsByCourse.computeIfAbsent(course, k -> new ArrayList<>()).add(result);
        }
        return resultsByCourse;
    }

    public static Map<Student, List<Result>> getResultsByCourseIdGroupedByStudent(int courseId) {
        // get results with courses
        List<Object[]> resultsWithStudents = resultDAO.findResultsWithStudentByCourseId(courseId);

        Map<Student, List<Result>> resultsByStudent = new HashMap<>();
        for (Object[] resultAndStudent : resultsWithStudents) {
            Result result = (Result) resultAndStudent[0];
            Student student = (Student) resultAndStudent[1];

            // Group by course name
            resultsByStudent.computeIfAbsent(student, k -> new ArrayList<>()).add(result);
        }
        return resultsByStudent;
    }

    public static List<Result> getResultsByStudentIdAndCourseId(int studentId, int courseId) {
        return resultDAO.findByStudentIdAndCourseId(studentId, courseId);
    }

    public static void updateResult(Result result) {
        resultDAO.update(result);
    }

    public static void deleteResult(int id) {
        resultDAO.delete(id);
    }
}
