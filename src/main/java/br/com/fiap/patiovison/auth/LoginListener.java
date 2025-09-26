package br.com.fiap.patiovison.auth;

import br.com.fiap.patiovison.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final UserService userService;

    public LoginListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        OAuth2User principal = (OAuth2User) event.getAuthentication().getPrincipal();
        log.info("Logado com usuário " + principal);
        userService.register(principal);
    }
}
