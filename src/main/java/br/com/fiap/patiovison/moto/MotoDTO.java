package br.com.fiap.patiovison.moto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotoDTO {
    private String modelo;
    private String iotIdentificador;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;

    public static MotoDTO fromEntity(Moto moto) {
        return new MotoDTO(
                moto.getModelo(),
                moto.getIotIdentificador(),
                moto.getDataEntrada(),
                moto.getDataSaida()
        );
    }
}
