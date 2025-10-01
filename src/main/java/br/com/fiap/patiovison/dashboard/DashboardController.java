package br.com.fiap.patiovison.dashboard;

import br.com.fiap.patiovison.patio.Patio;
import br.com.fiap.patiovison.patio.PatioService;
import br.com.fiap.patiovison.setor.Setor;
import br.com.fiap.patiovison.user.User;
import br.com.fiap.patiovison.user.UserService;
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
public class DashboardController {

    private final PatioService patioService;
    private final UserService userService;

    public DashboardController(PatioService patioService, UserService userService) {
        this.patioService = patioService;
        this.userService = userService;
    }

    @GetMapping("/index")
    public String index(Model model, Authentication authentication,
                        @RequestParam(name = "patioId", required = false) Long patioId) {
        
        // Busca lista de pátios para seleção
        List<Patio> patios = patioService.findAllEntities();
        model.addAttribute("patios", patios);
        
        // Adiciona informações do usuário autenticado incluindo avatar
        addUserInfoToModel(model, authentication);

        // Determina pátio selecionado
        Patio selectedPatio = determineSelectedPatio(patios, patioId);
        model.addAttribute("selectedPatio", selectedPatio);

        // Calcula ocupação dos setores se houver pátio selecionado
        if (selectedPatio != null && selectedPatio.getSetores() != null) {
            Map<Long, Integer> setorOcupacao = calculateSetorOcupacao(selectedPatio);
            model.addAttribute("setorOcupacao", setorOcupacao);
        }

        return "index";
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
        return (int) ((double) motosAtuais * 100.0 / setor.getCapacidadeMaxima());
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
                System.out.println("DEBUG OAuth2 User: ID=" + userIdentifier + ", Email=" + userEmail);
            }
            
            model.addAttribute("username", userEmail);
            
            System.out.println("DEBUG: Buscando usuário com email: " + userEmail);
            System.out.println("DEBUG: Tipo de authentication: " + authentication.getPrincipal().getClass());
            
            // Busca informações completas do usuário para obter o avatar
            User user = userService.findByEmail(userEmail);
            if (user != null) {
                System.out.println("DEBUG: Usuário encontrado - ID: " + user.getId() + ", Nome: " + user.getName() + ", Avatar: " + user.getAvatarUrl());
                if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                    model.addAttribute("avatar", user.getAvatarUrl());
                    System.out.println("DEBUG: Avatar adicionado ao modelo: " + user.getAvatarUrl());
                } else {
                    System.out.println("DEBUG: Usuário sem avatar válido");
                }
            } else {
                System.out.println("DEBUG: Usuário NÃO encontrado no banco de dados");
            }
        }
    }
}
