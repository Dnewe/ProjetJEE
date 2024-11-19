package com.jeeproject.dao;

import com.jeeproject.model.Professor;
import com.jeeproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ProfessorDAO {

    private final SessionFactory sessionFactory;

    public ProfessorDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(Professor professor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(professor);
        transaction.commit();
        session.close();
    }

    public Professor findById(int id) {
        Session session = sessionFactory.openSession();
        Professor professor = session.get(Professor.class, id);
        session.close();
        return professor;
    }

    public List<Professor> findAll() {
        Session session = sessionFactory.openSession();
        List<Professor> professors = session.createQuery("FROM Professor", Professor.class).list();
        session.close();
        return professors;
    }

    public void update(Professor professor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(professor);
        transaction.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Professor professor = session.get(Professor.class, id);
        if (professor != null) {
            session.delete(professor);
        }
        transaction.commit();
        session.close();
    }
}
