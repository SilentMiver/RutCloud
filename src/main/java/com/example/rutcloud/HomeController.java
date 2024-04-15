package com.example.rutcloud;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    @GetMapping("/files")
    public String files(Model model) {
        model.addAttribute("files", fileEntityRepository.findAll());
        return "files";
    }

}
