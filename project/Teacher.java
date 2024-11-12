package com.example.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "enseignant")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nom", nullable = false)
    private String lastName;

    @Column(name = "prenom", nullable = false)
    private String firstName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses;

    // Constructors, getters, setters
}
