
package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "StudentController", urlPatterns = "/student")
public class StudentController extends HttpServlet {

    private String resultPage;
    private String errorPage = ServletUtil.defaultErrorPage;
    private String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        errorMessage = null;
        switch (action) {
            /*case "create":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "student?action=list";
                errorPage = "student?action=list";
                createStudent(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;*/
            case "update":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "WEB-INF/adminPages/students/studentDetails.jsp");
                errorPage = "student?action=list";
                updateStudent(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "student?action=list";
                errorPage = "student?action=list";
                deleteStudent(request);
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
        	case "updateForm":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
        	    resultPage = "WEB-INF/adminPages/student/updateStudent.jsp";
                errorPage = "student?action=list";
        	    viewStudent(request); // Charge les informations de l'étudiant
        	    ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
        	    break;
            case "details":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/student/studentDetails.jsp";
                errorPage = "student?action=list";
                viewStudent(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/student/students.jsp";
                viewStudents(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "courseList":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/professorPages/students.jsp";
                viewCourseStudents(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void updateStudent(HttpServletRequest request) {
        // get parameters
        String lastName = request.getParameter("last-name");
        String firstName = request.getParameter("first-name");
        Date dateOfBirth = TypeUtil.getDateFromString(request.getParameter("date-of-birth"));
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // update student
        Student student = StudentService.getStudentById(studentId);
        if (ServletUtil.validString(lastName)) { student.setLastName(lastName); }
        if (ServletUtil.validString(firstName)) { student.setFirstName(firstName); }
        if (dateOfBirth != null) { student.setDateOfBirth(dateOfBirth); }
        // apply changes
        StudentService.updateStudent(student);
        request.setAttribute("student", student);
        request.setAttribute("successMessage", "Profil étudiant modifié avec succès");
    }

    private  void deleteStudent(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // delete student
        StudentService.deleteStudent(studentId);
        request.setAttribute("successMessage", "Profil étudiant supprimé avec succès");
    }

    private void viewStudent(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // get student
        Student student = StudentService.getStudentById(studentId);
        request.setAttribute("student", student);
        // get student courses
        List<Course> enrolledCourses = CourseService.getCoursesByStudentId(studentId);
        request.setAttribute("enrolledCourses", enrolledCourses);
        // get avalaible courses
        List<Course> availableCourses = CourseService.getAllCourses();
        availableCourses.removeAll(enrolledCourses);
        request.setAttribute("availableCourses", availableCourses);
    }

    private void viewStudents(HttpServletRequest request) {
        // get parameters
        String search = request.getParameter("search");
        String courseIdParam = request.getParameter("course-id");
        int courseId = (courseIdParam != null && !courseIdParam.isEmpty()) ? Integer.parseInt(courseIdParam) : -1;

        // get filtered students
        List<Student> students = StudentService.getFilteredStudents(search, courseId);
        request.setAttribute("students", students);

        // get courses
        List<Course> courses = CourseService.getAllCourses();
        request.setAttribute("courses", courses);
        // get filtered-course
        if (courseId !=-1) { request.setAttribute("filteredCourse", CourseService.getCourseById(courseId)); }
    }
    
    private void notifyStudentEnrollmentChange(Student student, String changeType) {
        String subject = "Changement dans votre inscription";
        String message = String.format(
            "Bonjour %s,\n\nNous vous informons qu'un changement a été effectué dans votre inscription : %s.\n\nCordialement,\nL'équipe de gestion",
            student.getFirstName(),
            changeType
        );

        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), subject, message);
        } catch (MessagingException e) {
            errorMessage = "échec de l'envoi de l'email à l'étudiant : " + student.getId();
        }
    }

    private void viewCourseStudents(HttpServletRequest request) {
        // get parameters
        String search = request.getParameter("search");
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get filtered students
        List<Student> students = StudentService.getFilteredStudents(search, courseId);
        request.setAttribute("students", students);
        // get average
        Map<Student, List<Result>> resultsByStudent = ResultService.getResultsByCourseIdGroupedByStudent(courseId);
        Map<Student, Double> averageByStudent = resultsByStudent.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                ));
        request.setAttribute("averageByStudent", averageByStudent);
        request.setAttribute("course", CourseService.getCourseById(courseId));
    }
    
    private void notifyStudentGradePublication(Student student, String courseName, double grade) {
        String subject = "Nouvelle note publiée";
        String message = String.format(
            "Bonjour %s,\n\nUne nouvelle note a été publiée pour le cours : %s.\n\nCordialement,\nL'équipe de gestion",
            student.getFirstName(),
            courseName
        );

        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), subject, message);
        } catch (MessagingException e) {
            e.printStackTrace();
            errorMessage = "échec de l'envoi de l'email pour la publication de la note.";
        }
    }
}
