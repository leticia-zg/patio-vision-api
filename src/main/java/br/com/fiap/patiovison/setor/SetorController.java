package br.com.fiap.patiovison.setor;

import br.com.fiap.patiovison.patio.PatioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/setor")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService setorService;
    private final PatioService patioService;

    @GetMapping
    public String index(@RequestParam(value = "patioId", required = false) Long patioId,
                        Model model, Authentication authentication) {
        model.addAttribute("setores", patioId != null ? setorService.findByPatioId(patioId) : setorService.findAll());
        model.addAttribute("patioSelecionado", patioId);
        model.addAttribute("patios", patioService.findAll());
        addPrincipal(model, authentication);
        return "setor/index";
    }

    @GetMapping("/form")
    public String form(SetorDTO setorDTO, Model model, Authentication authentication) {
        model.addAttribute("setor", setorDTO);
        model.addAttribute("patios", patioService.findAll());
        addPrincipal(model, authentication);
        return "setor/form";
    }

    @PostMapping("/form")
    public String save(@Valid SetorDTO setorDTO, BindingResult result, RedirectAttributes redirect,
                       Authentication authentication, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("setor", setorDTO);
            model.addAttribute("patios", patioService.findAll());
            addPrincipal(model, authentication);
            return "setor/form";
        }
        setorService.save(setorDTO);
        redirect.addFlashAttribute("message", "Setor salvo com sucesso!");
        return "redirect:/setor";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        SetorDTO dto = setorService.findById(id);
        model.addAttribute("setor", dto);
        model.addAttribute("patios", patioService.findAll());
        addPrincipal(model, authentication);
        return "setor/form";
    }

    @PutMapping("/{id}")
    public String update(@Valid SetorDTO setorDTO, BindingResult result, RedirectAttributes redirect,
                         Authentication authentication, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("setor", setorDTO);
            model.addAttribute("patios", patioService.findAll());
            addPrincipal(model, authentication);
            return "setor/form";
        }
        setorService.save(setorDTO);
        redirect.addFlashAttribute("message", "Setor atualizado com sucesso!");
        return "redirect:/setor";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect, Authentication authentication) {
        setorService.delete(id);
        redirect.addFlashAttribute("message", "Setor removido com sucesso!");
        return "redirect:/setor";
    }
    private void addPrincipal(Model model, Authentication authentication) {
        if (authentication == null) return;
        Object principal = authentication.getPrincipal();
        model.addAttribute("user", principal);
        if (principal instanceof OAuth2User oAuth2User){
            Object pic = oAuth2User.getAttributes().get("picture");
            Object avatar = pic != null ? pic : oAuth2User.getAttributes().get("avatar_url");
            if (avatar != null) model.addAttribute("avatar", avatar.toString());
        }
    }
}
