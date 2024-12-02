package com.jeeproject.controller;

import com.jeeproject.model.Enrollment;
import com.jeeproject.model.Student;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.EnrollmentService;
import com.jeeproject.service.StudentService;
import com.jeeproject.util.EmailUtil;
import com.jeeproject.util.ServletUtil;
import com.jeeproject.util.TypeUtil;
import jakarta.mail.MessagingException;
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
    private String errorPage = ServletUtil.defaultErrorPage;
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
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "enrollment?action=list");
                errorPage = ServletUtil.getResultPage(request, ServletUtil.defaultErrorPage);
                createEnrollment(request);
                break;
            case "delete":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "enrollment?action=list");
                errorPage = ServletUtil.getResultPage(request, ServletUtil.defaultErrorPage);
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
                //resultPage = "WEB-INF/views/enrollments.jsp";
                /*listEnrollments(request);*/
                break;
            case "view":
                //resultPage = "WEB-INF/views/studentEnrollments.jsp";
                /*viewStudentEnrollments(request);*/
                break;
            default:
                ServletUtil.invalidAction(request, response);
                return;
        }

        ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
    }

    private void createEnrollment(HttpServletRequest request) {
        Date enrollmentDate = request.getParameter("enrollment-date")!= null ?
                TypeUtil.getDateFromString(request.getParameter("enrollment-date")) : new Date();
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));

        if (enrollmentDate == null) {
            errorMessage = "Date invalide. Utilisez le format yyyy-MM-dd.";
            return;
        }
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "étudiant introuvable.";
            return;
        }
        if (EnrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId) != null) {
            errorMessage = "Inscription déjà existante.";
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(enrollmentDate);
        enrollment.setCourse(CourseService.getCourseById(courseId));
        enrollment.setStudent(StudentService.getStudentById(studentId));

        EnrollmentService.addEnrollment(enrollment);
        request.setAttribute("successMessage", "Inscription créée avec succès");
        notifyStudentEnrollmentChange(StudentService.getStudentById(studentId), "Inscription au cours " + CourseService.getCourseById(courseId).getName());
    }

    private void deleteEnrollment(HttpServletRequest request) {
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // check parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        Enrollment enrollment = EnrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
        if (enrollment == null) {
            errorMessage = "Inscription introuvable.";
            return;
        }
        // delete enrollment
        EnrollmentService.deleteEnrollment(enrollment.getId());
        request.setAttribute("successMessage", "Inscription supprimée avec succès");
        notifyStudentEnrollmentChange(StudentService.getStudentById(studentId), "Désinscription du cours " + CourseService.getCourseById(courseId).getName());
    }

    private void notifyStudentEnrollmentChange(Student student, String changeType) {
        String subject = "Changement dans votre inscription";
        String message = String.format(
                "Bonjour %s,\n\nNous vous informons qu'un changement a été effectué dans votre inscription : %s.\n\nCordialement,\nL'équipe de gestion",
                student.getFirstName(),
                changeType
        );

        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), subject, message);
        } catch (MessagingException e) {
            errorMessage = "échec de l'envoi de l'email à l'étudiant : " + student.getId();
        }
    }
}
