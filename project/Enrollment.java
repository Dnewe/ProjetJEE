package com.example.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inscription")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id", nullable = false)
    private Course course;

    @Column(name = "date_inscription", nullable = false)
    private Date enrollmentDate;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Grade grade;

    // Constructors, getters, setters
}
