package br.com.fiap.patiovison.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;
}
