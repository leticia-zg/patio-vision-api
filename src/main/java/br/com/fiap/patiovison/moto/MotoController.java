package br.com.fiap.patiovison.moto;

import br.com.fiap.patiovison.setor.SetorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/moto")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final SetorService setorService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("motos", motoService.findAll());
        return "moto/index";
    }

    @GetMapping("/form")
    public String form(MotoDTO motoDTO, Model model) {
        model.addAttribute("setores", setorService.findAll());
        return "moto/form";
    }

    @PostMapping("/form")
    public String save(@Valid MotoDTO motoDTO, BindingResult result, RedirectAttributes redirect) {
        if(result.hasErrors()) return "moto/form";
        motoService.save(motoDTO);
        redirect.addFlashAttribute("message", "Moto salva com sucesso!");
        return "redirect:/moto";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        MotoDTO dto = motoService.findById(id);
        model.addAttribute("motoDTO", dto);
        model.addAttribute("setores", setorService.findAll());
        return "moto/form";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        motoService.delete(id);
        redirect.addFlashAttribute("message", "Moto removida com sucesso!");
        return "redirect:/moto";
    }
}
