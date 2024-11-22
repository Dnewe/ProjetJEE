package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projet.UserDAO;
import projet.User;
import projet.Student;
import projet.Professor;

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
        String dobStr = request.getParameter("dob");

        User user = null;

        if ("admin".equals(role)) {
            user = new User(id, email, role);
        } else if ("student".equals(role)) {
            java.sql.Date dob = java.sql.Date.valueOf(dobStr); // Convertir la date au bon format
            user = new Student(email, "", prenom, nom, dob, contact); // Le mot de passe est vide ici, vous pouvez l'ajouter si nécessaire
            user.setId(id); // Ajouter l'ID de l'utilisateur
        } else if ("professor".equals(role)) {
            // Pour un enseignant, on utilise le constructeur de Professor
            user = new Professor(email, "", prenom, nom, contact); // Le mot de passe est vide ici, vous pouvez l'ajouter si nécessaire
            user.setId(id); // Ajouter l'ID de l'utilisateur
        }

        // Mise à jour de l'utilisateur dans la base de données
        boolean success = userDAO.updateUser(user);

        // Si la mise à jour est réussie, redirection vers la gestion des utilisateurs
        if (success) {
            response.sendRedirect("manageUsers");
        } else {
            // En cas d'erreur, affichage d'un message d'erreur sur la page de modification
            request.setAttribute("errorMessage", "Erreur lors de la mise à jour de l'utilisateur !");
            request.getRequestDispatcher("editUser.jsp").forward(request, response);
        }
    }
}
