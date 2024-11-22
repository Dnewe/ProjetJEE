package projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import projet.User;
import projet.Professor;
import projet.Grade;

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
    
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");

                userList.add(new User(id, email, role));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public boolean deleteUser(int userId) {
        String query = "DELETE FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Professor> getAllProfessors() {
        List<Professor> professors = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE role = 'professor'";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                professors.add(new Professor(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("lastName"),
                    rs.getString("firstName"),
                    rs.getString("contact")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professors;
    }

    public String getProfessorNameById(int id) {
        String sql = "SELECT lastName, firstName FROM user WHERE id = ? AND role = 'professor'";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("lastName") + " " + rs.getString("firstName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static final String SELECT_STUDENTS_IN_COURSE = "SELECT u.id, u.email, u.firstName, u.lastName, r.grade, r.date "
            + "FROM users u "
            + "JOIN enrollment e ON u.id = e.student_id "
            + "JOIN result r ON e.id = r.enrollment_id "
            + "WHERE e.course_id = ? AND u.role = 'student'";

    public List<Grade> getStudentsResultsInCourse(int courseId) {
        List<Grade> grades = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENTS_IN_COURSE)) {

            preparedStatement.setInt(1, courseId);
            ResultSet gradeSet = preparedStatement.executeQuery();

            while (gradeSet.next()) {
                int enrollmentId = gradeSet.getInt("enrollment_id");
                String studentFirstName = gradeSet.getString("firstName");
                String studentLastName = gradeSet.getString("lastName");
                double grade = gradeSet.getDouble("grade");
                Date date = gradeSet.getDate("date");

                Grade grade = new Grade(enrollmentId, grade, date);
                grade.setStudentFirstName(studentFirstName);
                grade.setStudentLastName(studentLastName);
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

}
