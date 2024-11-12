import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/GradeServlet")
public class GradeServlet extends HttpServlet {
    private GradeService gradeService = new GradeService(); // Service pour gérer les notes

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = request.getParameter("courseId");
        
        // Boucle pour enregistrer les notes pour chaque étudiant
        for (String param : request.getParameterMap().keySet()) {
            if (param.startsWith("grade_")) {
                int studentId = Integer.parseInt(param.split("_")[1]);
                int grade = Integer.parseInt(request.getParameter(param));
                gradeService.addGrade(studentId, courseId, grade);
            }
        }

        response.sendRedirect("teacher-dashboard.jsp"); // Rediriger vers le tableau de bord de l'enseignant
    }
}
