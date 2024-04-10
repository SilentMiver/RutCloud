package com.example.rutcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    // Метод для сохранения файла в базе данных
    @Transactional
    public FileEntity saveFile(MultipartFile file, Session session) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setData(file.getBytes());
        fileEntity.setSession(session); // Связываем файл с сессией
        return fileRepository.save(fileEntity);
    }

    // Метод для получения файла по его ID
    public Optional<FileEntity> getFile(Long id) {
        return fileRepository.findById(id);
    }

    // Метод для получения всех файлов
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }
}

