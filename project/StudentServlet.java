import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    private StudentService studentService = new StudentService(); // Service pour gérer les étudiants

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addStudent(request, response);
        } else if ("delete".equals(action)) {
            deleteStudent(request, response);
        } else if ("edit".equals(action)) {
            editStudent(request, response);
        }
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String dob = request.getParameter("dob");
        String contact = request.getParameter("contact");

        Student student = new Student(firstName, lastName, dob, contact);
        studentService.addStudent(student);

        response.sendRedirect("manage-students.jsp"); // Redirige vers la page de gestion des étudiants
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        studentService.deleteStudent(id);
        response.sendRedirect("manage-students.jsp");
    }

    private void editStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String dob = request.getParameter("dob");
        String contact = request.getParameter("contact");

        Student student = new Student(id, firstName, lastName, dob, contact);
        studentService.updateStudent(student);

        response.sendRedirect("manage-students.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Student> students = studentService.getAllStudents();
        request.setAttribute("students", students);
        request.getRequestDispatcher("manage-students.jsp").forward(request, response);
    }
}
