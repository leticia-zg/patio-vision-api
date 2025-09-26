package br.com.fiap.patiovison.setor;


import br.com.fiap.patiovison.patio.Patio;
import br.com.fiap.patiovison.patio.PatioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SetorService {

    private final SetorRepository setorRepository;
    private final PatioRepository patioRepository;

    public List<SetorDTO> findAll() {
        return setorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SetorDTO> findByPatioId(Long patioId) {
        return setorRepository.findByPatioId(patioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SetorDTO findById(Long id) {
        return setorRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Setor não encontrado"));
    }

    public SetorDTO save(SetorDTO dto) {
        Setor setor = toEntity(dto);
        Setor saved = setorRepository.save(setor);
        return toDTO(saved);
    }

    public void delete(Long id) {
        setorRepository.deleteById(id);
    }

    private SetorDTO toDTO(Setor setor) {
        return SetorDTO.builder()
                .id(setor.getId())
                .nome(setor.getNome())
                .capacidadeMaxima(setor.getCapacidadeMaxima())
                .patioId(setor.getPatio() != null ? setor.getPatio().getId() : null)
                .patioNome(setor.getPatio() != null ? setor.getPatio().getNome() : null)
                .build();
    }

    private Setor toEntity(SetorDTO dto) {
        Setor setor = new Setor();
        setor.setId(dto.getId());
        setor.setNome(dto.getNome());
        setor.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        if(dto.getPatioId() != null) {
            Patio patio = patioRepository.findById(dto.getPatioId())
                    .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
            setor.setPatio(patio);
        }
        return setor;
    }
}