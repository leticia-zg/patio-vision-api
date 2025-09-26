package br.com.fiap.patiovison.moto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotoRepository extends JpaRepository<Moto, Long> {
    List<Moto> findByDataSaidaIsNull();
    List<Moto> findByIotIdentificador(String iotIdentificador);
}