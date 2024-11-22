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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/allStudentsCourses")
public class AllStudentsCoursesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String jdbcURL = "jdbc:mysql://localhost:3306/AcademicAppDB";
    private String jdbcUsername = "root";
    private String jdbcPassword = "cytech0001";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fetchSQL = "SELECT student.id AS studentId, student.first_name, student.last_name, course.name " +
                "FROM student " +
                "JOIN enrollment ON student.id = enrollment.student_id " +
                "JOIN course ON enrollment.course_id = course.id";

        Map<Student, List<String>> studentCoursesMap = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(fetchSQL);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(rs.getInt("studentId"), null, null, rs.getString("first_name"), rs.getString("last_name"));
                String courseName = rs.getString("name");

                studentCoursesMap.putIfAbsent(student, new ArrayList<>());
                studentCoursesMap.get(student).add(courseName);
            }

            request.setAttribute("studentCoursesMap", studentCoursesMap);
            request.getRequestDispatcher("allStudentsCourses.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
