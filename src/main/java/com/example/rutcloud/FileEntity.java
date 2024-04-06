package com.example.rutcloud;


import jakarta.persistence.*;

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

    public FileEntity(Long id, String fileName, byte[] data) {
        this.id = id;
        this.fileName = fileName;
        this.data = data;
    }

    public FileEntity() {
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }
}

