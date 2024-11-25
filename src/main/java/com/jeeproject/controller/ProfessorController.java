package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Professor;
import com.jeeproject.model.Student;
import com.jeeproject.model.User;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.service.StudentService;
import com.jeeproject.service.UserService;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProfessorController", urlPatterns = "/professor")
public class ProfessorController extends HttpServlet {

    private String resultPage;
    private String errorPage;
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
                resultPage = "professor?action=list";
                errorPage = "error.jsp";
                createProfessor(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                resultPage = "WEB-INF/views/updateProfessor.jsp";
                errorPage = "error.jsp";
                updateProfessor(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                resultPage = "professor?action=list";
                errorPage = "error.jsp";
                deleteProfessor(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "saveGrades":
                resultPage = "professor?action=submitGrades";
                errorPage = "error.jsp";
                saveGrades(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
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
            case "registerForm":
                resultPage = "WEB-INF/views/registerProfessor.jsp";
                errorPage = "error.jsp";
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "details":
                resultPage = "WEB-INF/views/professorDetails.jsp";
                errorPage = "error.jsp";
                viewProfessor(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                resultPage = "WEB-INF/views/professors.jsp";
                errorPage = "error.jsp";
                viewProfessors(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "submitGrades":
                resultPage = "WEB-INF/views/gradeSubmission.jsp";
                errorPage = "error.jsp";
                submitGrades(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void createProfessor(HttpServletRequest request) {
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String contact = request.getParameter("contact");
        int userId = ServletUtil.getIntFromString(request.getParameter("user_id"));

        if (!ServletUtil.validString(lastName)) {
            errorMessage = "Nom invalide.";
            return;
        }
        if (!ServletUtil.validString(firstName)) {
            errorMessage = "Prénom invalide.";
            return;
        }
        if (!ServletUtil.validString(contact)) {
            errorMessage = "Contact invalide.";
            return;
        }
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        if (ProfessorService.getProfessorByUserId(userId) != null || StudentService.getStudentByUserId(userId) != null) {
            errorMessage = "Utilisateur déjà associé à un étudiant ou professeur.";
            return;
        }

        User user = UserService.getUserById(userId);
        if (!"professor".equals(user.getRole())) {
            errorMessage = "Le rôle de l'utilisateur ne correspond pas.";
            return;
        }

        Professor professor = new Professor();
        professor.setLastName(lastName);
        professor.setFirstName(firstName);
        professor.setContact(contact);
        professor.setUser(user);

        ProfessorService.addProfessor(professor);
    }

    private void updateProfessor(HttpServletRequest request) {
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String contact = request.getParameter("contact");
        int professorId = ServletUtil.getIntFromString(request.getParameter("id"));

        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }

        Professor professor = ProfessorService.getProfessorById(professorId);
        if (ServletUtil.validString(lastName)) professor.setLastName(lastName);
        if (ServletUtil.validString(firstName)) professor.setFirstName(firstName);
        if (ServletUtil.validString(contact)) professor.setContact(contact);

        ProfessorService.updateProfessor(professor);
    }

    private void deleteProfessor(HttpServletRequest request) {
        int professorId = ServletUtil.getIntFromString(request.getParameter("id"));

        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }

        ProfessorService.deleteProfessor(professorId);
    }

    private void viewProfessor(HttpServletRequest request) {
        int professorId = ServletUtil.getIntFromString(request.getParameter("id"));

        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }

        Professor professor = ProfessorService.getProfessorById(professorId);
        request.setAttribute("professor", professor);

        List<Course> courses = CourseService.getCoursesByProfessorId(professorId);
        request.setAttribute("courses", courses);
    }

    private void viewProfessors(HttpServletRequest request) {
        List<Professor> professors = ProfessorService.getAllProfessors();
        request.setAttribute("professors", professors);
    }

    private void submitGrades(HttpServletRequest request) {
        int professorId = ServletUtil.getIntFromString((String) request.getSession().getAttribute("professorId"));
        int courseId = ServletUtil.getIntFromString(request.getParameter("course_id"));

        if (professorId == -1) {
            errorMessage = "Professeur non identifié.";
            return;
        }

        request.setAttribute("courses", CourseService.getCoursesByProfessor(professorId));

        if (courseId != -1) {
            request.setAttribute("selectedCourseId", courseId);
            request.setAttribute("students", StudentService.getStudentsByCourse(courseId));
        }
    }

    private void saveGrades(HttpServletRequest request) {
        int courseId = ServletUtil.getIntFromString(request.getParameter("course_id"));

        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }

        Map<String, String[]> gradeParams = request.getParameterMap();

        for (String paramName : gradeParams.keySet()) {
            if (paramName.startsWith("grades[")) {
                int studentId = Integer.parseInt(paramName.substring(7, paramName.length() - 1));
                double grade = Double.parseDouble(gradeParams.get(paramName)[0]);

                ProfessorService.updateStudentGrade(courseId, studentId, grade);
            }
        }
    }
}
