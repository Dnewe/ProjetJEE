package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;
import com.jeeproject.service.*;
import com.jeeproject.util.ServletUtil;
import com.jeeproject.util.TypeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ResultController", urlPatterns = "/result")
public class ResultController extends HttpServlet {

    String resultPage;
    String errorPage;
    String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "create":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "";
                errorPage = "error.jsp";
                createResult(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "/WEB-INF/views/resultDetails.jsp");
                errorPage = "error.jsp";
                updateResult(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "";
                errorPage = "error.jsp";
                deleteResult(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "studentDetails":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "/WEB-INF/professorPages/studentDetails.jsp");
                errorPage = "error.jsp";
                viewStudentCourseResults(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "courseList":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/professorPages/students.jsp";
                errorPage = "error.jsp";
                viewCourseResults(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "studentList":
                if (ServletUtil.notStudent(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/studentPages/results.jsp";
                errorPage = "error.jsp";
                viewStudentResults(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "updateForm":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/professorPages/updateResult.jsp";
                request.setAttribute("result", ResultService.getResultById(TypeUtil.getIntFromString(request.getParameter("result-id"))));
                request.setAttribute("student", StudentService.getStudentById(TypeUtil.getIntFromString(request.getParameter("student-id"))));
                request.setAttribute("course", CourseService.getCourseById(TypeUtil.getIntFromString(request.getParameter("course-id"))));
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    private void createResult(HttpServletRequest request) {
        // get parameters
        double grade = TypeUtil.getDoubleFromString(request.getParameter("grade"));
        int maxScore = TypeUtil.getIntFromString(request.getParameter("max-score"));
        double weight = TypeUtil.getDoubleFromString(request.getParameter("weight"));
        String assessmentName = request.getParameter("assessment-name");
        int enrollmentId = TypeUtil.getIntFromString(request.getParameter("enrollment-id"));
        Date entryDate = new Date();
        // verify parameters
        if (!ServletUtil.validString(assessmentName)) {
            errorMessage = "Nom du devoir invalide.";
            return;
        }
        if (!validGrade(grade, maxScore, weight)) {
            errorMessage = "Note invalide.";
            return;
        }
        if (enrollmentId == -1 || EnrollmentService.getEnrollmentById(enrollmentId) == null) {
            errorMessage = "L'étudiant n'est pas inscrit au cours.";
            return;
        }
        // create result
        Result result = new Result();
        result.setEnrollment(EnrollmentService.getEnrollmentById(enrollmentId));
        result.setGrade(grade);
        result.setMaxScore(maxScore);
        result.setWeight(weight);
        result.setAssessmentName(assessmentName);
        result.setEntryDate(entryDate);
        // add result
        ResultService.addResult(result);
    }


    private void updateResult(HttpServletRequest request) {
        // get parameters
        double grade = TypeUtil.getDoubleFromString(request.getParameter("grade"));
        int maxScore = TypeUtil.getIntFromString(request.getParameter("max-score"));
        double weight = TypeUtil.getDoubleFromString(request.getParameter("weight"));
        int resultId = TypeUtil.getIntFromString(request.getParameter("result-id"));
        String assessmentName = request.getParameter("assessment-name");
        Date entryDate = TypeUtil.getDateFromString(request.getParameter("entry-date"));
        // verify parameters
        if (resultId == -1 || ResultService.getResultById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        // update result
        Result result = ResultService.getResultById(resultId);
        if (maxScore>0 && maxScore<1000) {
            result.setMaxScore(maxScore);
        } else {
            maxScore = result.getMaxScore();
        }
        if (grade>0 && grade<1000 && grade<maxScore) { result.setGrade(grade); }
        if (weight>0 && weight<1000) { result.setWeight(weight); }
        if (ServletUtil.validString(assessmentName)) { result.setAssessmentName(assessmentName);}
        if (entryDate!=null) { result.setEntryDate(entryDate);}
        ResultService.updateResult(result);
        request.setAttribute("result", result);
    }


    private  void deleteResult(HttpServletRequest request) {
        // get parameters
        int resultId = TypeUtil.getIntFromString(request.getParameter("result-id"));
        // verify parameters
        if (resultId == -1 || ResultService.getResultById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        // delete result
        ResultService.deleteResult(resultId);
    }

    private void viewCourseResults(HttpServletRequest request) {
        // get parameters
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get course
        request.setAttribute("course", CourseService.getCourseById(courseId));
        // get results
        Map<Student, List<Result>> resultsByStudent = ResultService.getResultsByCourseIdGroupedByStudent(courseId);
        request.setAttribute("resultsByStudent", resultsByStudent);
        // get average
        Map<Student, Double> averageByStudent = resultsByStudent.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateAverage(entry.getValue())
                ));
        request.setAttribute("averageByStudent", averageByStudent);
    }

    private void viewStudentResults(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // get results
        Map<Course, List<Result>> resultsByCourse = ResultService.getResultsByStudentIdGroupedByCourse(studentId);
        request.setAttribute("resultsByCourse", resultsByCourse);
        // get average
        Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateAverage(entry.getValue())
                ));
        request.setAttribute("averageByCourse", averageByCourse);
    }

    private void viewStudentCourseResults(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (studentId == -1 || CourseService.getCourseById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get results and average
        List<Result> results = ResultService.getResultsByStudentIdAndCourseId(studentId, courseId);
        request.setAttribute("results", results);
        request.setAttribute("average", calculateAverage(results));
        // get course and student
        request.setAttribute("course", CourseService.getCourseById(courseId));
        request.setAttribute("student", StudentService.getStudentById(studentId));
    }
    
    /*public void publishGrade(int studentId, int courseId, double grade) {
        // Logique pour ajouter/modifier une note
        GradeService.addGrade(studentId, courseId, grade);

        // R�cup�rer les informations de l'�tudiant et du cours
        Student student = StudentService.getStudentById(studentId);
        Course course = CourseService.getCourseById(courseId);

        // Notifier l'�tudiant
        notifyStudentGradePublication(student, course.getName(), grade);
    }*/


    private void viewTranscript(HttpServletRequest request) {
        // TODO
    }

    private boolean validGrade(double grade, double maxScore, double weight) {
        return (grade>0 && maxScore>0 && weight>0) && (grade<1000 && maxScore<1000 && weight<1000) && grade <= maxScore;
    }

    private static double calculateAverage(List<Result> results) {
        double totalWeightedGrades = 0.0;
        double totalWeights = 0.0;
        for (Result result : results) {
            totalWeightedGrades += result.getGrade() / result.getMaxScore() * result.getWeight();
            totalWeights += result.getWeight();
        }
        return Math.round((totalWeights == 0.0 ? 0.0 : totalWeightedGrades / totalWeights)*20*100)/100.;
    }
}