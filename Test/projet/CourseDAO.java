package projet;

import projet.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/AcademicAppDB";
    private String jdbcUsername = "root";
    private String jdbcPassword = "cytech0001";

    private static final String INSERT_COURSE_SQL = "INSERT INTO course (name, description) VALUES (?, ?)";
    private static final String SELECT_ALL_COURSES = "SELECT * FROM course";
    private static final String ASSIGN_PROFESSOR_SQL = "UPDATE course SET professor_id = ? WHERE id = ?";
    private static final String DELETE_COURSE_SQL = "DELETE FROM course WHERE id = ?";

    public boolean addCourse(Course course) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURSE_SQL)) {

            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COURSES);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("professor_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public boolean assignProfessor(int courseId, int professorId) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_PROFESSOR_SQL)) {

            preparedStatement.setInt(1, professorId);
            preparedStatement.setInt(2, courseId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COURSE_SQL)) {

            preparedStatement.setInt(1, courseId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
