package br.com.fiap.patiovison.setor;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetorRepository extends JpaRepository<Setor, Long> {
    List<Setor> findByNome(String nome);
    List<Setor> findByPatioId(Long patioId);
}