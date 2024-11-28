package com.jeeproject.controller;

import com.jeeproject.model.User;
import com.jeeproject.service.UserService;
import com.jeeproject.util.HibernateUtil;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "SettingsServlet", urlPatterns = "/settings")
public class SettingsServlet extends HttpServlet {

    String resultPage;
    String errorPage = "/WEB-INF/commonPages/settings.jsp";
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
            case "changePassword":
                resultPage = "settings.jsp";
                errorPage = "/WEB-INF/commonPages/settings.jsp";
                changePassword(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
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
            case "settingsForm":
                resultPage = "/WEB-INF/commonPages/settings.jsp";
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void changePassword(HttpServletRequest request) {
        // get parameters
        String oldPassword = request.getParameter("old-password");
        String newPassword = request.getParameter("new-password");
        // check parameters
        HttpSession session = request.getSession();
        if (session.getAttribute("logged-user")==null) {
            errorMessage = "Vous n'êtes pas connecté.";
            return;
        }
        User user = (User) session.getAttribute("logged-user");
        if (!user.getPassword().equals(oldPassword)) {
            errorMessage = "L'ancien mot de passe est incorrect";
            return;
        }
        if (newPassword.length()<6) {
            errorMessage = "Le nouveau mot de passe doit faire au moins 6 caractères.";
            return;
        }
        // change password
        user.setPassword(newPassword);
        UserService.updateUser(user);
        request.setAttribute("success-message", "Mot de passe modifié avec succès.");
    }


}