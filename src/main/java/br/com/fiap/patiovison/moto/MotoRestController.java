package br.com.fiap.patiovison.moto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moto")
@RequiredArgsConstructor
public class MotoRestController {

    private final MotoService motoService;

    @GetMapping
    public List<MotoDTO> listar() {
        return motoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoDTO> buscar(@PathVariable Long id) {
        MotoDTO dto = motoService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MotoDTO> criar(@RequestBody MotoDTO motoDTO) {
        MotoDTO salvo = motoService.save(motoDTO);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoDTO> atualizar(@PathVariable Long id, @RequestBody MotoDTO motoDTO) {
        motoDTO.setId(id);
        MotoDTO atualizado = motoService.save(motoDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        motoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


