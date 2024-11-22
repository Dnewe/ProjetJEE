package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projet.CourseDAO;
import projet.Course;

import java.io.IOException;

@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "addCourse":
                String courseName = request.getParameter("courseName");
                String description = request.getParameter("description");
                courseDAO.addCourse(new Course(courseName, description));
                break;
            case "assignProfessor":
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                int professorId = Integer.parseInt(request.getParameter("professorId"));
                courseDAO.assignProfessor(courseId, professorId);
                break;
            case "deleteCourse":
                courseId = Integer.parseInt(request.getParameter("courseId"));
                courseDAO.deleteCourse(courseId);
                break;
        }

        response.sendRedirect("manageCourses.jsp");
    }
}
