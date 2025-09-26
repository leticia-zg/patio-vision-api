package br.com.fiap.patiovison.dashboard;

import br.com.fiap.patiovison.helper.AppConstants;
import br.com.fiap.patiovison.helper.BaseController;
import br.com.fiap.patiovison.patio.Patio;
import br.com.fiap.patiovison.patio.PatioService;
import br.com.fiap.patiovison.setor.Setor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller responsável pelo dashboard principal da aplicação.
 * Exibe informações de ocupação dos setores por pátio.
 */
@Controller
public class DashboardController extends BaseController {

    private final PatioService patioService;

    public DashboardController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping("/index")
    public String index(Model model, Authentication authentication,
                        @RequestParam(name = "patioId", required = false) Long patioId) {
        
        // Busca lista de pátios para seleção
        List<Patio> patios = patioService.findAllEntities();
        model.addAttribute(AppConstants.ATTR_PATIOS, patios);
        
        // Adiciona informações do usuário autenticado
        addPrincipal(model, authentication);

        // Determina pátio selecionado
        Patio selectedPatio = determineSelectedPatio(patios, patioId);
        model.addAttribute("selectedPatio", selectedPatio);

        // Calcula ocupação dos setores se houver pátio selecionado
        if (selectedPatio != null && selectedPatio.getSetores() != null) {
            Map<Long, Integer> setorOcupacao = calculateSetorOcupacao(selectedPatio);
            model.addAttribute("setorOcupacao", setorOcupacao);
        }

        return AppConstants.VIEW_INDEX;
    }

    /**
     * Determina qual pátio deve ser selecionado no dashboard.
     * @param patios Lista de todos os pátios disponíveis
     * @param patioId ID do pátio específico (pode ser null)
     * @return Pátio selecionado ou primeiro da lista se nenhum especificado
     */
    private Patio determineSelectedPatio(List<Patio> patios, Long patioId) {
        if (patioId != null) {
            return patioService.findByIdEntity(patioId);
        } else if (!patios.isEmpty()) {
            return patios.get(0); // Seleciona primeiro pátio por padrão
        }
        return null;
    }

    /**
     * Calcula a porcentagem de ocupação de cada setor do pátio.
     * @param patio Pátio selecionado
     * @return Mapa com ID do setor e porcentagem de ocupação
     */
    private Map<Long, Integer> calculateSetorOcupacao(Patio patio) {
        return patio.getSetores().stream()
                .collect(Collectors.toMap(
                        Setor::getId,
                        this::calculateOcupacaoPercentual
                ));
    }

    /**
     * Calcula a porcentagem de ocupação de um setor específico.
     * @param setor Setor para calcular ocupação
     * @return Porcentagem de ocupação (0-100)
     */
    private Integer calculateOcupacaoPercentual(Setor setor) {
        if (setor.getCapacidadeMaxima() == 0) {
            return 0;
        }
        
        int motosAtuais = setor.getMotos() != null ? setor.getMotos().size() : 0;
        return (int) ((double) motosAtuais * AppConstants.PERCENTUAL_MULTIPLICADOR / setor.getCapacidadeMaxima());
    }
}
