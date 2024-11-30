package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;
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

@WebServlet(name = "TranscriptController", urlPatterns = "/transcript")
public class TranscriptServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = "";
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        String comment = request.getParameter("comment");
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        } else if (!ServletUtil.validString(comment)) {
            errorMessage = "Appréciation invalide";
        } else {
            // get grades
            Student student = StudentService.getStudentById(studentId);
            Map<Course, List<Result>> resultsByCourse = ResultService.getResultsByStudentIdGroupedByCourse(studentId);
            Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> MathUtil.calculateAverage(entry.getValue())
                    ));
            // Configuration de la réponse HTTP pour le téléchargement
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"releve_notes_" + studentId + ".pdf\"");

            // Générer le PDF
            try {
                TranscriptService.download(response, student, resultsByCourse, averageByCourse, comment);
            } catch (Exception e) {
                errorMessage = "Création du pdf impossible.";
            }
        }

        String resultPage = ServletUtil.getResultPage(request, "adminDashboard.jsp");
        String errorPage = ServletUtil.defaultErrorPage;
        ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = null;
        if (ServletUtil.notProfessor(request) && ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
        String resultPage = "/WEB-INF/adminPages/student/createTranscript.jsp";

        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
        } else {
            request.setAttribute("student", StudentService.getStudentById(studentId));
            // get results
            Map<Course, List<Result>> resultsByCourse = ResultService.getResultsByStudentIdGroupedByCourse(studentId);
            request.setAttribute("resultsByCourse", resultsByCourse);
            // get average
            Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> MathUtil.calculateAverage(entry.getValue())
                    ));
            request.setAttribute("averageByCourse", averageByCourse);
        }

        ServletUtil.forward(request, response, resultPage, ServletUtil.defaultErrorPage, errorMessage);
    }
}
