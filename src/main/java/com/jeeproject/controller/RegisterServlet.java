package com.jeeproject.controller;

import com.jeeproject.model.Professor;
import com.jeeproject.model.Student;
import com.jeeproject.model.User;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.service.StudentService;
import com.jeeproject.service.UserService;
import com.jeeproject.util.EmailUtil;
import com.jeeproject.util.ServletUtil;
import com.jeeproject.util.TypeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    String resultPage;
    String errorPage;
    String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        resultPage = "user?action=list";
        errorPage = "error.jsp";
        register(request);
        ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
    }


    private void register(HttpServletRequest request) {
        // get common parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        // get professor parameters
        String professorLastName = request.getParameter("professor-last-name");
        String professorFirstName = request.getParameter("professor-first-name");
        String professorContact = request.getParameter("professor-contact");
        // get student parameters
        String studentLastName = request.getParameter("student-last-name");
        String studentFirstName = request.getParameter("student-first-name");
        String studentContact = request.getParameter("student-contact");
        Date dateOfBirth = TypeUtil.getDateFromString(request.getParameter("student-date-of-birth"));
        // verify parameters
        boolean valid = false;
        switch (role) {
            case "student":
                valid = validUserParameters(email, password, role) && validStudentParameters(studentFirstName, studentLastName, studentContact, dateOfBirth);
                break;
            case "professor":
                valid = validUserParameters(email, password, role) && validProfessorParameters(professorFirstName, professorLastName, professorContact);
                break;
            case "admin":
                valid = validUserParameters(email, password, role);
                break;
        }
        if (!valid) { return;}
        // create user
        User user = createUser(email, password, role);
        UserService.addUser(user);

        // create professor/student
        switch (role) {
            case "student":
                StudentService.addStudent(createStudent(user, studentFirstName, studentLastName, studentContact, dateOfBirth));
                break;
            case "professor":
                ProfessorService.addProfessor(createProfessor(user, professorFirstName, professorLastName, professorContact));
                break;
        }
    }


    private User createUser(String email, String password, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    private Student createStudent(User user, String firstName, String lastName, String contact, Date dateOfBirth) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setContact(contact);
        student.setDateOfBirth(dateOfBirth);
        student.setUser(user);
        return student;
    }

    private Professor createProfessor(User user, String firstName, String lastName, String contact) {
        Professor professor = new Professor();
        professor.setLastName(lastName);
        professor.setFirstName(firstName);
        professor.setContact(contact);
        professor.setUser(user);
        return professor;
    }


    private boolean validUserParameters(String email, String password, String role) {
        if (!role.equals("admin") && !role.equals("professor") && !role.equals("student")) {
            errorMessage = "Role invalide";
            return false;
        }
        if (!EmailUtil.validEmail(email)) {
            errorMessage = "Email invalide ou déjà utilisé.";
            return false;
        }
        if (!ServletUtil.validString(password)) {
            errorMessage = "Mot de passe invalide";
            return false;
        }
        return true;
    }

    private boolean validStudentParameters(String firstName, String lastName, String contact, Date dateOfBirth) {
        if (dateOfBirth == null) {
            errorMessage = "Date invalide. Utilisez le format yyyy-MM-dd.";
            return false;
        }
        return validCommonParameters(firstName, lastName, contact);
    }

    private boolean validProfessorParameters(String firstName, String lastName, String contact) {
        return validCommonParameters(firstName, lastName, contact);
    }

    private boolean validCommonParameters(String firstName, String lastName, String contact) {
        if (!ServletUtil.validString(lastName)) {
            System.out.println(lastName);
            errorMessage = "Nom invalide.";
            return false;
        }
        if (!ServletUtil.validString(firstName)) {
            errorMessage = "Prénom invalide.";
            return false;
        }
        if (!ServletUtil.validString(contact)) {
            errorMessage = "Contact invalide.";
            return false;
        }
        return true;
    }
}
