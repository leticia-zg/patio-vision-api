package br.com.fiap.patiovison.patio;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class PatioService {

    private final PatioRepository patioRepository;

    public List<PatioDTO> findAll() {
        return patioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PatioDTO findById(Long id) {
        return patioRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
    }

    public PatioDTO save(PatioDTO dto) {
        Patio patio;
        if(dto.getId() != null) {
            // Atualização
            patio = patioRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
            patio.setNome(dto.getNome());
        } else {
            // Criação
            patio = toEntity(dto);
        }

        Patio saved = patioRepository.save(patio);
        return toDTO(saved);
    }

    public void delete(Long id) {
        patioRepository.deleteById(id);
    }

    private PatioDTO toDTO(Patio patio) {
        return PatioDTO.builder()
                .id(patio.getId())
                .nome(patio.getNome())
                .build();
    }

    private Patio toEntity(PatioDTO dto) {
        Patio patio = new Patio();
        patio.setNome(dto.getNome());
        return patio;
    }

    public List<Patio> findAllEntities() {
        return patioRepository.findAll(); // retorna entidades completas com setores e motos
    }

    public Patio findByIdEntity(Long id) {
        return patioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));
    }
}
