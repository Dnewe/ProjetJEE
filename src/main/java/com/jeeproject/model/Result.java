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

    @Column(name = "max_score", nullable = false)
    private double maxScore;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "entry_date", nullable = false)
    private Date entryDate;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public Date getEntryDate() { return entryDate; }
    public void setEntryDate(Date entryDate) { this.entryDate = entryDate; }

    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) {}
}
