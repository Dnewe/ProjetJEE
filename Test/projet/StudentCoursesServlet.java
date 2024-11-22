package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/studentCourses")
public class StudentCoursesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String jdbcURL = "jdbc:mysql://localhost:3306/AcademicAppDB";
    private String jdbcUsername = "root";
    private String jdbcPassword = "cytech0001";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));

        String fetchCoursesSQL = "SELECT course.id, course.name, course.description " +
                "FROM course JOIN enrollment ON course.id = enrollment.course_id " +
                "WHERE enrollment.student_id = ?";

        List<Course> courses = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(fetchCoursesSQL)) {

            preparedStatement.setInt(1, studentId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(rs.getInt("id"), rs.getString("name"), rs.getString("description"), 0));
                }
            }

            request.setAttribute("courses", courses);
            request.getRequestDispatcher("studentCourses.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
