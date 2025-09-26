package br.com.fiap.patiovison.patio;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatioRepository extends JpaRepository<Patio, Long> {
    Optional<Patio> findByNome(String nome);
}
