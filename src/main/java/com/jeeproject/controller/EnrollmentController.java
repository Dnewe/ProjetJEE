package com.jeeproject.controller;

import com.jeeproject.model.Enrollment;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.EnrollmentService;
import com.jeeproject.service.StudentService;
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

    private String resultPage;
    private String errorPage = "error.jsp";
    private String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "create":
                resultPage = "enrollment?action=list";
                createEnrollment(request);
                break;
            case "update":
                resultPage = "enrollment?action=list";
                updateEnrollment(request);
                break;
            case "delete":
                resultPage = "enrollment?action=list";
                deleteEnrollment(request);
                break;
            default:
                ServletUtil.invalidAction(request, response);
                return;
        }

        ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "list":
                resultPage = "WEB-INF/views/enrollments.jsp";
                listEnrollments(request);
                break;
            case "view":
                resultPage = "WEB-INF/views/studentEnrollments.jsp";
                viewStudentEnrollments(request);
                break;
            default:
                ServletUtil.invalidAction(request, response);
                return;
        }

        ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
    }

    private void createEnrollment(HttpServletRequest request) {
        Date enrollmentDate = ServletUtil.getDateFromString(request.getParameter("enrollment_date"));
        int courseId = ServletUtil.getIntFromString(request.getParameter("course_id"));
        int studentId = ServletUtil.getIntFromString(request.getParameter("student_id"));

        if (enrollmentDate == null) {
            errorMessage = "Date invalide. Utilisez le format yyyy-MM-dd.";
            return;
        }

        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }

        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Étudiant introuvable.";
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(enrollmentDate);
        enrollment.setCourse(CourseService.getCourseById(courseId));
        enrollment.setStudent(StudentService.getStudentById(studentId));

        EnrollmentService.addEnrollment(enrollment);
    }

    private void updateEnrollment(HttpServletRequest request) {
        int enrollmentId = ServletUtil.getIntFromString(request.getParameter("id"));
        Date enrollmentDate = ServletUtil.getDateFromString(request.getParameter("enrollment_date"));

        Enrollment enrollment = EnrollmentService.getEnrollmentById(enrollmentId);
        if (enrollment == null) {
            errorMessage = "Inscription introuvable.";
            return;
        }

        if (enrollmentDate != null) {
            enrollment.setEnrollmentDate(enrollmentDate);
        }

        EnrollmentService.updateEnrollment(enrollment);
    }

    private void deleteEnrollment(HttpServletRequest request) {
        int enrollmentId = ServletUtil.getIntFromString(request.getParameter("id"));

        if (enrollmentId == -1 || EnrollmentService.getEnrollmentById(enrollmentId) == null) {
            errorMessage = "Inscription introuvable.";
            return;
        }

        EnrollmentService.deleteEnrollment(enrollmentId);
    }

    private void listEnrollments(HttpServletRequest request) {
        request.setAttribute("enrollments", EnrollmentService.getAllEnrollments());
    }

    private void viewStudentEnrollments(HttpServletRequest request) {
        int studentId = ServletUtil.getIntFromString(request.getParameter("studentId"));

        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Étudiant introuvable.";
            return;
        }

        request.setAttribute("student", StudentService.getStudentById(studentId));
        request.setAttribute("enrolledCourses", EnrollmentService.getCoursesByStudent(studentId));
        request.setAttribute("availableCourses", CourseService.getCoursesNotEnrolledByStudent(studentId));
    }
}
