package com.jeeproject.controller;

import com.jeeproject.model.User;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.service.StudentService;
import com.jeeproject.service.UserService;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    String errorPage;
    String errorMessage;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        errorMessage = null;
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = UserService.authenticate(email, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);
            switch (user.getRole()) {
                case "admin":
                    response.sendRedirect("adminDashboard.jsp");
                    break;
                case "student":
                    response.sendRedirect("studentDashboard.jsp");
                    session.setAttribute("loggedStudent", StudentService.getStudentByUserId(user.getId()));
                    break;
                case "professor":
                    response.sendRedirect("professorDashboard.jsp");
                    session.setAttribute("loggedProfessor", ProfessorService.getProfessorByUserId(user.getId()));
                    break;
            }
        } else {
            errorPage = "login.jsp";
            errorMessage = "Email et/ou mot de passe invalide(s).";
            ServletUtil.forward(request, response, null, errorPage, errorMessage);
        }
    }
}
