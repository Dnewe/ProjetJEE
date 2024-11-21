package com.jeeproject.dao;

import com.jeeproject.model.Result;
import com.jeeproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ResultDAO {

    private final SessionFactory sessionFactory;

    public ResultDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(Result result) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(result);
        transaction.commit();
        session.close();
    }

    public Result findById(int id) {
        Session session = sessionFactory.openSession();
        Result result = session.get(Result.class, id);
        session.close();
        return result;
    }

    public List<Result> findAll() {
        Session session = sessionFactory.openSession();
        List<Result> results = session.createQuery("FROM Result", Result.class).list();
        session.close();
        return results;
    }

    public void update(Result result) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(result);
        transaction.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Result result = session.get(Result.class, id);
        if (result != null) {
            session.delete(result);
        }
        transaction.commit();
        session.close();
    }
}