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

}
