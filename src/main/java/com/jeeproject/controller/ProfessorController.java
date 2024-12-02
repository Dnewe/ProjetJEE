package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Professor;
import com.jeeproject.model.User;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.service.StudentService;
import com.jeeproject.service.UserService;
import com.jeeproject.util.ServletUtil;
import com.jeeproject.util.TypeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProfessorController", urlPatterns = "/professor")
public class ProfessorController extends HttpServlet {

    private String resultPage;
    private String errorPage = ServletUtil.defaultErrorPage;
    private String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            System.out.println("NO ACTION (post)");
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            /*case "create":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "professor?action=list";
                errorPage = "error.jsp";
                createProfessor(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;*/
            case "update":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "professor?action=list");
                errorPage = "professor?action=list";
                updateProfessor(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "professor?action=list";
                errorPage = "professor?action=list";
                deleteProfessor(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            /*case "saveGrades":
                resultPage = "professor?action=submitGrades";
                errorPage = "error.jsp";
                saveGrades(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;*/
            default:
                System.out.println("ACTION NOT FOUND PROFESSOR (post) : " + action);
                ServletUtil.invalidAction(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            System.out.println("NO ACTION");
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "details":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/professor/professorDetails.jsp";
                errorPage = "professor?action=list";
                viewProfessor(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/professor/professors.jsp";
                viewProfessors(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "updateForm":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/professor/updateProfessor.jsp";
                request.setAttribute("professor", ProfessorService.getProfessorById(TypeUtil.getIntFromString(request.getParameter("professor-id"))));
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
            /*case "submitGrades":
                resultPage = "WEB-INF/adminPages/professor/gradeSubmission.jsp";
                errorPage = "error.jsp";
                submitGrades(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;*/
            default:
                System.out.println("ACTION NOT FOUND PROFESSOR (get)");
                ServletUtil.invalidAction(request, response);
        }
    }

    /*private void createProfessor(HttpServletRequest request) {
        String lastName = request.getParameter("last-name");
        String firstName = request.getParameter("first-name");
        String contact = request.getParameter("contact");
        int userId = TypeUtil.getIntFromString(request.getParameter("user-id"));

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
            errorMessage = "Le r�le de l'utilisateur ne correspond pas.";
            return;
        }

        Professor professor = new Professor();
        professor.setLastName(lastName);
        professor.setFirstName(firstName);
        professor.setContact(contact);
        professor.setUser(user);

        ProfessorService.addProfessor(professor);
        request.setAttribute("successMessage", "Professeur créé avec succès");
    }*/

    private void updateProfessor(HttpServletRequest request) {
        String lastName = request.getParameter("last-name");
        String firstName = request.getParameter("first-name");
        String contact = request.getParameter("contact");
        int professorId = TypeUtil.getIntFromString(request.getParameter("professor-id"));

        System.out.println(professorId);

        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }

        Professor professor = ProfessorService.getProfessorById(professorId);
        if (ServletUtil.validString(lastName)) professor.setLastName(lastName);
        if (ServletUtil.validString(firstName)) professor.setFirstName(firstName);
        if (ServletUtil.validString(contact)) professor.setContact(contact);

        ProfessorService.updateProfessor(professor);
        request.setAttribute("successMessage", "Professeur modifié avec succès");
    }

    private void deleteProfessor(HttpServletRequest request) {
        int professorId = TypeUtil.getIntFromString(request.getParameter("professor-id"));

        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }

        ProfessorService.deleteProfessor(professorId);
        request.setAttribute("successMessage", "Professeur supprimé avec succès");
    }

    private void viewProfessor(HttpServletRequest request) {
        int professorId = TypeUtil.getIntFromString(request.getParameter("professor-id"));

        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }
        // add professor attribute
        Professor professor = ProfessorService.getProfessorById(professorId);
        request.setAttribute("professor", professor);
        // add assigned courses attribute
        List<Course> assignedCourses = CourseService.getCoursesByProfessorId(professorId);
        request.setAttribute("assignedCourses", assignedCourses);
        // add available courses attribute
        List<Course> availableCourses = CourseService.getAllCourses();
        availableCourses.removeAll(assignedCourses);
        request.setAttribute("availableCourses", availableCourses);
    }

    private void viewProfessors(HttpServletRequest request) {
        List<Professor> professors = ProfessorService.getAllProfessors();
        request.setAttribute("professors", professors);
    }

    private void submitGrades(HttpServletRequest request) {
        int professorId = TypeUtil.getIntFromString((String) request.getSession().getAttribute("professor-id"));
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));

        if (professorId == -1) {
            errorMessage = "Professeur non identifié.";
            return;
        }

        request.setAttribute("courses", CourseService.getCoursesByProfessorId(professorId));

        if (courseId != -1) {
            request.setAttribute("selected-course-id", courseId);
            request.setAttribute("students", StudentService.getStudentsByCourseId(courseId));
        }
    }

    private void saveGrades(HttpServletRequest request) {
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));

        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }

        Map<String, String[]> gradeParams = request.getParameterMap();

        for (String paramName : gradeParams.keySet()) {
            if (paramName.startsWith("grades[")) {
                int studentId = Integer.parseInt(paramName.substring(7, paramName.length() - 1));
                double grade = Double.parseDouble(gradeParams.get(paramName)[0]);

                //ProfessorService.updateStudentGrade(courseId, studentId, grade);
            }
        }
    }
}
