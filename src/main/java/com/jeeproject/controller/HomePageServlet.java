package com.jeeproject.controller;

import com.jeeproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "HomePageServlet", urlPatterns = "/home")
public class HomePageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String role = user.getRole();

        switch (role) {
            case "admin":
                request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
                break;
            case "student":
                request.getRequestDispatcher("studentDashboard.jsp").forward(request, response);
            case "professor":
                request.getRequestDispatcher("professorDashboard.jsp").forward(request, response);
            default:
                request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
