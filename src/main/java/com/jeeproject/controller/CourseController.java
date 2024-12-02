package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.model.Professor;
import com.jeeproject.model.Student;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.service.StudentService;
import com.jeeproject.util.ServletUtil;
import com.jeeproject.util.TypeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CourseController", urlPatterns = "/course")
public class CourseController extends HttpServlet {

    String resultPage;
    String errorPage = ServletUtil.defaultErrorPage;
    String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            System.out.println("NO ACTION COURSE (post)");
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
            case "create":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=list");
                errorPage = "course?action=createForm";
                createCourse(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=list");
                errorPage = "course?action=list";
                updateCourse(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=list");
                errorPage = "course?action=list";
                deleteCourse(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "assignProfessor":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=list");
                errorPage = "course?action=list";
                assignProfessorToCourse(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "removeProfessor":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = ServletUtil.getResultPage(request, "course?action=list");
                errorPage = "course?action=list";
                removeProfessorFromCourse(request);
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
            case "details":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/adminPages/course/courseDetails.jsp";
                errorPage = "course?action=list";
                viewCourse(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/adminPages/course/courses.jsp";
                viewCourses(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "studentList":
                if (ServletUtil.notStudent(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/studentPages/courses.jsp";
                viewStudentCourses(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "professorList":
                if (ServletUtil.notProfessor(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "/WEB-INF/professorPages/courses.jsp";
                viewProfessorCourses(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "createForm":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/course/createCourse.jsp";
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
            case "updateForm":
                if (ServletUtil.notAdmin(request)) { ServletUtil.unauthorized(request,response); return;}
                resultPage = "WEB-INF/adminPages/course/updateCourse.jsp";
                errorPage = "course?action=list";
                setUpdateFormParam(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void assignProfessorToCourse(HttpServletRequest request) {
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        int professorId = TypeUtil.getIntFromString(request.getParameter("professor-id"));
        
        // Vérification si le cours existe
        Course course = CourseService.getCourseById(courseId);
        if (course == null) {
            System.out.println(courseId);
            errorMessage = "Cours introuvable.";
            return;
        }
        // Vérification si le professeur existe
        Professor professor = ProfessorService.getProfessorById(professorId);
        if (professor == null) {
            errorMessage = "Professeur introuvable.";
            //return;
        }

        // Assigner le professeur au cours
        course.setProfessor(professor);
        CourseService.updateCourse(course);
        request.setAttribute("successMessage", "Professeur assigné au cours avec succès");
    }

    private void removeProfessorFromCourse(HttpServletRequest request) {
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // Vérification si le cours existe
        Course course = CourseService.getCourseById(courseId);
        if (course == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        course.setProfessor(null);
        CourseService.updateCourse(course);
        request.setAttribute("successMessage", "Professeur retiré du cours avec succès");
    }

    private void createCourse(HttpServletRequest request) {
        // get parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int professorId = TypeUtil.getIntFromString(request.getParameter("professor-id"));
        // verify parameters
        if (!ServletUtil.validString(name)) {
            errorMessage = "Le nom du cours n'est pas valide";
            return;
        }
        if (!ServletUtil.validString(description)) {
            errorMessage = "La description du cours n'est pas valide";
            return;
        }
        if (professorId != -1 && ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Le professeur est introuvable.";
            return;
        }
        // create course
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        if (professorId != -1) { course.setProfessor(ProfessorService.getProfessorById(professorId));} else { course.setProfessor(null); }
        // add course
        CourseService.addCourse(course);
        request.setAttribute("successMessage", "Cours créé avec succès");
    }


    private void updateCourse(HttpServletRequest request) {
        // get parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // update course
        Course course = CourseService.getCourseById(courseId);
        if (ServletUtil.validString(name)) { course.setName(name); }
        if (ServletUtil.validString(description)) { course.setDescription(description); }
        CourseService.updateCourse(course);
        request.setAttribute("course", course);
        request.setAttribute("successMessage", "Cours modifié avec succès");
    }


    private  void deleteCourse(HttpServletRequest request) {
        // get parameters
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // delete course
        CourseService.deleteCourse(courseId);
        request.setAttribute("successMessage", "Cours supprimé avec succès");
    }

    private void viewCourse(HttpServletRequest request) {
        // get parameters
        int courseId = TypeUtil.getIntFromString(request.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get course
        Course course = CourseService.getCourseById(courseId);
        request.setAttribute("course", course);
        // get enrolled students
        List<Student> enrolledStudents = StudentService.getStudentsByCourseId(courseId);
        request.setAttribute("enrolledStudents", enrolledStudents);
        // get available students
        List<Student> availableStudents = StudentService.getAllStudents();
        availableStudents.removeAll(enrolledStudents);
        request.setAttribute("availableStudents", availableStudents);
        // get available professors
        List<Professor> availableProfessors = ProfessorService.getAllProfessors();
        request.setAttribute("availableProfessors", availableProfessors);
    }

    private void viewCourses(HttpServletRequest request) {
        // get courses
        List<Course> courses = CourseService.getAllCourses();
        request.setAttribute("courses", courses);
    }

    private void viewStudentCourses(HttpServletRequest request) {
        // get parameters
        int studentId = TypeUtil.getIntFromString(request.getParameter("student-id"));
        // verify parameters
        if (studentId == -1 || StudentService.getStudentById(studentId) == null) {
            errorMessage = "Etudiant introuvable.";
            return;
        }
        // get courses
        List<Course> courses = CourseService.getCoursesByStudentId(studentId);
        request.setAttribute("courses", courses);
    }

    private void viewProfessorCourses(HttpServletRequest request) {
        // get parameters
        int professorId = TypeUtil.getIntFromString(request.getParameter("professor-id"));
        // verify parameters
        if (professorId == -1 || ProfessorService.getProfessorById(professorId) == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }
        // get courses
        List<Course> courses = CourseService.getCoursesByProfessorId(professorId);
        request.setAttribute("courses", courses);
    }

    private void setUpdateFormParam(HttpServletRequest req) {
        int courseId = TypeUtil.getIntFromString(req.getParameter("course-id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
        }
        req.setAttribute("course", CourseService.getCourseById(courseId));
    }
}