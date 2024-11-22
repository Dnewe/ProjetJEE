package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projet.CourseDAO;
import projet.UserDAO;
import projet.Student;

import java.io.IOException;
import java.util.List;

@WebServlet("/GradeEntryServlet")
public class GradeEntryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CourseDAO courseDAO = new CourseDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        List<Student> students = studentDAO.getStudentsByCourse(courseId);

        request.setAttribute("students", students);
        request.setAttribute("selectedCourseId", courseId);
        request.getRequestDispatcher("enterGrades.jsp").forward(request, response);
    }
}
