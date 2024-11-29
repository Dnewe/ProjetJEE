
package com.jeeproject.controller;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;
import com.jeeproject.model.User;
import com.jeeproject.service.*;
import com.jeeproject.util.EmailUtil;
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

    String resultPage;
    String errorPage;
    String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        errorMessage = null;
        switch (action) {
            case "create":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "student?action=list";
                errorPage = "error.jsp";
                createStudent(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/students/studentDetails.jsp";
                errorPage = "error.jsp";
                updateStudent(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
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
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
        	    resultPage = "WEB-INF/adminPages/student/updateStudent.jsp";
        	    errorPage = "error.jsp";
        	    viewStudent(request); // Charge les informations de l'étudiant
        	    ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
        	    break;
        	case "create":
        	    resultPage = "WEB-INF/adminPages/student/students.jsp";
        	    errorPage = "error.jsp";
        	    createStudent(request);
        	    ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
        	    break;
            case "details":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/student/studentDetails.jsp";
                errorPage = "error.jsp";
                viewStudent(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/student/students.jsp";
                errorPage = "error.jsp";
                viewStudents(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "course_list":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/student/students.jsp";
                errorPage = "error.jsp";
                viewCourseStudents(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "viewEnrolledCourses":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/student/viewEnrolledCourses.jsp";
                errorPage = "error.jsp";
                viewEnrolledCourses(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "viewGrades":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/student/viewGrades.jsp";
                errorPage = "error.jsp";
                viewGrades(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "downloadPdf":
                resultPage = "studentDashboard.jsp";
                errorPage = "error.jsp";
                generateStudentGradesPdf(request, response);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void createStudent(HttpServletRequest request) {
        // get parameters
        String lastName = request.getParameter("last-name");
        String firstName = request.getParameter("first-name");
        String contact = request.getParameter("contact");
        Date dateOfBirth = TypeUtil.getDateFromString(request.getParameter("date-of-birth"));
        int userId = TypeUtil.getIntFromString(request.getParameter("user-id"));
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
            errorMessage = "Le role de l'utilisateur ne correspond pas.";
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
        notifyStudentEnrollmentChange(student, "Inscription ajoutée");
    }
    
    private void viewEnrolledCourses(HttpServletRequest request) {
        int studentId = (int) request.getSession().getAttribute("student-id");

        // Fetch enrolled courses from the service
        List<Course> enrolledCourses = CourseService.getCoursesByStudentId(studentId);
        request.setAttribute("enrolled-courses", enrolledCourses);
    }
    
    private void viewGrades(HttpServletRequest request) {
        int studentId = (int) request.getSession().getAttribute("student-id");

        // Fetch grades and averages from the service
        Map<Course, List<Result>> grades = ResultService.getResultsByStudentIdGroupedByCourse(studentId);
        request.setAttribute("grades", grades);
    }

    private void updateStudent(HttpServletRequest request) {
        // get parameters
        String lastName = request.getParameter("last-name");
        String firstName = request.getParameter("first-name");
        String contact = request.getParameter("contact");
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
        if (ServletUtil.validString(contact)) { student.setContact(contact); }
        if (dateOfBirth != null) { student.setDateOfBirth(dateOfBirth); }
        // apply changes
        StudentService.updateStudent(student);
        request.setAttribute("student", student);
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
        Student student = StudentService.getStudentById(studentId);
        notifyStudentEnrollmentChange(student, "Inscription supprimée");
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
            "Bonjour %s,\n\nNous vous informons qu'un changement a �t� effectu� dans votre inscription : %s.\n\nCordialement,\nL'�quipe de gestion",
            student.getFirstName(),
            changeType
        );

        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), subject, message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("échec de l'envoi de l'email à l'étudiant : " + student.getId());
        }
    }

    
    private void viewCourseStudents(HttpServletRequest request) {
        // get parameters
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get students
        List<Student> students = StudentService.getStudentsByCourseId(courseId);
        request.setAttribute("students", students);
    }
    
    public void generateStudentGradesPdf(HttpServletRequest request, HttpServletResponse response) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // get grades
        Student student = StudentService.getStudentById(studentId);
        Map<Course, List<Result>> resultsByCourse = ResultService.getResultsByStudentIdGroupedByCourse(studentId);
        Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateAverage(entry.getValue())
                ));
        // Configuration de la réponse HTTP pour le téléchargement
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"releve_notes_" + studentId + ".pdf\"");

        // Générer le PDF
        try {
            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.addNewPage();
            Document document = new Document(pdfDoc);

            // Ajouter le contenu au PDF
            document.add(new Paragraph("Relevé de Notes")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Nom : " + student.getLastName()));
            document.add(new Paragraph("Prénom : " + student.getFirstName()));
            document.add(new Paragraph("\nNotes :").setBold());

            for (Map.Entry<Course, List<Result>> entry : resultsByCourse.entrySet()) {
                document.add(new Paragraph((entry.getKey()).getName()).setUnderline());
                for (Result result : entry.getValue()) {
                    document.add(new Paragraph( "    "
                            + result.getAssessmentName() + " (coeff " + result.getWeight() + ") : "
                            + result.getGrade() + " / " + result.getMaxScore()));
                }
                document.add(new Paragraph("    " + "moyenne :" + averageByCourse.get(entry.getKey()) + " / 20"));
                document.add(new Paragraph("\n"));
            }
            document.close();
        } catch (Exception e) {
            errorMessage = "Création du pdf impossible.";
        }
    }
    
    private void notifyStudentGradePublication(Student student, String courseName, double grade) {
        String subject = "Nouvelle note publiée";
        String message = String.format(
            "Bonjour %s,\n\nUne nouvelle note a été publiée pour le cours : %s.\n\nNote obtenue : %.2f\n\nCordialement,\nL'équipe de gestion",
            student.getFirstName(),
            courseName,
            grade
        );

        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), subject, message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("échec de l'envoi de l'email pour la publication de la note.");
        }
    }

    private static double calculateAverage(List<Result> results) {
        double totalWeightedGrades = 0.0;
        double totalWeights = 0.0;
        for (Result result : results) {
            totalWeightedGrades += result.getGrade() / result.getMaxScore() * result.getWeight();
            totalWeights += result.getWeight();
        }
        return Math.round((totalWeights == 0.0 ? 0.0 : totalWeightedGrades / totalWeights)*20*100)/100.;
    }
}
