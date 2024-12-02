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
import java.util.List;

@WebServlet(name = "UserController", urlPatterns = "/user")
public class UserController extends HttpServlet {

    private String resultPage;
    private String errorPage = ServletUtil.defaultErrorPage;
    private String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            /*case "create":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "user?action=list";
                errorPage = "error.jsp";
                createUser(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;*/
            case "update":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/adminPages/user/userDetails.jsp";
                errorPage = "user?action=list";
                updateUser(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "user?action=list";
                errorPage = "user?action=list";
                deleteUser(request);
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
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/adminPages/user/userDetails.jsp";
                errorPage = "user?action=list";
                viewUser(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/adminPages/user/users.jsp";
                viewUsers(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "createForm":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/user/register.jsp";
                request.setAttribute("selectedRole", request.getParameter("role"));
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
            case "updateForm":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/user/updateUser.jsp";
                errorPage = "user?action=list";
                setUpdateFormParam(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    /*private void createUser(HttpServletRequest request) {
        // get parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        // verify parameters
        if (!role.equals("admin") && !role.equals("professor") && !role.equals("student")) {
            errorMessage = "Role invalide";
            return;
        }
        if (!EmailUtil.validEmail(email)) {
            errorMessage = "Email invalide";
            return;
        }
        if (!ServletUtil.validString(password)) {
            errorMessage = "Mot de passe invalide";
            return;
        }
        // create user
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        // add user
        UserService.addUser(user);
    }*/


    private void updateUser(HttpServletRequest request) {
        // get parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int userId = TypeUtil.getIntFromString(request.getParameter("user-id"));
        // verify parameters
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // update user
        User user = UserService.getUserById(userId);
        if (EmailUtil.validEmail(email)) { user.setEmail(email); }
        if (ServletUtil.validString(password)) { user.setPassword(password); }
        UserService.updateUser(user);
        request.setAttribute("user", user);
        request.setAttribute("successMessage", "Profil utilisateur modifié avec succès");
    }


    private  void deleteUser(HttpServletRequest request) {
        // get parameters
        int userId = TypeUtil.getIntFromString(request.getParameter("user-id"));
        // verify parameters
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // delete user
        UserService.deleteUser(userId);
        // delete student
        Student student = StudentService.getStudentByUserId(userId);
        if (student != null ) { StudentService.deleteStudent(student.getId()); }
        // delete professor
        Professor professor = ProfessorService.getProfessorByUserId(userId);
        if (professor != null ) { ProfessorService.deleteProfessor(professor.getId()); }
        request.setAttribute("successMessage", "Utilisateur supprimé avec succès");
    }


    private void viewUser(HttpServletRequest request) {
        // get parameters
        int userId = TypeUtil.getIntFromString(request.getParameter("user-id"));
        // verify parameters
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // get user
        User user = UserService.getUserById(userId);
        request.setAttribute("user", user);
        // get associated student or professor
        switch (user.getRole()) {
            case "professor":
                Professor professor = ProfessorService.getProfessorByUserId(userId);
                if (professor!=null) {
                    request.setAttribute("firstName", professor.getFirstName());
                    request.setAttribute("lastName", professor.getLastName());
                }
                break;
            case "student":
                Student student = StudentService.getStudentByUserId(userId);
                if (student!=null) {
                    request.setAttribute("firstName", student.getFirstName());
                    request.setAttribute("lastName", student.getLastName());
                }
                break;
            default:
                request.setAttribute("firstName", "N/A");
                request.setAttribute("lastName", "N/A");
        }
    }

    private void viewUsers(HttpServletRequest request) {
        List<User> users = UserService.getAllUsers();
        request.setAttribute("users", users);
    }

    private void setUpdateFormParam(HttpServletRequest req) {
        int userId = TypeUtil.getIntFromString(req.getParameter("user-id"));
        // verify parameters
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        req.setAttribute("user", UserService.getUserById(userId));
    }
}
