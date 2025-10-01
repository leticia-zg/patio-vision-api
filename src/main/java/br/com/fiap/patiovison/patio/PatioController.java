package br.com.fiap.patiovison.patio;

import br.com.fiap.patiovison.user.User;
import br.com.fiap.patiovison.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/**
 * Controller responsável pelo gerenciamento de pátios.
 */
@Controller
@RequestMapping("/patio")
@RequiredArgsConstructor
public class PatioController {

    private final PatioService patioService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        model.addAttribute("patios", patioService.findAll());
        addUserInfoToModel(model, authentication);
        return "patio/index";
    }

    @GetMapping("/form")
    public String form(PatioDTO patioDTO, Model model, Authentication authentication) {
        model.addAttribute("patio", patioDTO);
        addUserInfoToModel(model, authentication);
        return "patio/form";
    }

    @PostMapping("/form")
    public String save(@Valid PatioDTO patioDTO, BindingResult result, 
                       RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patio", patioDTO);
            addUserInfoToModel(model, authentication);
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
        addUserInfoToModel(model, authentication);
        return "patio/form";
    }

    @PutMapping("/{id}")
    public String update(@Valid PatioDTO patioDTO, BindingResult result, 
                         RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patio", patioDTO);
            addUserInfoToModel(model, authentication);
            return "patio/form";
        }
        
        patioService.save(patioDTO);
        redirect.addFlashAttribute("message", "Pátio atualizado com sucesso!");
        return "redirect:/patio";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        patioService.delete(id);
        redirect.addFlashAttribute("message", "Pátio removido com sucesso!");
        return "redirect:/patio";
    }

    /**
     * Adiciona informações do usuário autenticado ao modelo, incluindo avatar.
     * @param model Modelo do Spring MVC
     * @param authentication Informações de autenticação
     */
    private void addUserInfoToModel(Model model, Authentication authentication) {
        if (authentication != null) {
            String userIdentifier = authentication.getName();
            String userEmail = userIdentifier;
            
            // Para OAuth2, precisamos extrair o email dos atributos
            if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                userEmail = (String) oauth2User.getAttributes().get("email");
                if (userEmail == null) {
                    userEmail = oauth2User.getAttributes().get("login") + "@github.com";
                }
            }
            
            model.addAttribute("username", userEmail);
            
            // Busca informações completas do usuário para obter o avatar
            User user = userService.findByEmail(userEmail);
            if (user != null && user.getAvatarUrl() != null) {
                model.addAttribute("avatar", user.getAvatarUrl());
            }
        }
    }
}
