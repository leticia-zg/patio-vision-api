package br.com.fiap.patiovison.patio;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatioRepository extends JpaRepository<Patio, Long> {
    Optional<Patio> findByNome(String nome);
    
    @Query("SELECT p FROM Patio p LEFT JOIN FETCH p.setores")
    List<Patio> findAllWithSetores();
}
