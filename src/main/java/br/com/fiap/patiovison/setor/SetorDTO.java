package br.com.fiap.patiovison.setor;


import br.com.fiap.patiovison.moto.MotoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetorDTO {

    private Long id;

    @NotBlank(message = "{setor.nome.notblank}")
    private String nome;

    @NotNull(message = "{setor.capacidadeMaxima.notnull}")
    private Integer capacidadeMaxima;

    @NotNull(message = "{setor.patio.notnull}")
    private Long patioId;

    // Nome do pátio para exibição na lista
    private String patioNome;

    private List<MotoDTO> motos;

    // Converte a entidade Setor para o DTO
    public static SetorDTO fromEntity(Setor setor) {
        return SetorDTO.builder()
                .id(setor.getId())
                .nome(setor.getNome())
                .capacidadeMaxima(setor.getCapacidadeMaxima())
                .patioId(setor.getPatio() != null ? setor.getPatio().getId() : null)
                .patioNome(setor.getPatio() != null ? setor.getPatio().getNome() : null)
                .motos(setor.getMotos() != null ?
                        setor.getMotos().stream()
                                .map(MotoDTO::fromEntity)
                                .collect(Collectors.toList())
                        : null)
                .build();
    }
}

