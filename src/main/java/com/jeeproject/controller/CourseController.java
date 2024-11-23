package com.jeeproject.controller;

import com.jeeproject.model.Course;
import com.jeeproject.service.CourseService;
import com.jeeproject.service.ProfessorService;
import com.jeeproject.util.ServletUtil;
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
    String errorPage;
    String errorMessage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            ServletUtil.invalidAction(request, response);
            return;
        }

        errorMessage = null;
        switch (action) {
        	case "assignProfessor":
                resultPage = "/WEB-INF/views/courseDetails.jsp";
                errorPage = "error.jsp";
                assignProfessorToCourse(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "create":
                resultPage = "";
                errorPage = "error.jsp";
                createCourse(request);
                ServletUtil.redirect(request, response, resultPage, errorPage, errorMessage);
                break;
            case "update":
                resultPage = "/WEB-INF/views/courseDetails.jsp";
                errorPage = "error.jsp";
                updateCourse(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "delete":
                resultPage = "";
                errorPage = "error.jsp";
                deleteCourse(request);
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
                resultPage = "/WEB-INF/views/courseDetails.jsp";
                errorPage = "error.jsp";
                viewCourse(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            case "list":
                resultPage = "/WEB-INF/views/courses.jsp";
                errorPage = "error.jsp";
                viewCourses(request);
                ServletUtil.forward(request, response, resultPage, errorPage, errorMessage);
                break;
            default:
                ServletUtil.invalidAction(request, response);
        }
    }

    private void assignProfessorToCourse(HttpServletRequest request) {
        int courseId = ServletUtil.getIntFromString(request.getParameter("courseId"));
        int professorId = ServletUtil.getIntFromString(request.getParameter("professor_id"));
        
        // Vérification si le cours existe
        Course course = CourseService.getCourseById(courseId);
        if (course == null) {
            errorMessage = "Cours introuvable.";
            return;
        }

        // Vérification si le professeur existe
        Professor professor = ProfessorService.getProfessorById(professorId);
        if (professor == null) {
            errorMessage = "Professeur introuvable.";
            return;
        }

        // Assigner le professeur au cours
        course.setProfessor(professor);
        CourseService.updateCourse(course);
        
        // Recharger les informations du cours avec le professeur assigné
        request.setAttribute("course", course);
        request.setAttribute("professors", ProfessorService.getAllProfessors()); // Charger la liste des professeurs
    }

    private void createCourse(HttpServletRequest request) {
        // get parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int professorId = ServletUtil.getIntFromString(request.getParameter("professor_id"));
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
    }


    private void updateCourse(HttpServletRequest request) {
        // get parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int courseId = ServletUtil.getIntFromString(request.getParameter("id"));
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
    }


    private  void deleteCourse(HttpServletRequest request) {
        // get parameters
        int courseId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // delete course
        CourseService.deleteCourse(courseId);
    }

    private void viewCourse(HttpServletRequest request) {
        // get parameters
        int courseId = ServletUtil.getIntFromString(request.getParameter("id"));
        // verify parameters
        if (courseId == -1 || CourseService.getCourseById(courseId) == null) {
            errorMessage = "Cours introuvable.";
            return;
        }
        // get courses
        Course course = CourseService.getCourseById(courseId);
        request.setAttribute("course", course);
    }

    private void viewCourses(HttpServletRequest request) {
        // get courses
        List<Course> courses = CourseService.getAllCourses();
        request.setAttribute("courses", courses);
    }
}