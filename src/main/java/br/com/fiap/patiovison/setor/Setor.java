package br.com.fiap.patiovison.setor;


import java.util.List;

import br.com.fiap.patiovison.moto.Moto;
import br.com.fiap.patiovison.patio.Patio;
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
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{setor.nome.notblank}")
    @Column(unique = true)
    private String nome;

    @NotNull(message = "{setor.capacidadeMaxima.notnull}")
    private Integer capacidadeMaxima;

    @OneToMany(mappedBy = "setor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Moto> motos;

    @ManyToOne
    @JoinColumn(name = "patio_id")
    private Patio patio;
}
