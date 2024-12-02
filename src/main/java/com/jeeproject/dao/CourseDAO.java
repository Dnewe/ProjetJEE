package com.jeeproject.dao;

import com.jeeproject.model.Course;
import com.jeeproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CourseDAO {

    private final SessionFactory sessionFactory;

    public CourseDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(Course course) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(course);
        transaction.commit();
        session.close();
    }

    public Course findById(int id) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT c FROM Course c WHERE c.id = :id";
        Course course = session.createQuery(hql, Course.class)
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return course;
    }

    public List<Course> findByStudentId(int studentId) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT c FROM Course c JOIN Enrollment e ON c.id = e.course.id WHERE e.student.id = :studentId";
        List<Course> courses = session.createQuery(hql, Course.class)
                .setParameter("studentId", studentId)
                .getResultList();
        session.close();
        return courses;
    }

    public List<Course> findByProfessorId(int professorId) {
        Session session = sessionFactory.openSession();
        String hql = "FROM Course c WHERE c.professor.id = :professorId";
        List<Course> courses = session.createQuery(hql, Course.class)
                .setParameter("professorId", professorId)
                .getResultList();
        session.close();
        return courses;
    }

    public List<Course> findAll() {
        Session session = sessionFactory.openSession();
        List<Course> courses = session.createQuery("FROM Course", Course.class).list();
        session.close();
        return courses;
    }

    public void update(Course course) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(course);
        transaction.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Course course = session.get(Course.class, id);
        if (course != null) {
            session.delete(course);
        }
        transaction.commit();
        session.close();
    }
}
