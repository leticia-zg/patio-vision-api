package br.com.fiap.patiovison.patio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patio")
@RequiredArgsConstructor
public class PatioRestController {

    private final PatioService patioService;

    @GetMapping
    public List<PatioDTO> listar() {
        return patioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatioDTO> buscar(@PathVariable Long id) {
        PatioDTO dto = patioService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PatioDTO> criar(@RequestBody PatioDTO patioDTO) {
        PatioDTO salvo = patioService.save(patioDTO);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatioDTO> atualizar(@PathVariable Long id, @RequestBody PatioDTO patioDTO) {
        patioDTO.setId(id);
        PatioDTO atualizado = patioService.save(patioDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        patioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
