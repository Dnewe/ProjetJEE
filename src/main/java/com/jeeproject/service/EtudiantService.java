package com.jeeproject.service;

import com.jeeproject.dao.EtudiantDAO;
import com.jeeproject.model.Etudiant;

import java.util.List;

public class EtudiantService {
    private EtudiantDAO etudiantDAO;

    public EtudiantService(EtudiantDAO etudiantDAO) {
        this.etudiantDAO = etudiantDAO;
    }

    public void saveEtudiant(Etudiant etudiant) {
        etudiantDAO.saveEtudiant(etudiant);
    }

    public List<Etudiant> getAllEtudiants() {
        System.out.println(etudiantDAO.getAllEtudiants());
        return etudiantDAO.getAllEtudiants();
    }
}
