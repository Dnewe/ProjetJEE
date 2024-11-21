package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Professor;
import com.jeeproject.model.User;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.service.UserService;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ProfessorController", urlPatterns = "/professor")
public class ProfessorController extends HttpServlet {

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
                resultPage = "professor?action=list";
                errorPage = "error.jsp";
                createProfessor(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                resultPage = "WEB-INF/views/professorDetails.jsp";
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
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void createProfessor(HttpServletRequest request) {
        // get parameters
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String contact = request.getParameter("contact");
        int userId = ServletUtil.getIntFromString(request.getParameter("user_id"));
        // verify parameters
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
            errorMessage = "Utilisateur introuvable";
            return;
        }
        if (ProfessorService.getProfessorByUserId(userId) != null || ProfessorService.getProfessorByUserId(userId) != null) {
            errorMessage = "Utilisateur déjà associé à un étudiant ou professeur.";
            return;
        }
        // verify user role
        User user = UserService.getUserById(userId);
        if (!user.getRole().equals("professor")) {
            errorMessage = "Le rôle de l'utilisateur ne correspond pas.";
            return;
        }
        // create professor
        Professor professor = new Professor();
        professor.setLastName(lastName);
        professor.setFirstName(firstName);
        professor.setContact(contact);
        professor.setUser(UserService.getUserById(userId));
        // add professor
        ProfessorService.addProfessor(professor);
    }

    private void updateProfessor(HttpServletRequest request) {
        // get parameters
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String contact = request.getParameter("contact");
        int professorId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // update professor
        Professor professor = ProfessorService.getProfessorById(professorId);
        if (ServletUtil.validString(lastName)) { professor.setLastName(lastName); }
        if (ServletUtil.validString(firstName)) { professor.setFirstName(firstName); }
        if (ServletUtil.validString(contact)) { professor.setContact(contact); }
        // apply changes
        ProfessorService.updateProfessor(professor);
        request.setAttribute("professor", professor);
    }

    private  void deleteProfessor(HttpServletRequest request) {
        // get parameters
        int professorId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // delete professor
        ProfessorService.deleteProfessor(professorId);
    }

    private void viewProfessor(HttpServletRequest request) {
        // get parameters
        int professorId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // get professor
        Professor professor = ProfessorService.getProfessorById(professorId);
        request.setAttribute("professor", professor);
        // get professor courses
        List<Course> courses = CourseService.getCoursesByProfessorId(professorId);
        request.setAttribute("courses", courses);
    }

    private void viewProfessors(HttpServletRequest request) {
        List<Professor> professors = ProfessorService.getAllProfessors();
        request.setAttribute("professors", professors);
    }
}
