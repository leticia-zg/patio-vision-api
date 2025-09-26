package br.com.fiap.patiovison.auth;

import br.com.fiap.patiovison.user.RegistrationDTO;
import br.com.fiap.patiovison.user.UserService;
import br.com.fiap.patiovison.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("registration", new RegistrationDTO());
        // Limpa mensagem antiga se chegou sem erro
        model.addAttribute("LOGIN_ERROR_MESSAGE", null);
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registration") RegistrationDTO registrationDTO,
                           BindingResult bindingResult,
                           Model model){
        if (bindingResult.hasErrors()){
            return "login";
        }
        var existing = userRepository.findByEmail(registrationDTO.getEmail()).orElse(null);
        if (existing != null && existing.getPassword() != null && !existing.getPassword().isBlank()) {
            model.addAttribute("errorRegister", "Email já cadastrado. Faça login.");
            return "login";
        }
        userService.registerForm(registrationDTO);
        model.addAttribute("success", "Conta criada. Faça login");
        model.addAttribute("registration", new RegistrationDTO());
        return "login";
    }
}