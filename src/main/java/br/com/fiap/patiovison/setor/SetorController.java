package br.com.fiap.patiovison.setor;

import br.com.fiap.patiovison.helper.AppConstants;
import br.com.fiap.patiovison.helper.BaseController;
import br.com.fiap.patiovison.patio.PatioService;
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
 * Extende BaseController para reutilizar funcionalidades comuns.
 */
@Controller
@RequestMapping("/setor")
@RequiredArgsConstructor
public class SetorController extends BaseController {

    private final SetorService setorService;
    private final PatioService patioService;

    @GetMapping
    public String index(@RequestParam(value = "patioId", required = false) Long patioId,
                        Model model, Authentication authentication) {
        
        // Aplica filtro por pátio se especificado
        var setores = patioId != null ? 
            setorService.findByPatioId(patioId) : 
            setorService.findAll();
            
        model.addAttribute(AppConstants.ATTR_SETORES, setores);
        model.addAttribute("patioSelecionado", patioId);
        model.addAttribute(AppConstants.ATTR_PATIOS, patioService.findAll());
        addPrincipal(model, authentication);
        
        return AppConstants.VIEW_SETOR_INDEX;
    }

    @GetMapping("/form")
    public String form(SetorDTO setorDTO, Model model, Authentication authentication) {
        model.addAttribute(AppConstants.ATTR_SETOR, setorDTO);
        addPatiosToModel(model);
        addPrincipal(model, authentication);
        return AppConstants.VIEW_SETOR_FORM;
    }

    @PostMapping("/form")
    public String save(@Valid SetorDTO setorDTO, BindingResult result, 
                       RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(AppConstants.ATTR_SETOR, setorDTO);
            addPatiosToModel(model);
            addPrincipal(model, authentication);
            return AppConstants.VIEW_SETOR_FORM;
        }
        
        setorService.save(setorDTO);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_SETOR_SALVO);
        return AppConstants.REDIRECT_SETOR;
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        SetorDTO dto = setorService.findById(id);
        model.addAttribute(AppConstants.ATTR_SETOR, dto);
        addPatiosToModel(model);
        addPrincipal(model, authentication);
        return AppConstants.VIEW_SETOR_FORM;
    }

    @PutMapping("/{id}")
    public String update(@Valid SetorDTO setorDTO, BindingResult result, 
                         RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(AppConstants.ATTR_SETOR, setorDTO);
            addPatiosToModel(model);
            addPrincipal(model, authentication);
            return AppConstants.VIEW_SETOR_FORM;
        }
        
        setorService.save(setorDTO);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_SETOR_ATUALIZADO);
        return AppConstants.REDIRECT_SETOR;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        setorService.delete(id);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_SETOR_REMOVIDO);
        return AppConstants.REDIRECT_SETOR;
    }

    /**
     * Método auxiliar para adicionar lista de pátios ao modelo.
     * Evita duplicação de código nos métodos de formulário.
     */
    private void addPatiosToModel(Model model) {
        model.addAttribute(AppConstants.ATTR_PATIOS, patioService.findAll());
    }
}
