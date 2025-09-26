package br.com.fiap.patiovison.dashboard;

import br.com.fiap.patiovison.patio.Patio;
import br.com.fiap.patiovison.patio.PatioService;
import br.com.fiap.patiovison.setor.Setor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final PatioService patioService;

    public DashboardController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping("/index")
    public String index(Model model,
                        Authentication authentication,
                        @RequestParam(name = "patioId", required = false) Long patioId) {
        // Lista de pátios para o select
        List<Patio> patios = patioService.findAllEntities(); // retorna entidades completas
        model.addAttribute("patios", patios);
        if (authentication != null){
            Object principal = authentication.getPrincipal();
            model.addAttribute("user", principal);
            if (principal instanceof OAuth2User oAuth2User){
                Object pic = oAuth2User.getAttribute("picture");
                Object avatar = pic != null ? pic : oAuth2User.getAttribute("avatar_url");
                if (avatar != null) model.addAttribute("avatarUrl", avatar.toString());
            }
        }

        // Pátio selecionado
        Patio selectedPatio = null;
        if (patioId != null) {
            selectedPatio = patioService.findByIdEntity(patioId);
        } else if (!patios.isEmpty()) {
            selectedPatio = patios.get(0); // seleciona o primeiro por padrão
        }

        model.addAttribute("selectedPatio", selectedPatio);

        if (selectedPatio != null) {
            Map<Long, Integer> setorOcupacao = selectedPatio.getSetores()
                    .stream()
                    .collect(Collectors.toMap(
                            Setor::getId,
                            s -> (int) ((double) s.getMotos().size() * 100 / s.getCapacidadeMaxima())
                    ));
            model.addAttribute("setorOcupacao", setorOcupacao);
        }


        return "index";
    }
}
