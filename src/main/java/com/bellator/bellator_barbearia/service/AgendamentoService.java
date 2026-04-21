package com.bellator.bellator_barbearia.service;

import com.bellator.bellator_barbearia.dto.AgendamentoCreateRequest;
import com.bellator.bellator_barbearia.exception.ApiException;
import com.bellator.bellator_barbearia.model.Agendamentos;
import com.bellator.bellator_barbearia.role.StatusAgendamento;
import com.bellator.bellator_barbearia.model.Usuarios;
import com.bellator.bellator_barbearia.repository.AgendamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repo;
    private final UsuarioService usuarioService;
    private final ServicoService servicoService;
    private final EmailService emailService;

    public AgendamentoService(AgendamentoRepository repo, UsuarioService usuarioService,
            ServicoService servicoService, EmailService emailService) {
        this.repo = repo;
        this.usuarioService = usuarioService;
        this.servicoService = servicoService;
        this.emailService = emailService;
    }

    public Agendamentos criar(String clienteEmail, AgendamentoCreateRequest req) {
        Usuarios cliente = usuarioService.byEmail(clienteEmail);
        Usuarios barbeiro = usuarioService.byId(req.barbeiroId);

        if (!"BARBEIRO".equals(barbeiro.getRole().name())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Usuário selecionado não é barbeiro");
        }

        if (repo.existsByBarbeiroAndDataAndHorario(barbeiro, req.data, req.horario)) {
            throw new ApiException(HttpStatus.CONFLICT, "Horário já ocupado");
        }

        Agendamentos a = new Agendamentos();
        a.setCliente(cliente);
        a.setBarbeiro(barbeiro);
        a.setServicos(servicoService.byId(req.servicoId));
        a.setData(req.data);
        a.setHorario(req.horario);
        a.setStatus(StatusAgendamento.AGENDADO);
        Agendamentos salvo = repo.save(a);

        // Enviar email de confirmação
        emailService.enviarEmailConfirmacaoAgendamento(cliente.getEmail(), cliente.getNome(), salvo.getData(),
                salvo.getHorario());

        return salvo;
    }

    public List<Agendamentos> meus(String clienteEmail) {
        Usuarios cliente = usuarioService.byEmail(clienteEmail);
        return repo.findByClienteOrderByDataDescHorarioDesc(cliente);
    }

    public List<Agendamentos> agendaBarbeiro(String barbeiroEmail, LocalDate data) {
        Usuarios barbeiro = usuarioService.byEmail(barbeiroEmail);
        return repo.findByBarbeiroAndDataOrderByHorarioAsc(barbeiro, data);
    }

    public Agendamentos concluir(Long id, String barbeiroEmail) {
        Agendamentos a = repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
        if (!a.getBarbeiro().getEmail().equalsIgnoreCase(barbeiroEmail)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Você não pode concluir esse agendamento");
        }
        a.setStatus(StatusAgendamento.CONCLUIDO);
        return repo.save(a);
    }

    public Agendamentos cancelar(Long id, String requesterEmail, boolean isAdmin) {
        Agendamentos a = repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
        boolean isClienteDono = a.getCliente().getEmail().equalsIgnoreCase(requesterEmail);
        if (!isAdmin && !isClienteDono) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Você não pode cancelar esse agendamento");
        }
        a.setStatus(StatusAgendamento.CANCELADO);
        Agendamentos salvo = repo.save(a);

        // Enviar email de cancelamento
        emailService.enviarEmailCancelamentoAgendamento(a.getCliente().getEmail(), a.getCliente().getNome(),
                salvo.getData(), salvo.getHorario());

        return salvo;
    }

    public Agendamentos reagendar(Long id, LocalDate novaData, LocalTime novoHorario, String requesterEmail,
            boolean isAdmin) {
        Agendamentos a = repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        boolean isClienteDono = a.getCliente().getEmail().equalsIgnoreCase(requesterEmail);
        if (!isAdmin && !isClienteDono) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Você não pode reagendar esse agendamento");
        }

        LocalDate dataAntiga = a.getData();
        LocalTime horarioAntigo = a.getHorario();

        System.out.println("Tentativa de Reagendamento ID: " + id);
        System.out.println("Antigo: " + dataAntiga + " " + horarioAntigo);
        System.out.println("Novo:   " + novaData + " " + novoHorario);

        // Verificar disponibilidade no novo horário apenas se ele mudou
        if (!dataAntiga.equals(novaData) || !horarioAntigo.equals(novoHorario)) {
            System.out.println("O horário mudou. Verificando disponibilidade...");
            if (repo.existsByBarbeiroAndDataAndHorario(a.getBarbeiro(), novaData, novoHorario)) {
                System.out.println("CONFLITO: Horário já ocupado no banco!");
                throw new ApiException(HttpStatus.CONFLICT, "Novo horário já ocupado");
            }
        } else {
            System.out.println("O horário é o mesmo. Ignorando verificação de conflito.");
        }

        a.setData(novaData);
        a.setHorario(novoHorario);
        a.setStatus(StatusAgendamento.AGENDADO);
        Agendamentos salvo = repo.save(a);

        // Enviar email de reagendamento
        emailService.enviarEmailReagendamentoAgendamento(a.getCliente().getEmail(), a.getCliente().getNome(),
                dataAntiga, horarioAntigo, novaData, novoHorario);

        return salvo;
    }

    public List<Agendamentos> listarTodos() {
        return repo.findAll();
    }
}
