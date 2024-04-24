package com.example.rutcloud;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final FileEntityRepository fileEntityRepository;

    public HomeController(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileEntityRepository.findAll());
        return "index";
    }

    @GetMapping("/session-add")
    public String add() {
        return "send/session-adder";
    }

    @GetMapping("/session-select")
    public String select() {
        return "get/session-selector";
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
        model.addAttribute("session", session);
        model.addAttribute("session_number", session);
        model.addAttribute("files", fileEntityRepository.findAllBySessionId(session));
        return "get/get-files";
    }
}
