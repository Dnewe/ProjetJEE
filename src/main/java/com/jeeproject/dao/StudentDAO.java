package com.jeeproject.dao;

import com.jeeproject.model.Student;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.jeeproject.util.HibernateUtil;
import java.util.List;

public class StudentDAO {

    private final SessionFactory sessionFactory;

    public StudentDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(Student student) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(student);
        transaction.commit();
        session.close();
    }

    public Student findById(int id) {
        Session session = sessionFactory.openSession();
        Student student = session.get(Student.class, id);
        session.close();
        return student;
    }

    public Student findByUserId(int userId) {
        Session session = sessionFactory.openSession();
        String hql = "FROM Student s WHERE s.user.id = :userId";
        Student student = session.createQuery(hql, Student.class).setParameter("userId", userId).uniqueResult();
        session.close();
        return student;
    }

    public List<Student> findByCourseId(int courseId) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT s FROM Student s JOIN Enrollment e ON s.id = e.student.id WHERE e.course.id = :courseId";
        List<Student> students = session.createQuery(hql, Student.class)
                .setParameter("courseId", courseId)
                .getResultList();
        session.close();
        return students;
    }

    public List<Student> findAll() {
        Session session = sessionFactory.openSession();
        List<Student> students = session.createQuery("FROM Student", Student.class).list();
        session.close();
        return students;
    }

    public void update(Student student) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(student);
        transaction.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Student student = session.get(Student.class, id);
        if (student != null) {
            session.delete(student);
        }
        transaction.commit();
        session.close();
    }

    public List<Student> findFilteredStudents(String search, int courseId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        boolean hasSearch = (search != null && !search.trim().isEmpty());
        boolean hasCourseFilter = (courseId != -1);

        String hql = "SELECT DISTINCT s FROM Student s ";
        if (hasCourseFilter) { hql += "JOIN Enrollment e ON s.id = e.student.id WHERE e.course.id = :courseId "; }
        if (hasSearch) {
            hql += (hasCourseFilter ? "AND " : "WHERE ") +
                    "(LOWER(s.lastName) LIKE :search OR LOWER(s.firstName) LIKE :search) ";
        }
        hql += "ORDER BY s.lastName, s.firstName";

        Query query = session.createQuery(hql, Student.class);
        if (hasSearch) query.setParameter("search", "%" + search.toLowerCase() + "%");
        if (hasCourseFilter) query.setParameter("courseId", courseId);
        List<Student> students = query.getResultList();
        transaction.commit();
        session.close();
        return students;
    }
}
