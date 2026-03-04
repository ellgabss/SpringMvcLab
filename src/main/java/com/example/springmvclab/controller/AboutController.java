package com.example.springmvclab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;

@Controller
public class AboutController {

    @GetMapping("/about") // Menentukan endpoint GET /about
    public String aboutPage(Model model) {
        // Mengirim data string dan list ke template
        model.addAttribute("appName", "Spring MVC Lab");
        model.addAttribute("version", "1.0");
        model.addAttribute("author", "Ellen Gabriela Serumena"); // Pakai namamu

        List<String> techList = Arrays.asList("Spring Boot", "Thymeleaf", "Bootstrap", "Java 25");
        model.addAttribute("technologies", techList); // Mengirim list teknologi

        return "about"; // Akan mencari file templates/about.html
    }
}