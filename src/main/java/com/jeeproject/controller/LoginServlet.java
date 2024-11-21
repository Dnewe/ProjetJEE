package com.jeeproject.controller;

import com.jeeproject.model.User;
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
                    break;
                case "teacher":
                    response.sendRedirect("teacherDashboard.jsp");
                    break;
            }
        } else {
            errorPage = "login.jsp";
            errorMessage = "Invalid email or password";
            ServletUtil.forward(request, response, null, errorPage, errorMessage);
        }
    }
}
