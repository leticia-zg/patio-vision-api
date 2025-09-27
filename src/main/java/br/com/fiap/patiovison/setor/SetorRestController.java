package br.com.fiap.patiovison.setor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/setor")
@RequiredArgsConstructor
public class SetorRestController {

    private final SetorService setorService;

    @GetMapping
    public List<SetorDTO> listar(@RequestParam(value = "patioId", required = false) Long patioId) {
        if (patioId != null) {
            return setorService.findByPatioId(patioId);
        }
        return setorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetorDTO> buscar(@PathVariable Long id) {
        SetorDTO dto = setorService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SetorDTO> criar(@RequestBody SetorDTO setorDTO) {
        SetorDTO salvo = setorService.save(setorDTO);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetorDTO> atualizar(@PathVariable Long id, @RequestBody SetorDTO setorDTO) {
        setorDTO.setId(id);
        SetorDTO atualizado = setorService.save(setorDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        setorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
