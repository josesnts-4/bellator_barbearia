package com.bellator.bellator_barbearia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarEmailBoasVindas(String destinatario, String nome) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject("Bem-vindo(a) à Bellator Barbearia! \uD83D\uDC88");
            
            String texto = "Olá " + nome + ",\n\n"
                    + "Seu cadastro foi realizado com sucesso!\n"
                    + "Estamos muito felizes em ter você como cliente da Bellator Barbearia.\n\n"
                    + "Acesse nosso sistema para fazer seus agendamentos de forma rápida e prática.\n\n"
                    + "Um abraço,\nEquipe Bellator Barbearia";
            
            mensagem.setText(texto);
            System.out.println("Iniciando envio de email de boas-vindas para: " + destinatario);
            mailSender.send(mensagem);
            
            System.out.println("Email de boas-vindas enviado com sucesso para: " + destinatario);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de boas-vindas para " + destinatario);
            e.printStackTrace();
        }
    }

    @Async
    public void enviarEmailConfirmacaoAgendamento(String destinatario, String nome, LocalDate data, LocalTime horario) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject("Confirmação de Agendamento - Bellator Barbearia \uD83D\uDCC5");

            String texto = "Olá " + nome + ",\n\n"
                    + "Seu agendamento foi confirmado com sucesso!\n\n"
                    + "Detalhes do Agendamento:\n"
                    + "📅 Data: " + data.format(dateFormatter) + "\n"
                    + "⏰ Horário: " + horario.format(timeFormatter) + "\n\n"
                    + "Estamos ansiosos para atendê-lo!\n\n"
                    + "Caso precise cancelar ou reagendar, acesse nosso sistema.\n\n"
                    + "Atenciosamente,\nEquipe Bellator Barbearia";

            mensagem.setText(texto);
            System.out.println("Iniciando envio de email de confirmação de agendamento para: " + destinatario);
            mailSender.send(mensagem);

            System.out.println("Email de confirmação de agendamento enviado com sucesso para: " + destinatario);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de confirmação de agendamento para " + destinatario);
            e.printStackTrace();
        }
    }
}
