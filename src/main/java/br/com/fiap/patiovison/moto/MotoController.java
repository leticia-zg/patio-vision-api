package br.com.fiap.patiovison.moto;

import br.com.fiap.patiovison.helper.AppConstants;
import br.com.fiap.patiovison.helper.BaseController;
import br.com.fiap.patiovison.setor.SetorService;
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
 * Extende BaseController para reutilizar funcionalidades comuns.
 */
@Controller
@RequestMapping("/moto")
@RequiredArgsConstructor
public class MotoController extends BaseController {

    private final MotoService motoService;
    private final SetorService setorService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        model.addAttribute(AppConstants.ATTR_MOTOS, motoService.findAll());
        addPrincipal(model, authentication);
        return AppConstants.VIEW_MOTO_INDEX;
    }

    @GetMapping("/form")
    public String form(MotoDTO motoDTO, Model model, Authentication authentication) {
        model.addAttribute(AppConstants.ATTR_MOTO, motoDTO);
        addSetoresToModel(model);
        addPrincipal(model, authentication);
        return AppConstants.VIEW_MOTO_FORM;
    }

    @PostMapping("/form")
    public String save(@Valid MotoDTO motoDTO, BindingResult result, 
                       RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(AppConstants.ATTR_MOTO, motoDTO);
            addSetoresToModel(model);
            addPrincipal(model, authentication);
            return AppConstants.VIEW_MOTO_FORM;
        }
        
        motoService.save(motoDTO);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_MOTO_SALVA);
        return AppConstants.REDIRECT_MOTO;
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        MotoDTO dto = motoService.findById(id);
        model.addAttribute(AppConstants.ATTR_MOTO, dto);
        addSetoresToModel(model);
        addPrincipal(model, authentication);
        return AppConstants.VIEW_MOTO_FORM;
    }

    @PutMapping("/{id}")
    public String update(@Valid MotoDTO motoDTO, BindingResult result, 
                         RedirectAttributes redirect, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(AppConstants.ATTR_MOTO, motoDTO);
            addSetoresToModel(model);
            addPrincipal(model, authentication);
            return AppConstants.VIEW_MOTO_FORM;
        }
        
        motoService.save(motoDTO);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, "Moto atualizada com sucesso!");
        return AppConstants.REDIRECT_MOTO;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        motoService.delete(id);
        redirect.addFlashAttribute(AppConstants.ATTR_MESSAGE, AppConstants.MSG_MOTO_REMOVIDA);
        return AppConstants.REDIRECT_MOTO;
    }

    /**
     * Método auxiliar para adicionar lista de setores ao modelo.
     * Evita duplicação de código nos métodos de formulário.
     */
    private void addSetoresToModel(Model model) {
        model.addAttribute(AppConstants.ATTR_SETORES, setorService.findAll());
    }
}
