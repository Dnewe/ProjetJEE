package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projet.UserDAO;
import projet.User;

import java.io.IOException;

@WebServlet("/updateUser")
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String prenom = request.getParameter("prenom");
        String nom = request.getParameter("nom");
        String contact = request.getParameter("contact");
        String dob = request.getParameter("dob");

        // Créez l'objet utilisateur en fonction des informations fournies
        User user = new User(id, email, role, prenom, nom, contact, dob);

        boolean success = userDAO.updateUser(user);

        if (success) {
            response.sendRedirect("manageUsers");
        } else {
            request.setAttribute("errorMessage", "Erreur lors de la mise à jour de l'utilisateur !");
            request.getRequestDispatcher("editUser.jsp").forward(request, response);
        }
    }
}
