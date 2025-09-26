package br.com.fiap.patiovison.moto;

import java.time.LocalDateTime;

import br.com.fiap.patiovison.setor.Setor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{moto.modelo.notblank}")
    private String modelo;

    @NotBlank(message = "{moto.iotIdentificador.notblank}")
    @Column(unique = true)
    private String iotIdentificador;

    @NotNull(message = "{moto.dataEntrada.notnull}")
    private LocalDateTime dataEntrada;

    private LocalDateTime dataSaida;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private Setor setor;
}
