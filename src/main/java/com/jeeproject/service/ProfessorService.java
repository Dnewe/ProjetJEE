package com.jeeproject.service;

import com.jeeproject.dao.ProfessorDAO;
import com.jeeproject.model.Professor;

import java.util.List;

public class ProfessorService {

    private static final ProfessorDAO professorDAO = new ProfessorDAO();

    public static void addProfessor(Professor professor) {
        professorDAO.save(professor);
    }

    public static Professor getProfessorById(int id) {
        return professorDAO.findById(id);
    }

    public static List<Professor> getAllProfessors() {
        return professorDAO.findAll();
    }

    public static void updateProfessor(Professor professor) {
        professorDAO.update(professor);
    }

    public static void deleteProfessor(int id) {
        professorDAO.delete(id);
    }
}
