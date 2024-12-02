package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;
import com.jeeproject.service.PerformanceReportService;
import com.jeeproject.service.ResultService;
import com.jeeproject.service.StudentService;
import com.jeeproject.service.TranscriptService;
import com.jeeproject.util.MathUtil;
import com.jeeproject.util.ServletUtil;
import com.jeeproject.util.TypeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "PerformanceReportServlet", urlPatterns = "/performanceReport")
public class PerformanceReportServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = "";
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        } else {
            // Configuration de la réponse HTTP pour le téléchargement
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"rapport_performance_" + studentId + ".pdf\"");

            // Générer le PDF
            try {
                PerformanceReportService.generate(response, StudentService.getStudentById(studentId));
            } catch (Exception e) {
                errorMessage = "Création du pdf impossible.";
            }
        }

        String resultPage = ServletUtil.getResultPage(request, "adminDashboard.jsp");
        String errorPage = ServletUtil.defaultErrorPage;
        ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
