package br.com.fiap.patiovison.patio;

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
@RequestMapping("/patio")
@RequiredArgsConstructor
public class PatioController {

    private final PatioService patioService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        model.addAttribute("patios", patioService.findAll());
        addPrincipal(model, authentication);
        return "patio/index";
    }

    @GetMapping("/form")
    public String form(PatioDTO patioDTO, Model model, Authentication authentication) {
        model.addAttribute("patio", patioDTO);
        addPrincipal(model, authentication);
        return "patio/form";
    }

    @PostMapping("/form")
    public String save(@Valid PatioDTO patioDTO, BindingResult result, RedirectAttributes redirect,
                       Authentication authentication, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("patio", patioDTO);
            addPrincipal(model, authentication);
            return "patio/form";
        }
        patioService.save(patioDTO);
        redirect.addFlashAttribute("message", "Pátio salvo com sucesso!");
        return "redirect:/patio";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        PatioDTO dto = patioService.findById(id);
        model.addAttribute("patio", dto);
        addPrincipal(model, authentication);
        return "patio/form";
    }

    @PutMapping("/{id}")
    public String update(@Valid PatioDTO patioDTO, BindingResult result, RedirectAttributes redirect,
                         Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patio", patioDTO);
            addPrincipal(model, authentication);
            return "patio/form";
        }
        patioService.save(patioDTO);
        redirect.addFlashAttribute("message", "Pátio atualizado com sucesso!");
        return "redirect:/patio";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect, Authentication authentication) {
        patioService.delete(id);
        redirect.addFlashAttribute("message", "Pátio removido com sucesso!");
        return "redirect:/patio";
    }

    private void addPrincipal(Model model, Authentication authentication){
        if (authentication == null){
            return;
        }
        Object principal = authentication.getPrincipal();
        model.addAttribute("user", principal);
        if (principal instanceof OAuth2User oAuth2User){
            Object pic = oAuth2User.getAttributes().get("picture");
            Object avatar = pic != null ? pic : oAuth2User.getAttributes().get("avatar_url");
            if (avatar != null) model.addAttribute("avatar", avatar.toString());
        }
    }
}
