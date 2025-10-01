package br.com.fiap.patiovison.debug;

import br.com.fiap.patiovison.user.User;
import br.com.fiap.patiovison.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
@Slf4j
public class DebugController {
    
    private final UserService userService;
    
    @GetMapping("/current-user")
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        
        if (authentication != null) {
            result.put("authenticated", true);
            result.put("username", authentication.getName());
            result.put("principalType", authentication.getPrincipal().getClass().getSimpleName());
            
            User user = userService.findByEmail(authentication.getName());
            if (user != null) {
                result.put("userFound", true);
                result.put("userId", user.getId());
                result.put("userName", user.getName());
                result.put("userEmail", user.getEmail());
                result.put("avatarUrl", user.getAvatarUrl());
                result.put("hasPassword", user.getPassword() != null);
            } else {
                result.put("userFound", false);
            }
        } else {
            result.put("authenticated", false);
        }
        
        return result;
    }

    @GetMapping("/test-user-creation")
    public String testUserCreation(@RequestParam String email, 
                                  @RequestParam String name, 
                                  @RequestParam String password) {
        try {
            log.info("Testando criação de usuário: {}", email);
            
            // Verificar se usuário já existe
            boolean exists = userService.emailExists(email);
            log.info("E-mail {} já existe: {}", email, exists);
            
            if (exists) {
                return "E-mail já existe: " + email;
            }
            
            // Tentar criar usuário
            User user = userService.registerWithPassword(email, name, password);
            log.info("Usuário criado com sucesso: {}", user);
            
            return "Usuário criado com sucesso: " + user.getEmail() + " | ID: " + user.getId();
            
        } catch (Exception e) {
            log.error("Erro ao criar usuário", e);
            return "Erro: " + e.getMessage() + " | Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "N/A");
        }
    }
    
    @GetMapping("/check-user")
    public String checkUser(@RequestParam String email) {
        try {
            User user = userService.findByEmail(email);
            if (user != null) {
                return "Usuário encontrado: " + user.getEmail() + " | Nome: " + user.getName() + " | ID: " + user.getId();
            } else {
                return "Usuário não encontrado: " + email;
            }
        } catch (Exception e) {
            log.error("Erro ao buscar usuário", e);
            return "Erro: " + e.getMessage();
        }
    }
}