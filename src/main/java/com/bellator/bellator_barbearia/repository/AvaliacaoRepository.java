package com.bellator.bellator_barbearia.repository;

import com.bellator.bellator_barbearia.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    @Query("SELECT AVG(a.nota) FROM Avaliacao a")
    Double getMediaAvaliacoes();
}
