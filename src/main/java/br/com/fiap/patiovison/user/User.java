package br.com.fiap.patiovison.user;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
@Data
@Table(name = "pvuser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String avatarUrl;

    // Fields for form login
    private String password; // BCrypt hashed

    private String role = "ROLE_USER"; // simple single role model

    public User() {
    }

    // 🔹 Construtor para Google
    public User(OAuth2User principal) {
        var attrs = principal.getAttributes();

        this.email = (String) attrs.get("email");
        this.name = (String) attrs.getOrDefault("name", (String) attrs.get("login"));
        Object avatar = attrs.get("picture") != null
                ? attrs.get("picture")
                : attrs.get("avatar_url");
        this.avatarUrl = avatar != null ? avatar.toString() : null;
    }

    // 🔹 Construtor genérico usado pelo UserService
    public User(String email, String name, String avatarUrl) {
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }
}