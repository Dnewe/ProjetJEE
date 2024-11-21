package com.jeeproject.controller;

import com.jeeproject.model.Enrollment;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.EnrollmentService;
import com.jeeproject.service.StudentService;
import com.jeeproject.service.UserService;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "EnrollmentController", urlPatterns = "/enrollment")
public class EnrollmentController extends HttpServlet {

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
                resultPage = "enrollment?action=list";
                errorPage = "error.jsp";
                createEnrollment(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                resultPage = "";
                errorPage = "error.jsp";
                updateEnrollment(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                resultPage = "enrollment?action=list";
                errorPage = "error.jsp";
                deleteEnrollment(request);
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
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    private void createEnrollment(HttpServletRequest request) {
        // get parameters
        Date enrollmentDate = ServletUtil.getDateFromString(request.getParameter("enrollment_date"));
        int courseId = ServletUtil.getIntFromString(request.getParameter("course_id"));
        int studentId = ServletUtil.getIntFromString(request.getParameter("student_id"));
        // verify parameters
        if (enrollmentDate == null) {
            errorMessage = "Date invalide. Utilisez le format yyyy-MM-dd.";
            return;
        }
        if (courseId == -1 || UserService.getUserById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        if (studentId == -1 || UserService.getUserById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(enrollmentDate);
        enrollment.setCourse(CourseService.getCourseById(courseId));
        enrollment.setStudent(StudentService.getStudentById(studentId));
        // add enrollment
        EnrollmentService.addEnrollment(enrollment);
    }


    private void updateEnrollment(HttpServletRequest request) {
        // get parameters
        int enrollmentId = ServletUtil.getIntFromString(request.getParameter("id"));
        Date enrollmentDate = ServletUtil.getDateFromString(request.getParameter("enrollment_date"));
        // verify parameters
        if (enrollmentId == -1 || EnrollmentService.getEnrollmentById(enrollmentId) == null) {
            errorMessage = "Inscription introuvable.";
            return;
        }
        // update enrollment
        Enrollment enrollment = EnrollmentService.getEnrollmentById(enrollmentId);
        if (enrollmentDate != null) {enrollment.setEnrollmentDate(enrollmentDate);}
        EnrollmentService.updateEnrollment(enrollment);
        request.setAttribute("enrollment", enrollment);
    }


    private  void deleteEnrollment(HttpServletRequest request) {
        // get parameters
        int enrollmentId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (enrollmentId == -1 || EnrollmentService.getEnrollmentById(enrollmentId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // delete enrollment
        EnrollmentService.deleteEnrollment(enrollmentId);
    }
}
