package br.com.fiap.patiovison.patio;

import br.com.fiap.patiovison.helper.AppConstants;
import br.com.fiap.patiovison.helper.BaseController;
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
 * Extende BaseController para reutilizar funcionalidades comuns.
 */
@Controller
@RequestMapping("/patio")
@RequiredArgsConstructor
public class PatioController extends BaseController {

    private final PatioService patioService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        model.addAttribute(AppConstants.ATTR_PATIOS, patioService.findAll());
        addPrincipal(model, authentication);
        return AppConstants.VIEW_PATIO_INDEX;
    }

    @GetMapping("/form")
    public String form(PatioDTO patioDTO, Model model, Authentication authentication) {
        model.addAttribute(AppConstants.ATTR_PATIO, patioDTO);
        addPrincipal(model, authentication);
        return AppConstants.VIEW_PATIO_FORM;
    }

    @PostMapping("/form")
    public String save(@Valid PatioDTO patioDTO, BindingResult result, 
                       RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(AppConstants.ATTR_PATIO, patioDTO);
            addPrincipal(model, authentication);
            return AppConstants.VIEW_PATIO_FORM;
        }
        
        patioService.save(patioDTO);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_PATIO_SALVO);
        return AppConstants.REDIRECT_PATIO;
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        PatioDTO dto = patioService.findById(id);
        model.addAttribute(AppConstants.ATTR_PATIO, dto);
        addPrincipal(model, authentication);
        return AppConstants.VIEW_PATIO_FORM;
    }

    @PutMapping("/{id}")
    public String update(@Valid PatioDTO patioDTO, BindingResult result, 
                         RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(AppConstants.ATTR_PATIO, patioDTO);
            addPrincipal(model, authentication);
            return AppConstants.VIEW_PATIO_FORM;
        }
        
        patioService.save(patioDTO);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_PATIO_ATUALIZADO);
        return AppConstants.REDIRECT_PATIO;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        patioService.delete(id);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_PATIO_REMOVIDO);
        return AppConstants.REDIRECT_PATIO;
    }
}
