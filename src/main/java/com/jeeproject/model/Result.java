package com.jeeproject.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "result")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "grade", nullable = false)
    private double grade;

    @Column(name = "entry_date", nullable = false)
    private Date entryDate;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    public Date getEntryDate() { return entryDate; }
    public void setEntryDate(Date entryDate) { this.entryDate = entryDate; }

    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) {}
}
