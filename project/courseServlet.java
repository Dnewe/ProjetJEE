import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {
    private CourseService courseService = new CourseService(); // Service pour gérer les cours

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addCourse(request, response);
        } else if ("delete".equals(action)) {
            deleteCourse(request, response);
        } else if ("edit".equals(action)) {
            editCourse(request, response);
        }
    }

    private void addCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String courseName = request.getParameter("courseName");

        Course course = new Course(courseName);
        courseService.addCourse(course);

        response.sendRedirect("manage-courses.jsp");
    }

    private void deleteCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        courseService.deleteCourse(id);
        response.sendRedirect("manage-courses.jsp");
    }

    private void editCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String courseName = request.getParameter("courseName");

        Course course = new Course(id, courseName);
        courseService.updateCourse(course);

        response.sendRedirect("manage-courses.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> courses = courseService.getAllCourses();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("manage-courses.jsp").forward(request, response);
    }
}
