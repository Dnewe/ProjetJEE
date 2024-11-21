package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.EnrollmentService;
import com.jeeproject.service.ResultService;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
                resultPage = "";
                errorPage = "error.jsp";
                createResult(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                resultPage = "/WEB-INF/views/resultDetails.jsp";
                errorPage = "error.jsp";
                updateResult(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
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
            case "details":
                resultPage = "/WEB-INF/views/resultDetails.jsp";
                errorPage = "error.jsp";
                viewResult(request);
                ServletUtil.invalidAction(request, response);
                break;
            case "course_list":
                resultPage = "/WEB-INF/views/results.jsp";
                errorPage = "error.jsp";
                viewCourseResults(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "student_list":
                resultPage = "/WEB-INF/views/results.jsp";
                errorPage = "error.jsp";
                viewStudentResults(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "transcript":
                resultPage = "";
                errorPage = "error.jsp";
                viewTranscript(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    private void createResult(HttpServletRequest request) {
        // get parameters
        double grade = ServletUtil.getDoubleFromString(request.getParameter("grade"));
        double maxScore = ServletUtil.getDoubleFromString(request.getParameter("max_score"));
        double weight = ServletUtil.getDoubleFromString(request.getParameter("weight"));
        int enrollmentId = ServletUtil.getIntFromString(request.getParameter("enrollment_id"));
        Date entryDate = ServletUtil.getDateFromString(request.getParameter("entry_date"));
        // verify parameters
        if (!validGrade(grade, maxScore, weight)) {
            errorMessage = "Note invalide.";
            return;
        }
        if (entryDate == null) {
            errorMessage = "Date invalide. Utilisez le format yyyy-MM-dd.";
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
        // add result
        ResultService.addResult(result);
    }


    private void updateResult(HttpServletRequest request) {
        // get parameters
        double grade = ServletUtil.getDoubleFromString(request.getParameter("grade"));
        double maxScore = ServletUtil.getDoubleFromString(request.getParameter("max_score"));
        double weight = ServletUtil.getDoubleFromString(request.getParameter("weight"));
        int resultId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (resultId == -1 || ResultService.getResultById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        // update result
        Result result = ResultService.getResultById(resultId);
        if (maxScore>0 && maxScore<1000) { result.setMaxScore(maxScore); }
        if (grade>0 && grade<1000 && grade<result.getMaxScore()) { result.setGrade(grade); }
        if (weight>0 && weight<1000) { result.setWeight(weight); }
        ResultService.updateResult(result);
        request.setAttribute("result", result);
    }


    private  void deleteResult(HttpServletRequest request) {
        // get parameters
        int resultId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (resultId == -1 || ResultService.getResultById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        // delete result
        ResultService.deleteResult(resultId);
    }

    private void viewResult(HttpServletRequest request) {
        // get parameters
        int resultId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (resultId == -1 || CourseService.getCourseById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        // get result
        Result result = ResultService.getResultById(resultId);
        request.setAttribute("result", result);
    }

    private void viewCourseResults(HttpServletRequest request) {
        // get parameters
        int courseId = ServletUtil.getIntFromString(request.getParameter("course_id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get results
        Map<Student, List<Result>> resultsByStudent = ResultService.getResultsByCourseIdGroupedByStudent(courseId);
        request.setAttribute("resultsByStudent", resultsByStudent);
    }

    private void viewStudentResults(HttpServletRequest request) {
        // get parameters
        int studentId = ServletUtil.getIntFromString(request.getParameter("student_id"));
        // verify parameters
        if (studentId == -1 || CourseService.getCourseById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // get results
        Map<Course, List<Result>> resultsByCourse = ResultService.getResultsByStudentIdGroupedByCourse(studentId);
        request.setAttribute("resultsByCourse", resultsByCourse);
    }

    private void viewTranscript(HttpServletRequest request) {
        // TODO
    }

    private boolean validGrade(double grade, double maxScore, double weight) {
        return (grade>0 && maxScore>0 && weight>0) && (grade<1000 && maxScore<1000 && weight<1000) && grade <= maxScore;
    }
}
