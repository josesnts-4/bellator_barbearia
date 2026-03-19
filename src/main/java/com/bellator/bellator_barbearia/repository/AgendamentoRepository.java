package com.bellator.bellator_barbearia.repository;

import com.bellator.bellator_barbearia.model.Agendamentos;
import com.bellator.bellator_barbearia.role.StatusAgendamento;
import com.bellator.bellator_barbearia.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamentos, Long> {
    boolean existsByBarbeiroAndDataAndHorario(Usuarios barbeiro, LocalDate data, LocalTime horario);

    List<Agendamentos> findByClienteOrderByDataDescHorarioDesc(Usuarios cliente);

    List<Agendamentos> findByBarbeiroAndDataOrderByHorarioAsc(Usuarios barbeiro, LocalDate data);

    List<Agendamentos> findByBarbeiroAndDataAndStatusNotOrderByHorarioAsc(Usuarios barbeiro, LocalDate data, StatusAgendamento status);
}
