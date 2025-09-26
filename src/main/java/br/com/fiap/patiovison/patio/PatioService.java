package br.com.fiap.patiovison.patio;

import br.com.fiap.patiovison.helper.AppConstants;
import br.com.fiap.patiovison.helper.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de pátios.
 * Implementa BaseService para padronização de operações CRUD.
 */
@Service
@RequiredArgsConstructor
public class PatioService implements BaseService<Patio, PatioDTO> {

    private final PatioRepository patioRepository;

    @Override
    public List<PatioDTO> findAll() {
        return patioRepository.findAllWithSetores()
                .stream()
                .map(PatioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PatioDTO findById(Long id) {
        return patioRepository.findById(id)
                .map(PatioDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException(AppConstants.ERR_PATIO_NAO_ENCONTRADO));
    }

    @Override
    public PatioDTO save(PatioDTO dto) {
        Patio patio;
        if (dto.getId() != null) {
            // Atualização - busca entidade existente
            patio = patioRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException(AppConstants.ERR_PATIO_NAO_ENCONTRADO));
            patio.setNome(dto.getNome());
        } else {
            // Criação - nova entidade
            patio = toEntity(dto);
        }

        Patio saved = patioRepository.save(patio);
        return PatioDTO.fromEntity(saved);
    }

    @Override
    public void delete(Long id) {
        if (!patioRepository.existsById(id)) {
            throw new RuntimeException(AppConstants.ERR_PATIO_NAO_ENCONTRADO);
        }
        patioRepository.deleteById(id);
    }

    @Override
    public PatioDTO toDTO(Patio patio) {
        return PatioDTO.fromEntity(patio);
    }

    @Override
    public Patio toEntity(PatioDTO dto) {
        Patio patio = new Patio();
        patio.setId(dto.getId());
        patio.setNome(dto.getNome());
        return patio;
    }

    // Métodos adicionais para compatibilidade com DashboardController

    /**
     * Busca todos os pátios como entidades (para o dashboard).
     * @return Lista de entidades Patio
     */
    public List<Patio> findAllEntities() {
        return patioRepository.findAllWithSetores();
    }

    /**
     * Busca pátio por ID como entidade (para o dashboard).
     * @param id ID do pátio
     * @return Entidade Patio
     */
    public Patio findByIdEntity(Long id) {
        return patioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(AppConstants.ERR_PATIO_NAO_ENCONTRADO));
    }
}
