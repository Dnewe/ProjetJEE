package com.jeeproject.controller;

import com.jeeproject.model.Etudiant;
import com.jeeproject.service.EtudiantService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.hibernate.SessionFactory;

import com.jeeproject.dao.EtudiantDAO;
import com.jeeproject.util.HibernateUtil;


@WebServlet("/etudiants")
public class EtudiantController extends HttpServlet {
    private EtudiantService etudiantService;

    @Override
    public void init() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        etudiantService = new EtudiantService(new EtudiantDAO(sessionFactory));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        request.setAttribute("etudiants", etudiants);
        request.getRequestDispatcher("/viewEtudiants.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        int age = Integer.parseInt(request.getParameter("age"));

        Etudiant etudiant = new Etudiant(nom, prenom, age);
        etudiantService.saveEtudiant(etudiant);

        response.sendRedirect("etudiants");
    }
}
