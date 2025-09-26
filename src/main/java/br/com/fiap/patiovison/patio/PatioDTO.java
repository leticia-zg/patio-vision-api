package br.com.fiap.patiovison.patio;


import br.com.fiap.patiovison.setor.SetorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatioDTO {

    private Long id;

    private String nome;

    private List<SetorDTO> setores;

    public static PatioDTO fromEntity(Patio patio) {
        return PatioDTO.builder()
                .id(patio.getId())
                .nome(patio.getNome())
                .setores(
                        patio.getSetores() != null ?
                                patio.getSetores().stream()
                                        .map(SetorDTO::fromEntity)
                                        .collect(Collectors.toList())
                                : null
                )
                .build();
    }
}

