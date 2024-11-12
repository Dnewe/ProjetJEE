import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
    private ReportService reportService = new ReportService(); // Service pour générer des rapports

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = request.getParameter("courseId");
        // Générer le rapport et l'envoyer au client
        Report report = reportService.generateReport(courseId);
        request.setAttribute("report", report);
        request.getRequestDispatcher("report.jsp").forward(request, response);
    }
}
