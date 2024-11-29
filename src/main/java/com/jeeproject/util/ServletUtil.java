package com.jeeproject.util;

import com.jeeproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ServletUtil {

    public static String defaultErrorPage = "WEB-INF/commonPages/error.jsp";

    public static String getResultPage(HttpServletRequest req, String defaultForwardPage) {
        return (req.getParameter("result-page") != null) ? req.getParameter("result-page") : defaultForwardPage;
    }

    public static void forward(HttpServletRequest req, HttpServletResponse resp, String forwardPage, String errorPage, String errorMessage) throws ServletException, IOException {
        if (errorMessage==null) {
            req.getRequestDispatcher(forwardPage).forward(req, resp);
        } else {
            req.setAttribute("errorMessage", errorMessage);
            req.getRequestDispatcher(errorPage).forward(req, resp);
        }
    }

    public static void redirect(HttpServletRequest req, HttpServletResponse resp, String redirectPage, String errorPage, String errorMessage) throws ServletException, IOException {
        if (errorMessage==null) {
            resp.sendRedirect(redirectPage);
        } else {
            req.setAttribute("error", errorMessage);
            req.getRequestDispatcher(errorPage).forward(req, resp);
        }
    }

    public static void invalidAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("{\"error\": \"Invalid action\"}");
    }

    public static void unauthorized(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/commonPages/unauthorized.jsp").forward(req, resp);
    }

    public static boolean validString(String str) {
        return (str!=null && !str.isEmpty() && str.length()<100);
    }

    public static boolean notAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        return user == null || !user.getRole().equals("admin");
    }

    public static boolean notProfessor(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        return user == null || !user.getRole().equals("professor");
    }

    public static boolean notStudent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        return user == null || !user.getRole().equals("student");
    }




}
