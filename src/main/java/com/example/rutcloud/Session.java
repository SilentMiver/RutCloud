package com.example.rutcloud;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session", fetch = FetchType.LAZY)
    private List<FileEntity> files;

    // Constructors, getters, and setters

    public Session() {
    }

    public Session(String sessionName, List<FileEntity> files) {
        this.sessionName = sessionName;
        this.files = files;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public List<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
    }
}



