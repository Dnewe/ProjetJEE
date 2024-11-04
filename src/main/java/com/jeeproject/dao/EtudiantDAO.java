package com.jeeproject.dao;


import com.jeeproject.model.Etudiant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class EtudiantDAO {
    private SessionFactory sessionFactory;

    public EtudiantDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveEtudiant(Etudiant etudiant) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(etudiant);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Etudiant> getAllEtudiants() {
        try (Session session = sessionFactory.openSession()) {
            Query<Etudiant> query = session.createQuery("from Etudiant", Etudiant.class);
            return query.list();
        }
    }
}
