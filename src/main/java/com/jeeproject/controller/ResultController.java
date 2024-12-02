package com.jeeproject.controller;

import com.jeeproject.model.*;
import com.jeeproject.service.*;
import com.jeeproject.util.EmailUtil;
import com.jeeproject.util.MathUtil;
import com.jeeproject.util.ServletUtil;
import com.jeeproject.util.TypeUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ResultController", urlPatterns = "/result")
public class ResultController extends HttpServlet {

    private String resultPage;
    private String errorPage = ServletUtil.defaultErrorPage;
    private String errorMessage = "NO MESSAGE";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "create":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=professorList&professor-id=" + ((Professor)request.getSession().getAttribute("loggedProfessor")).getId());
                errorPage = ServletUtil.getResultPage(request, ServletUtil.defaultErrorPage);
                createResult(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "createMultiple":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=professorList&professor-id=" + ((Professor)request.getSession().getAttribute("loggedProfessor")).getId());
                errorPage = ServletUtil.getResultPage(request, ServletUtil.defaultErrorPage);
                createResults(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=professorList&professor-id=" + ((Professor)request.getSession().getAttribute("loggedProfessor")).getId());
                errorPage = ServletUtil.getResultPage(request, ServletUtil.defaultErrorPage);
                updateResult(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=professorList&professor-id=" + ((Professor)request.getSession().getAttribute("loggedProfessor")).getId());
                errorPage = ServletUtil.getResultPage(request, ServletUtil.defaultErrorPage);
                deleteResult(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "studentDetails":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "/WEB-INF/professorPages/studentDetails.jsp");
                viewStudentCourseResults(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "studentList":
                if (ServletUtil.notStudent(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/studentPages/results.jsp";
                viewStudentResults(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "createForm":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/professorPages/gradeStudent.jsp";
                addCreateFormParams(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "createMultipleForm":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/professorPages/gradeStudents.jsp";
                addCreateMultipleFormParams(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "updateForm":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/professorPages/updateResult.jsp";
                addUpdateFormParams(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }


    private void createResult(HttpServletRequest request) {
        // get parameters
        double grade = TypeUtil.getDoubleFromString(request.getParameter("grade"));
        int maxScore = TypeUtil.getIntFromString(request.getParameter("max-score"));
        double weight = TypeUtil.getDoubleFromString(request.getParameter("weight"));
        String assessmentName = request.getParameter("assessment-name");
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        Enrollment enrollment = EnrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
        Date entryDate = new Date();
        // verify parameters
        if (!ServletUtil.validString(assessmentName)) {
            errorMessage = "Nom du devoir invalide.";
            return;
        }
        if (!validGrade(grade, maxScore, weight)) {
            errorMessage = "Note invalide.";
            return;
        }
        if (enrollment == null) {
            errorMessage = "L'étudiant n'est pas inscrit au cours.";
            return;
        }
        // create result
        Result result = new Result();
        result.setEnrollment(enrollment);
        result.setGrade(grade);
        result.setMaxScore(maxScore);
        result.setWeight(weight);
        result.setAssessmentName(assessmentName);
        result.setEntryDate(entryDate);
        // add result
        ResultService.addResult(result);
        //notifyStudentGradePublication(StudentService.getStudentById(studentId), CourseService.getCourseById(courseId));
        request.setAttribute("successMessage", "Note enregistée avec succès");
        notifyStudentGradePublication(StudentService.getStudentById(studentId), CourseService.getCourseById(courseId));
    }

    private void createResults(HttpServletRequest request) {
        // get global parameters
        String assessmentName = request.getParameter("assessment-name");
        int maxScore = TypeUtil.getIntFromString(request.getParameter("max-score"));
        double weight = TypeUtil.getDoubleFromString(request.getParameter("weight"));
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        Date entryDate = new Date();
        // check parameters
        if (!ServletUtil.validString(assessmentName)) {
            errorMessage = "Nom du devoir invalide.";
            return;
        }
        if (maxScore <= 0 || weight <= 0 || maxScore>1000 || weight>1000) {
            errorMessage = "Valeurs globales invalides.";
            return;
        }
        // get student grades
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.startsWith("grades[")) {
                // get student id
                String studentIdStr = paramName.substring(7, paramName.length() - 1);
                int studentId = TypeUtil.getIntFromString(studentIdStr);
                // get grade
                String gradeStr = request.getParameter(paramName);
                if (ServletUtil.validString(gradeStr)) {
                    double grade = TypeUtil.getDoubleFromString(gradeStr);
                    // check grade
                    if (!validGrade(grade, maxScore, weight)) {
                        continue; // Ignore this grade
                    }
                    // get enrollment
                    Enrollment enrollment = EnrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
                    if (enrollment == null) {
                        continue; // Ignorer this grade
                    }

                    // create result
                    Result result = new Result();
                    result.setEnrollment(enrollment);
                    result.setGrade(grade);
                    result.setMaxScore(maxScore);
                    result.setWeight(weight);
                    result.setAssessmentName(assessmentName);
                    result.setEntryDate(entryDate);
                    // add result
                    ResultService.addResult(result);
                    notifyStudentGradePublication(StudentService.getStudentById(studentId), CourseService.getCourseById(courseId));
                }
            }
        }
        request.setAttribute("successMessage", "Notes enregistées avec succès");
    }


    private void updateResult(HttpServletRequest request) {
        // get parameters
        double grade = TypeUtil.getDoubleFromString(request.getParameter("grade"));
        int maxScore = TypeUtil.getIntFromString(request.getParameter("max-score"));
        double weight = TypeUtil.getDoubleFromString(request.getParameter("weight"));
        int resultId = TypeUtil.getIntFromString(request.getParameter("result-id"));
        String assessmentName = request.getParameter("assessment-name");
        Date entryDate = TypeUtil.getDateFromString(request.getParameter("entry-date"));
        // verify parameters
        if (resultId == -1 || ResultService.getResultById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        // update result
        Result result = ResultService.getResultById(resultId);
        if (maxScore>0 && maxScore<1000) {
            result.setMaxScore(maxScore);
        } else {
            maxScore = result.getMaxScore();
        }
        if (grade>0 && grade<1000 && grade<maxScore) { result.setGrade(grade); }
        if (weight>0 && weight<1000) { result.setWeight(weight); }
        if (ServletUtil.validString(assessmentName)) { result.setAssessmentName(assessmentName);}
        if (entryDate!=null) { result.setEntryDate(entryDate);}
        ResultService.updateResult(result);
        request.setAttribute("result", result);
        request.setAttribute("successMessage", "Note modifiée avec succès");
    }

    private  void deleteResult(HttpServletRequest request) {
        // get parameters
        int resultId = TypeUtil.getIntFromString(request.getParameter("result-id"));
        // verify parameters
        if (resultId == -1 || ResultService.getResultById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        // delete result
        ResultService.deleteResult(resultId);
        request.setAttribute("successMessage", "Note supprimée avec succès");
    }

    private void viewStudentResults(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // get results
        Map<Course, List<Result>> resultsByCourse = ResultService.getResultsByStudentIdGroupedByCourse(studentId);
        request.setAttribute("resultsByCourse", resultsByCourse);
        // get average
        Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                ));
        request.setAttribute("averageByCourse", averageByCourse);
    }

    private void viewStudentCourseResults(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get results and average
        List<Result> results = ResultService.getResultsByStudentIdAndCourseId(studentId, courseId);
        request.setAttribute("results", results);
        request.setAttribute("average", MathUtil.calculateAverageFromResults(results));
        // get course and student
        request.setAttribute("course", CourseService.getCourseById(courseId));
        request.setAttribute("student", StudentService.getStudentById(studentId));
    }

    private void notifyStudentGradePublication(Student student, Course course) {
        String subject = "Nouvelle note publiée";
        String message = String.format(
                "Bonjour %s,\n\nUne nouvelle note a été publiée pour le cours : %s.\n\nCordialement,\nL'équipe de gestion",
                student.getFirstName(),
                course.getName()
        );

        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), subject, message);
        } catch (MessagingException e) {
            e.printStackTrace();
            errorMessage = "échec de l'envoi de l'email pour la publication de la note.";
        }
    }

    private void addCreateFormParams(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        request.setAttribute("student", StudentService.getStudentById(studentId));
        request.setAttribute("course", CourseService.getCourseById(courseId));
    }

    private void addCreateMultipleFormParams(HttpServletRequest request) {
        // get parameters
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        request.setAttribute("students", StudentService.getStudentsByCourseId(courseId));
        request.setAttribute("course", CourseService.getCourseById(courseId));
    }

    private void addUpdateFormParams(HttpServletRequest request) {
        // add common create form params
        addCreateFormParams(request);
        // get param
        int resultId = TypeUtil.getIntFromString(request.getParameter("result-id"));
        // verify parameters
        if (resultId == -1 || ResultService.getResultById(resultId) == null) {
            errorMessage = "Resultat introuvable.";
            return;
        }
        request.setAttribute("result", ResultService.getResultById(resultId));
    }

    private void viewTranscript(HttpServletRequest request) {
        // TODO
    }

    private boolean validGrade(double grade, double maxScore, double weight) {
        return (grade>=0 && maxScore>0 && weight>=0) && (grade<1000 && maxScore<1000 && weight<1000) && grade <= maxScore;
    }
}