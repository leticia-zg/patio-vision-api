package br.com.fiap.patiovison.moto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de Moto.
 * Inclui informações do setor associado para facilitar exibição.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MotoDTO {
    private Long id;
    private String modelo;
    private String iotIdentificador;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Long setorId;
    private String setorNome;

    /**
     * Converte entidade Moto para DTO.
     * @param moto Entidade Moto
     * @return MotoDTO
     */
    public static MotoDTO fromEntity(Moto moto) {
        return MotoDTO.builder()
                .id(moto.getId())
                .modelo(moto.getModelo())
                .iotIdentificador(moto.getIotIdentificador())
                .dataEntrada(moto.getDataEntrada())
                .dataSaida(moto.getDataSaida())
                .setorId(moto.getSetor() != null ? moto.getSetor().getId() : null)
                .setorNome(moto.getSetor() != null ? moto.getSetor().getNome() : null)
                .build();
    }
}
