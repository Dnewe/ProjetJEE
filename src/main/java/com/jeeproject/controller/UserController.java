package com.jeeproject.controller;

import com.jeeproject.model.User;
import com.jeeproject.service.UserService;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserController", urlPatterns = "/user")
public class UserController extends HttpServlet {

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
                resultPage = "user?action=list";
                errorPage = "error.jsp";
                createUser(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                resultPage = "/WEB-INF/views/userDetails.jsp";
                errorPage = "error.jsp";
                updateUser(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                resultPage = "user?action=list";
                errorPage = "error.jsp";
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
                resultPage = "/WEB-INF/views/userDetails.jsp";
                errorPage = "error.jsp";
                viewUser(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                resultPage = "/WEB-INF/views/users.jsp";
                errorPage = "error.jsp";
                viewUsers(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    private void createUser(HttpServletRequest request) {
        // get parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        // verify parameters
        if (!role.equals("admin") && !role.equals("professor") && !role.equals("student")) {
            errorMessage = "Role invalide";
            return;
        }
        if (!ServletUtil.validEmail(email)) {
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
    }


    private void updateUser(HttpServletRequest request) {
        // get parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int userId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // update user
        User user = UserService.getUserById(userId);
        if (ServletUtil.validEmail(email)) { user.setEmail(email); }
        if (ServletUtil.validString(password)) { user.setPassword(password); }
        UserService.updateUser(user);
        request.setAttribute("user", user);
    }


    private  void deleteUser(HttpServletRequest request) {
        // get parameters
        int userId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // delete user
        UserService.deleteUser(userId);
    }


    private void viewUser(HttpServletRequest request) {
        // get parameters
        int userId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // get user
        User user = UserService.getUserById(userId);
        request.setAttribute("user", user);
    }

    private void viewUsers(HttpServletRequest request) {
        List<User> users = UserService.getAllUsers();
        request.setAttribute("users", users);
    }
}
