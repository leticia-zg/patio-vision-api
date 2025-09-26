package br.com.fiap.patiovison.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
    }
    public User register(OAuth2User principal) {
        var attrs = principal.getAttributes();

        String email = attrs.get("email") != null
                ? (String) attrs.get("email")
                : attrs.get("login") + "@github.com";

        String name = attrs.get("name") != null
                ? (String) attrs.get("name")
                : (String) attrs.get("login");

        String avatarUrl = attrs.get("picture") != null
                ? (String) attrs.get("picture")
                : (String) attrs.get("avatar_url");

        return userRepository
                .findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email, name, avatarUrl)));
    }

                public User registerForm(RegistrationDTO dto){
                        String normalizedEmail = dto.getEmail().toLowerCase();
                        dto.setEmail(normalizedEmail);
                        return userRepository.findByEmail(normalizedEmail)
                                        .map(existing -> {
                                                // Se o usuário veio de OAuth (sem password) permitir definir agora
                                                boolean needsUpdate = false;
                                                if (existing.getPassword() == null || existing.getPassword().isBlank()) {
                                                        existing.setPassword(passwordEncoder.encode(dto.getPassword()));
                                                        needsUpdate = true;
                                                }
                                                if (dto.getName() != null && !dto.getName().isBlank() && !dto.getName().equals(existing.getName())) {
                                                        existing.setName(dto.getName());
                                                        needsUpdate = true;
                                                }
                                                if (existing.getRole() == null) { // segurança extra
                                                        existing.setRole("ROLE_USER");
                                                        needsUpdate = true;
                                                }
                                                return needsUpdate ? userRepository.save(existing) : existing;
                                        })
                                        .orElseGet(() -> {
                                                User user = new User();
                                                user.setEmail(dto.getEmail());
                                                user.setName(dto.getName());
                                                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                                                user.setRole("ROLE_USER");
                                                return userRepository.save(user);
                                        });
                }

}
