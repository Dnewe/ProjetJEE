package projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GradeDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/AcademicAppDB";
    private String jdbcUsername = "root";
    private String jdbcPassword = "cytech0001";

    private static final String INSERT_GRADE_SQL = "INSERT INTO grades (student_id, course_id, grade) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE grade = ?";

    public void saveGrade(int studentId, int courseId, double grade) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GRADE_SQL)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.setDouble(3, grade);
            preparedStatement.setDouble(4, grade);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
