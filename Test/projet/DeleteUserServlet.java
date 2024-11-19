package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projet.UserDAO;

import java.io.IOException;

@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        boolean success = userDAO.deleteUser(userId);

        if (success) {
            response.sendRedirect("manageUsers");
        } else {
            request.setAttribute("errorMessage", "Erreur lors de la suppression !");
            request.getRequestDispatcher("manageUsers.jsp").forward(request, response);
        }
    }
}
