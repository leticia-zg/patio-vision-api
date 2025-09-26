package br.com.fiap.patiovison.moto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotoService {

    private final MotoRepository motoRepository;

    // Retorna todas as motos como DTO
    public List<MotoDTO> findAll() {
        return motoRepository.findAll()
                .stream()
                .map(MotoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Busca uma moto pelo id
    public MotoDTO findById(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        return MotoDTO.fromEntity(moto);
    }

    // Salva ou atualiza uma moto a partir do DTO
    public MotoDTO save(MotoDTO dto) {
        Moto moto = toEntity(dto);
        Moto saved = motoRepository.save(moto);
        return MotoDTO.fromEntity(saved);
    }

    // Remove uma moto pelo id
    public void delete(Long id) {
        motoRepository.deleteById(id);
    }

    // Converte DTO para entidade
    private Moto toEntity(MotoDTO dto) {
        Moto moto = new Moto();
        moto.setModelo(dto.getModelo());
        moto.setIotIdentificador(dto.getIotIdentificador());
        moto.setDataEntrada(dto.getDataEntrada());
        moto.setDataSaida(dto.getDataSaida());
        return moto;
    }
}
