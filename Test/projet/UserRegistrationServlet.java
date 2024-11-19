package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import projet.dao.UserDAO;
import projet.models.User;

@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward vers une JSP contenant le formulaire d'inscription
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les données du formulaire
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // Validation basique des données
        if (email == null || password == null || role == null || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            request.setAttribute("errorMessage", "Tous les champs sont requis !");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Créer un objet User
        User user = new User(email, password, role);

        // Sauvegarder dans la base de données
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.registerUser(user);

        if (success) {
            response.sendRedirect("admindashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "Erreur lors de l'inscription !");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
