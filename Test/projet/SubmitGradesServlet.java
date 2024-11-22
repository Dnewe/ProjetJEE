package projet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projet.GradeDAO;

import java.io.IOException;
import java.util.Map;

@WebServlet("/SubmitGradesServlet")
public class SubmitGradesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private GradeDAO gradeDAO = new GradeDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        Map<String, String[]> parameterMap = request.getParameterMap();

        parameterMap.forEach((key, value) -> {
            if (key.startsWith("grades[")) {
                int studentId = Integer.parseInt(key.substring(7, key.length() - 1));
                double grade = Double.parseDouble(value[0]);
                gradeDAO.saveGrade(studentId, courseId, grade);
            }
        });

        response.sendRedirect("professorHome.jsp?success=true");
    }
}
