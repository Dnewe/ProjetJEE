package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Student;
import com.jeeproject.model.User;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.service.StudentService;
import com.jeeproject.service.UserService;
import com.jeeproject.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "StudentController", urlPatterns = "/student")
public class StudentController extends HttpServlet {

    String resultPage;
    String errorPage;
    String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("generateTranscript".equals(action)) {
            generateTranscript(request, response);
        } else {
            ServletUtil.invalidAction(request, response);
        }

        errorMessage = null;
        switch (action) {
            case "create":
                resultPage = "student?action=list";
                errorPage = "error.jsp";
                createStudent(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                resultPage = "WEB-INF/views/studentDetails.jsp";
                errorPage = "error.jsp";
                updateStudent(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                resultPage = "student?action=list";
                errorPage = "error.jsp";
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
        	    resultPage = "WEB-INF/views/updateStudent.jsp";
        	    errorPage = "error.jsp";
        	    viewStudent(request); // Charge les informations de l'étudiant
        	    ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
        	    break;
        	case "create":
        	    resultPage = "WEB-INF/views/students.jsp"; 
        	    errorPage = "error.jsp";
        	    createStudent(request);
        	    ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
        	    break;
            case "details":
                resultPage = "WEB-INF/views/studentDetails.jsp";
                errorPage = "error.jsp";
                viewStudent(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                resultPage = "WEB-INF/views/students.jsp";
                errorPage = "error.jsp";
                viewStudents(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "course_list":
                resultPage = "WEB-INF/views/students.jsp";
                errorPage = "error.jsp";
                viewCourseStudents(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "viewEnrolledCourses":
                resultPage = "WEB-INF/views/viewEnrolledCourses.jsp";
                errorPage = "error.jsp";
                viewEnrolledCourses(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "viewGrades":
                resultPage = "WEB-INF/views/viewGrades.jsp";
                errorPage = "error.jsp";
                viewGrades(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void createStudent(HttpServletRequest request) {
        // get parameters
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String contact = request.getParameter("contact");
        Date dateOfBirth = ServletUtil.getDateFromString(request.getParameter("date_of_birth"));
        int userId = ServletUtil.getIntFromString(request.getParameter("user_id"));
        // verify parameters
        if (!ServletUtil.validString(lastName)) {
            errorMessage = "Nom invalide.";
            return;
        }
        if (!ServletUtil.validString(firstName)) {
            errorMessage = "Prénom invalide.";
            return;
        }
        if (!ServletUtil.validString(contact)) {
            errorMessage = "Contact invalide.";
            return;
        }
        if (dateOfBirth == null) {
            errorMessage = "Date invalide. Utilisez le format yyyy-MM-dd.";
            return;
        }
        if (userId == -1 || UserService.getUserById(userId) == null) {
            errorMessage = "Utilisateur introuvable";
            return;
        }
        if (StudentService.getStudentByUserId(userId) != null || ProfessorService.getProfessorByUserId(userId) != null) {
            errorMessage = "Utilisateur déjà associé à un étudiant ou professeur.";
            return;
        }
        // verify user role
        User user = UserService.getUserById(userId);
        if (!user.getRole().equals("student")) {
            errorMessage = "Le rôle de l'utilisateur ne correspond pas.";
            return;
        }
        // create student
        Student student = new Student();
        student.setLastName(lastName);
        student.setFirstName(firstName);
        student.setContact(contact);
        student.setDateOfBirth(dateOfBirth);
        student.setUser(UserService.getUserById(userId));
        // add student
        StudentService.addStudent(student);
    }
    
    private void viewEnrolledCourses(HttpServletRequest request) {
        int studentId = (int) request.getSession().getAttribute("studentId");

        // Fetch enrolled courses from the service
        List<Course> enrolledCourses = CourseService.getCoursesByStudentId(studentId);
        request.setAttribute("enrolledCourses", enrolledCourses);
    }
    
    private void viewGrades(HttpServletRequest request) {
        int studentId = (int) request.getSession().getAttribute("studentId");

        // Fetch grades and averages from the service
        List<Grade> grades = GradeService.getGradesByStudentId(studentId);
        request.setAttribute("grades", grades);
    }

    private void updateStudent(HttpServletRequest request) {
        // get parameters
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String contact = request.getParameter("contact");
        Date dateOfBirth = ServletUtil.getDateFromString(request.getParameter("date_of_birth"));
        int studentId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // update student
        Student student = StudentService.getStudentById(studentId);
        if (ServletUtil.validString(lastName)) { student.setLastName(lastName); }
        if (ServletUtil.validString(firstName)) { student.setFirstName(firstName); }
        if (ServletUtil.validString(contact)) { student.setContact(contact); }
        if (dateOfBirth != null) { student.setDateOfBirth(dateOfBirth); }
        // apply changes
        StudentService.updateStudent(student);
        request.setAttribute("student", student);
    }

    private  void deleteStudent(HttpServletRequest request) {
        // get parameters
        int studentId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // delete student
        StudentService.deleteStudent(studentId);
    }

    private void viewStudent(HttpServletRequest request) {
        // get parameters
        int studentId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Utilisateur introuvable.";
            return;
        }
        // get student
        Student student = StudentService.getStudentById(studentId);
        request.setAttribute("student", student);
        // get student courses
        List<Course> courses = CourseService.getCoursesByStudentId(studentId);
        request.setAttribute("courses", courses);
    }

    private void viewStudents(HttpServletRequest request) {
        List<Student> students = StudentService.getAllStudents();
        request.setAttribute("students", students);
    }

    private void viewCourseStudents(HttpServletRequest request) {
        // get parameters
        int courseId = ServletUtil.getIntFromString(request.getParameter("course_id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get students
        List<Student> students = StudentService.getStudentsByCourseId(courseId);
        request.setAttribute("students", students);
    }
    
    private void generateTranscript(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int studentId = (int) request.getSession().getAttribute("studentId");

        // Fetch grades
        List<Grade> grades = GradeService.getGradesByStudentId(studentId);

        // Generate transcript (PDF or other format)
        byte[] transcript = TranscriptGenerator.generate(grades);

        // Send the transcript as a download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transcript.pdf");
        response.getOutputStream().write(transcript);
    }
}