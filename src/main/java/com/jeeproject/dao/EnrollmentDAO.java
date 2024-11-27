package com.jeeproject.dao;

import com.jeeproject.model.Enrollment;
import com.jeeproject.model.Student;
import com.jeeproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class EnrollmentDAO {

    private final SessionFactory sessionFactory;

    public EnrollmentDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(Enrollment enrollment) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(enrollment);
        transaction.commit();
        session.close();
    }

    public Enrollment findById(int id) {
        Session session = sessionFactory.openSession();
        Enrollment enrollment = session.get(Enrollment.class, id);
        session.close();
        return enrollment;
    }

    public Enrollment findByStudentIdAndCourseId(int studentId, int courseId) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT e " +
                "FROM Enrollment e " +
                "WHERE e.course.id = :courseId AND e.student.id = :studentId";
        Enrollment enrollment = session.createQuery(hql, Enrollment.class)
                .setParameter("courseId", courseId)
                .setParameter("studentId", studentId)
                .uniqueResult();
        session.close();
        return enrollment;
    }

    public List<Enrollment> findAll() {
        Session session = sessionFactory.openSession();
        List<Enrollment> enrollments = session.createQuery("FROM Enrollment", Enrollment.class).list();
        session.close();
        return enrollments;
    }

    public void update(Enrollment enrollment) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(enrollment);
        transaction.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Enrollment enrollment = session.get(Enrollment.class, id);
        if (enrollment != null) {
            session.delete(enrollment);
        }
        transaction.commit();
        session.close();
    }
}
