package com.example.rutcloud;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class HomeController {

    private final FileEntityRepository fileEntityRepository;
    private final SessionRepository sessionRepository;

    public HomeController(FileEntityRepository fileEntityRepository, SessionRepository sessionRepository) {
        this.fileEntityRepository = fileEntityRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileEntityRepository.findAll());
        return "index";
    }

    @GetMapping("/session-add")
    public String add() {
        return "get/session-adder";
    }

    @GetMapping("/session-select")
    public String select() {
        return "send/session-selector";
    }

    @GetMapping("/send-files")
    public String sendFiles(Model model, @RequestParam Long session) {
        model.addAttribute("session", session);
        model.addAttribute("session_number", session);
        model.addAttribute("files", fileEntityRepository.findAllBySessionId(session));
        return "send/send-files";
    }

    @GetMapping("/get-files")
    public String getFiles(Model model, @RequestParam Long session) {
        Session sessionData = sessionRepository.findById(session).orElse(null);
        if (sessionData == null) {
            return "redirect:/"; // Перенаправление на главную страницу, если сессия не найдена
        }

        model.addAttribute("session", session);
        model.addAttribute("session_number", session);
        model.addAttribute("files", fileEntityRepository.findAllBySessionId(session));

        String qrCodeFileName = "qr-code-" + session + ".png";
        try {
            byte[] qrCodeImage = QRCodeGenerator.generateQRCodeImage("http://localhost:8080/send-files?session=" + session, 200, 200);
            QRCodeGenerator.saveQRCodeToFile(qrCodeImage, qrCodeFileName);
            model.addAttribute("qrCodeFileName", qrCodeFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "get/get-files";
    }
    @GetMapping("/qr-codes/{filename}")
    public ResponseEntity<byte[]> getQRCodeImage(@PathVariable String filename) {
        try {
            Path path = FileSystems.getDefault().getPath(filename);
            byte[] imageData = Files.readAllBytes(path);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
