package com.example.rutcloud;

import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    // Constructors, getters, and setters

    public FileEntity() {
    }

    public FileEntity(String fileName, byte[] data, Session session) {
        this.fileName = fileName;
        this.data = data;
        this.session = session;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
