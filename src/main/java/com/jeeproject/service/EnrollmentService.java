package com.jeeproject.service;

import com.jeeproject.dao.EnrollmentDAO;
import com.jeeproject.model.Enrollment;

import java.util.List;

public class EnrollmentService {

    private static final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public static void addEnrollment(Enrollment enrollment) {
        enrollmentDAO.save(enrollment);
    }

    public static Enrollment getEnrollmentById(int id) {
        return enrollmentDAO.findById(id);
    }

    public static void updateEnrollment(Enrollment enrollment) {
        enrollmentDAO.update(enrollment);
    }

    public static void deleteEnrollment(int id) {
        enrollmentDAO.delete(id);
    }
}
