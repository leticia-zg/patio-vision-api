package br.com.fiap.patiovison.moto;

import br.com.fiap.patiovison.helper.AppConstants;
import br.com.fiap.patiovison.helper.BaseService;
import br.com.fiap.patiovison.setor.Setor;
import br.com.fiap.patiovison.setor.SetorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de motos.
 * Implementa BaseService para padronização de operações CRUD.
 */
@Service
@RequiredArgsConstructor
public class MotoService implements BaseService<Moto, MotoDTO> {

    private final MotoRepository motoRepository;
    private final SetorRepository setorRepository;

    @Override
    public List<MotoDTO> findAll() {
        return motoRepository.findAll()
                .stream()
                .map(MotoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MotoDTO findById(Long id) {
        return motoRepository.findById(id)
                .map(MotoDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException(AppConstants.ERR_MOTO_NAO_ENCONTRADA));
    }

    @Override
    public MotoDTO save(MotoDTO dto) {
        Moto moto;
        if (dto.getId() != null) {
            // Atualização - busca entidade existente
            moto = motoRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException(AppConstants.ERR_MOTO_NAO_ENCONTRADA));
            updateMotoFromDTO(moto, dto);
        } else {
            // Criação - nova entidade
            moto = toEntity(dto);
        }

        Moto saved = motoRepository.save(moto);
        return MotoDTO.fromEntity(saved);
    }

    @Override
    public void delete(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new RuntimeException(AppConstants.ERR_MOTO_NAO_ENCONTRADA);
        }
        motoRepository.deleteById(id);
    }

    @Override
    public MotoDTO toDTO(Moto moto) {
        return MotoDTO.fromEntity(moto);
    }

    @Override
    public Moto toEntity(MotoDTO dto) {
        Moto moto = new Moto();
        updateMotoFromDTO(moto, dto);
        return moto;
    }

    /**
     * Atualiza os campos da entidade Moto com base no DTO.
     * @param moto Entidade a ser atualizada
     * @param dto DTO com os novos dados
     */
    private void updateMotoFromDTO(Moto moto, MotoDTO dto) {
        moto.setModelo(dto.getModelo());
        moto.setIotIdentificador(dto.getIotIdentificador());
        moto.setDataEntrada(dto.getDataEntrada());
        moto.setDataSaida(dto.getDataSaida());
        
        // Associa setor se fornecido
        if (dto.getSetorId() != null) {
            Setor setor = setorRepository.findById(dto.getSetorId())
                    .orElseThrow(() -> new RuntimeException(AppConstants.ERR_SETOR_NAO_ENCONTRADO));
            moto.setSetor(setor);
        } else {
            moto.setSetor(null);
        }
    }
}
