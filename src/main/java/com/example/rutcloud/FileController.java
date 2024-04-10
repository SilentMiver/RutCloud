package com.example.rutcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private SessionService sessionService;

    // Обработчик для загрузки файлов
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Создаем новую сессию
            Session session = new Session();
            sessionService.createSession(session);

            // Сохраняем файл в базу данных, связывая его с созданной сессией
            FileEntity savedFile = fileService.saveFile(file, session);

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully with ID: " + savedFile.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    // Обработчик для получения списка файлов
    @GetMapping
    public ResponseEntity<List<FileDto>> getAllFiles() {
        List<FileEntity> files = fileService.getAllFiles();
        List<FileDto> fileDtos = files.stream()
                .map(this::convertToFileDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileDtos);
    }

    // Обработчик для скачивания файла по его ID
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Optional<FileEntity> fileOptional = fileService.getFile(id);
        if (fileOptional.isPresent()) {
            FileEntity file = fileOptional.get();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(file.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Метод для преобразования FileEntity в FileDto
    private FileDto convertToFileDto(FileEntity fileEntity) {
        FileDto fileDto = new FileDto();
        fileDto.setId(fileEntity.getId());
        fileDto.setFileName(fileEntity.getFileName());
        // Можно добавить другие поля, если нужно
        return fileDto;
    }
}

