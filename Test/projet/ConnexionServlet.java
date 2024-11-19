package projet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ConnexionServlet")
public class ConnexionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Connection getConnection() throws Exception {
        String jdbcURL = "jdbc:mysql://localhost:3306/AcademicAppDB";
        String dbUser = "root";
        String dbPassword = "cytech0001";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection connection = getConnection()) {
            String sql = "SELECT id, role FROM user WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String role = resultSet.getString("role");

                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("role", role);

                switch (role) {
                    case "student":
                        response.sendRedirect("studentHome.jsp");
                        break;
                    case "professor":
                        response.sendRedirect("professorHome.jsp");
                        break;
                    case "admin":
                        response.sendRedirect("adminDashboard.jsp");
                        break;
                    default:
                        response.sendRedirect("login.jsp?error=invalidRole");
                        break;
                }
            } else {
                response.sendRedirect("login.jsp?error=invalidCredentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=serverError");
        }
    }
}
