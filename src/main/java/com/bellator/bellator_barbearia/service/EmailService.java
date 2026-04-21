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

    public void verificarConfig() {
        System.out.println("DEBUG: Tentando enviar e-mail usando o remetente: [" + remetente + "]");
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
            System.err.println("ERRO CRÍTICO ao enviar email de boas-vindas para " + destinatario + ": " + e.getMessage());
            if (e.getCause() != null) System.err.println("Causa: " + e.getCause().getMessage());
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
            System.err.println("ERRO CRÍTICO ao enviar email de confirmação de agendamento para " + destinatario + ": " + e.getMessage());
            if (e.getCause() != null) System.err.println("Causa: " + e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @Async
    public void enviarEmailCancelamentoAgendamento(String destinatario, String nome, LocalDate data, LocalTime horario) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject("Cancelamento de Agendamento - Bellator Barbearia \u274C");

            String texto = "Olá " + nome + ",\n\n"
                    + "Informamos que o seu agendamento foi cancelado.\n\n"
                    + "Detalhes do Agendamento Cancelado:\n"
                    + "📅 Data: " + data.format(dateFormatter) + "\n"
                    + "⏰ Horário: " + horario.format(timeFormatter) + "\n\n"
                    + "Caso deseje realizar um novo agendamento, acesse nosso sistema.\n\n"
                    + "Atenciosamente,\nEquipe Bellator Barbearia";

            mensagem.setText(texto);
            System.out.println("Iniciando envio de email de cancelamento de agendamento para: " + destinatario);
            mailSender.send(mensagem);

            System.out.println("Email de cancelamento de agendamento enviado com sucesso para: " + destinatario);
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao enviar email de cancelamento de agendamento para " + destinatario + ": " + e.getMessage());
            if (e.getCause() != null) System.err.println("Causa: " + e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @Async
    public void enviarEmailReagendamentoAgendamento(String destinatario, String nome, 
            LocalDate dataAntiga, LocalTime horarioAntigo, LocalDate dataNova, LocalTime horarioNovo) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject("Reagendamento de Agendamento - Bellator Barbearia \uD83D\uDD04");

            String texto = "Olá " + nome + ",\n\n"
                    + "Seu agendamento foi reagendado com sucesso!\n\n"
                    + "De:\n"
                    + "📅 " + dataAntiga.format(dateFormatter) + " às " + horarioAntigo.format(timeFormatter) + "\n\n"
                    + "Para:\n"
                    + "📅 " + dataNova.format(dateFormatter) + " às " + horarioNovo.format(timeFormatter) + "\n\n"
                    + "Estamos ansiosos para atendê-lo no novo horário!\n\n"
                    + "Atenciosamente,\nEquipe Bellator Barbearia";

            mensagem.setText(texto);
            System.out.println("Iniciando envio de email de reagendamento para: " + destinatario);
            mailSender.send(mensagem);

            System.out.println("Email de reagendamento enviado com sucesso para: " + destinatario);
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao enviar email de reagendamento para " + destinatario + ": " + e.getMessage());
            if (e.getCause() != null) System.err.println("Causa: " + e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    public void enviarEmailTeste(String destinatario) {
        verificarConfig();
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Teste de Configuração - Bellator Barbearia");
        mensagem.setText("Se você recebeu este email, a configuração SMTP está funcionando corretamente no Railway!");
        mailSender.send(mensagem);
    }
}
