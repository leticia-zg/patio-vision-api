package br.com.fiap.patiovison.moto;

import br.com.fiap.patiovison.setor.SetorService;
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
 * Controller responsável pelo gerenciamento de motos.
 */
@Controller
@RequestMapping("/moto")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final SetorService setorService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        model.addAttribute("motos", motoService.findAll());
        addUserInfoToModel(model, authentication);
        return "moto/index";
    }

    @GetMapping("/form")
    public String form(MotoDTO motoDTO, Model model, Authentication authentication) {
        model.addAttribute("moto", motoDTO);
        addSetoresToModel(model);
        addUserInfoToModel(model, authentication);
        return "moto/form";
    }

    @PostMapping("/form")
    public String save(@Valid MotoDTO motoDTO, BindingResult result, 
                       RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("moto", motoDTO);
            addSetoresToModel(model);
            addUserInfoToModel(model, authentication);
            return "moto/form";
        }
        
        motoService.save(motoDTO);
        redirect.addFlashAttribute("message", "Moto salva com sucesso!");
        return "redirect:/moto";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        MotoDTO dto = motoService.findById(id);
        model.addAttribute("moto", dto);
        addSetoresToModel(model);
        addUserInfoToModel(model, authentication);
        return "moto/form";
    }

    @PutMapping("/{id}")
    public String update(@Valid MotoDTO motoDTO, BindingResult result, 
                         RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("moto", motoDTO);
            addSetoresToModel(model);
            addUserInfoToModel(model, authentication);
            return "moto/form";
        }
        
        motoService.save(motoDTO);
        redirect.addFlashAttribute("message", "Moto atualizada com sucesso!");
        return "redirect:/moto";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        motoService.delete(id);
        redirect.addFlashAttribute("message", "Moto removida com sucesso!");
        return "redirect:/moto";
    }

    /**
     * Método auxiliar para adicionar lista de setores ao modelo.
     * Evita duplicação de código nos métodos de formulário.
     */
    private void addSetoresToModel(Model model) {
        model.addAttribute("setores", setorService.findAll());
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
