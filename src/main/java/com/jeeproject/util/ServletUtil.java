package com.jeeproject.util;

import com.jeeproject.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class ServletUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Date minDate = new Date(-30610224000000L);
    private static final Date maxDate = new Date(253400659199000L);

    public static void forward(HttpServletRequest req, HttpServletResponse resp, String forwardPage, String errorPage, String errorMessage) throws ServletException, IOException {
        if (errorMessage==null) {
            req.getRequestDispatcher(forwardPage).forward(req, resp);
        } else {
            req.setAttribute("error", errorMessage);
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

    public static boolean validString(String str) {
        return (str!=null && !str.isEmpty());
    }

    public static boolean validEmail(String email) {
        // check not empty email
        if (email == null) {return false;}
        // check unique email
        if (UserService.getUserByEmail(email) != null) {return false;}
        // check email pattern
        Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static Date getDateFromString(String str) {
        if (str == null) {return null;}
        Date date;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
        if (date.before(minDate) || date.after(maxDate)) {return null;}
        return date;
    }

    public static int getIntFromString(String str) {
        if (str == null) {return -1;}
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static double getDoubleFromString(String str) {
        if (str == null) {return -1;}
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
