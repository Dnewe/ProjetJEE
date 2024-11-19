package projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/AcademicAppDB";
    private String jdbcUsername = "root";
    private String jdbcPassword = "cytech0001";

    private static final String INSERT_USER_SQL = "INSERT INTO user (email, password, role) VALUES (?, ?, ?)";
    private static final String INSERT_STUDENT_SQL = "INSERT INTO student (user_id, last_name, first_name, date_of_birth, contact) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_PROFESSOR_SQL = "INSERT INTO professor (user_id, last_name, first_name, contact) VALUES (?, ?, ?, ?)";

    public boolean registerUser(User user) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {

            PreparedStatement userStatement = connection.prepareStatement(INSERT_USER_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, user.getEmail());
            userStatement.setString(2, user.getPassword());
            userStatement.setString(3, user.getRole());
            userStatement.executeUpdate();

            int userId = 0;
            var generatedKeys = userStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            if ("student".equals(user.getRole()) && user instanceof Student) {
                Student student = (Student) user;
                PreparedStatement studentStatement = connection.prepareStatement(INSERT_STUDENT_SQL);
                studentStatement.setInt(1, userId);
                studentStatement.setString(2, student.getLastName());
                studentStatement.setString(3, student.getFirstName());
                studentStatement.setDate(4, student.getDateOfBirth());
                studentStatement.setString(5, student.getContact());
                studentStatement.executeUpdate();
            } else if ("professor".equals(user.getRole()) && user instanceof Professor) {
                Professor professor = (Professor) user;
                PreparedStatement professorStatement = connection.prepareStatement(INSERT_PROFESSOR_SQL);
                professorStatement.setInt(1, userId);
                professorStatement.setString(2, professor.getLastName());
                professorStatement.setString(3, professor.getFirstName());
                professorStatement.setString(4, professor.getContact());
                professorStatement.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
