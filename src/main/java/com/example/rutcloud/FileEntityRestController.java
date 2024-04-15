package com.example.rutcloud;

import com.example.rutcloud.FileEntity;
import com.example.rutcloud.FileEntityRepository;
import com.example.rutcloud.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileEntityRestController {

    @Autowired
    private FileEntityRepository fileEntityRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionRestController sessionRestController;

    @GetMapping
    public List<FileEntity> getAllFileEntities() {
        return fileEntityRepository.findAll();
    }

    @GetMapping("/rest/{id}")
    public ResponseEntity<FileEntity> getFileEntityById(@PathVariable Long id, @RequestParam Long session) {
        Optional<FileEntity> fileEntityOptional = fileEntityRepository.findById(id);
        if (fileEntityOptional.isPresent()) {
            FileEntity fileEntity = fileEntityOptional.get();
            // Проверяем, имеет ли пользователь доступ к файлу через сеанс
            if (fileEntity.getSession().getId().equals(session)) {
                return new ResponseEntity<>(fileEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Отказано в доступе
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, @RequestParam Long session) {
        Optional<FileEntity> fileEntityOptional = fileEntityRepository.findById(id);
        if (fileEntityOptional.isPresent()) {
            FileEntity fileEntity = fileEntityOptional.get();
            // Проверяем, имеет ли пользователь доступ к файлу через сеанс
            if (fileEntity.getSession().getId().equals(session)) {
                ByteArrayResource resource = new ByteArrayResource(fileEntity.getData());
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileEntity.getFileName());
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(fileEntity.getData().length)
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Отказано в доступе
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<FileEntity> createFileEntity(@RequestBody FileEntity fileEntity) {
        FileEntity createdFileEntity = fileEntityRepository.save(fileEntity);
        return new ResponseEntity<>(createdFileEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FileEntity> updateFileEntity(@PathVariable Long id, @RequestBody FileEntity fileEntity) {
        if (!fileEntityRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fileEntity.setId(id);
        FileEntity updatedFileEntity = fileEntityRepository.save(fileEntity);
        return new ResponseEntity<>(updatedFileEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFileEntity(@PathVariable Long id) {
        if (!fileEntityRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fileEntityRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("fileData") MultipartFile file,
                                             @RequestParam("fileName") String fileName,
                                             @RequestParam("session") Long sessionId) {

        // Проверяем существование указанной сессии через SessionRestController
        ResponseEntity<Session> sessionResponse = sessionRestController.getSessionById(sessionId);
        if (sessionResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>("Invalid session ID", HttpStatus.UNAUTHORIZED);
        }

        // Продолжаем с загрузкой файла
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
            }

            byte[] fileData = file.getBytes();

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setData(fileData);
            fileEntity.setSession(sessionResponse.getBody()); // Устанавливаем сессию

            fileEntityRepository.save(fileEntity);

            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

