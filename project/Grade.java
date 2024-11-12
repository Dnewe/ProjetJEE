package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "resultat")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscription_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "note")
    private double grade;

    @Column(name = "date_saisie", nullable = false)
    private Date gradeSubmissionDate;

    // Constructors, getters, setters
}
