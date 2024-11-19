package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

import projet.UserDAO;
import projet.User;
import projet.Student;
import projet.Professor;

@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (email == null || password == null || role == null || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            request.setAttribute("errorMessage", "Tous les champs sont requis !");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        User user;
        UserDAO userDAO = new UserDAO();

        try {
            switch (role) {
                case "student":
                    String lastName = request.getParameter("lastName");
                    String firstName = request.getParameter("firstName");
                    String contact = request.getParameter("contact");
                    Date dateOfBirth = Date.valueOf(request.getParameter("dateOfBirth"));

                    if (lastName == null || firstName == null || contact == null || dateOfBirth == null || 
                        lastName.isEmpty() || firstName.isEmpty() || contact.isEmpty()) {
                        throw new IllegalArgumentException("Tous les champs étudiants sont requis !");
                    }

                    user = new Student(email, password, lastName, firstName, dateOfBirth, contact);
                    break;

                case "professor":
                    lastName = request.getParameter("lastName");
                    firstName = request.getParameter("firstName");
                    contact = request.getParameter("contact");

                    if (lastName == null || firstName == null || contact == null || 
                        lastName.isEmpty() || firstName.isEmpty() || contact.isEmpty()) {
                        throw new IllegalArgumentException("Tous les champs enseignants sont requis !");
                    }

                    user = new Professor(email, password, lastName, firstName, contact);
                    break;

                case "admin":
                    user = new User(email, password, role);
                    break;

                default:
                    throw new IllegalArgumentException("Rôle non valide !");
            }

            boolean success = userDAO.registerUser(user);

            if (success) {
                response.sendRedirect("admindashboard.jsp");
            } else {
                request.setAttribute("errorMessage", "Erreur lors de l'inscription !");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
