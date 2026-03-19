package com.bellator.bellator_barbearia.service;

import com.bellator.bellator_barbearia.dto.ServicoRequest;
import com.bellator.bellator_barbearia.exception.ApiException;
import com.bellator.bellator_barbearia.model.Servicos;
import com.bellator.bellator_barbearia.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository repo;

    public ServicoService(ServicoRepository repo) {
        this.repo = repo;
    }

    public List<Servicos> listar() {
        return repo.findAll();
    }

    public Servicos criar(ServicoRequest req) {
        Servicos s = new Servicos();
        s.setNome(req.nome);
        s.setPreco(req.preco);
        s.setDuracaoMinutos(req.duracaoMinutos);
        return repo.save(s);
    }

    public Servicos atualizar(Long id, ServicoRequest req) {
        Servicos s = repo.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
        s.setNome(req.nome);
        s.setPreco(req.preco);
        s.setDuracaoMinutos(req.duracaoMinutos);
        return repo.save(s);
    }

    public void deletar(Long id) {
        if (!repo.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Serviço não encontrado");
        }
        repo.deleteById(id);
    }

    public Servicos byId(Long id) {
        return repo.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }
}
