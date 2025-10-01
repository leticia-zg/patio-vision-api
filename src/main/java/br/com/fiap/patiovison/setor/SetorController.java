package br.com.fiap.patiovison.setor;

import br.com.fiap.patiovison.patio.PatioService;
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
 * Controller responsável pelo gerenciamento de setores.
 */
@Controller
@RequestMapping("/setor")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService setorService;
    private final PatioService patioService;
    private final UserService userService;

    @GetMapping
    public String index(@RequestParam(value = "patioId", required = false) Long patioId,
                        Model model, Authentication authentication) {
        
        // Aplica filtro por pátio se especificado
        var setores = patioId != null ? 
            setorService.findByPatioId(patioId) : 
            setorService.findAll();
            
        model.addAttribute("setores", setores);
        model.addAttribute("patioSelecionado", patioId);
        model.addAttribute("patios", patioService.findAll());
        addUserInfoToModel(model, authentication);
        
        return "setor/index";
    }

    @GetMapping("/form")
    public String form(SetorDTO setorDTO, Model model, Authentication authentication) {
        model.addAttribute("setor", setorDTO);
        addPatiosToModel(model);
        addUserInfoToModel(model, authentication);
        return "setor/form";
    }

    @PostMapping("/form")
    public String save(@Valid SetorDTO setorDTO, BindingResult result, 
                       RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("setor", setorDTO);
            addPatiosToModel(model);
            addUserInfoToModel(model, authentication);
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
        addPatiosToModel(model);
        addUserInfoToModel(model, authentication);
        return "setor/form";
    }

    @PutMapping("/{id}")
    public String update(@Valid SetorDTO setorDTO, BindingResult result, 
                         RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("setor", setorDTO);
            addPatiosToModel(model);
            addUserInfoToModel(model, authentication);
            return "setor/form";
        }
        
        setorService.save(setorDTO);
        redirect.addFlashAttribute("message", "Setor atualizado com sucesso!");
        return "redirect:/setor";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        setorService.delete(id);
        redirect.addFlashAttribute("message", "Setor removido com sucesso!");
        return "redirect:/setor";
    }

    /**
     * Método auxiliar para adicionar lista de pátios ao modelo.
     * Evita duplicação de código nos métodos de formulário.
     */
    private void addPatiosToModel(Model model) {
        model.addAttribute("patios", patioService.findAll());
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
