package com.bellator.bellator_barbearia.service;

import com.bellator.bellator_barbearia.model.Avaliacao;
import com.bellator.bellator_barbearia.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository repo;

    public AvaliacaoService(AvaliacaoRepository repo) {
        this.repo = repo;
    }

    public Avaliacao salvar(Avaliacao a) {
        return repo.save(a);
    }

    public Double buscarMedia() {
        Double media = repo.getMediaAvaliacoes();
        return media != null ? media : 0.0;
    }
}
