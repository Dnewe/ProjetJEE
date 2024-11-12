import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/TeacherServlet")
public class TeacherServlet extends HttpServlet {
    private TeacherService teacherService = new TeacherService(); // Service pour gérer les enseignants

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addTeacher(request, response);
        } else if ("delete".equals(action)) {
            deleteTeacher(request, response);
        } else if ("edit".equals(action)) {
            editTeacher(request, response);
        }
    }

    private void addTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        Teacher teacher = new Teacher(firstName, lastName);
        teacherService.addTeacher(teacher);

        response.sendRedirect("manage-teachers.jsp");
    }

    private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        teacherService.deleteTeacher(id);
        response.sendRedirect("manage-teachers.jsp");
    }

    private void editTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        Teacher teacher = new Teacher(id, firstName, lastName);
        teacherService.updateTeacher(teacher);

        response.sendRedirect("manage-teachers.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Teacher> teachers = teacherService.getAllTeachers();
        request.setAttribute("teachers", teachers);
        request.getRequestDispatcher("manage-teachers.jsp").forward(request, response);
    }
}
