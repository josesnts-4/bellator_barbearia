package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String testEmail(@RequestParam String to) {
        try {
            emailService.enviarEmailTeste(to);
            return "Email enviado com sucesso para " + to + "! Verifique sua caixa de entrada.";
        } catch (Exception e) {
            return "Falha ao enviar email: " + e.getMessage();
        }
    }
}
