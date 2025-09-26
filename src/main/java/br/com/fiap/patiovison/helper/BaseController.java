package br.com.fiap.patiovison.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

/**
 * Classe base para controllers que centraliza funcionalidades comuns
 * como adição de informações do usuário autenticado ao modelo.
 */
public abstract class BaseController {

    /**
     * Adiciona informações do principal (usuário autenticado) ao modelo.
     * Extrai avatar/foto do perfil OAuth2 quando disponível.
     * 
     * @param model Model do Spring MVC
     * @param authentication Informações de autenticação
     */
    protected void addPrincipal(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
        
        Object principal = authentication.getPrincipal();
        model.addAttribute("user", principal);
        
        if (principal instanceof OAuth2User oAuth2User) {
            Object picture = oAuth2User.getAttributes().get("picture");
            Object avatar = picture != null ? picture : oAuth2User.getAttributes().get("avatar_url");
            
            if (avatar != null) {
                model.addAttribute("avatar", avatar.toString());
            }
        }
    }
}