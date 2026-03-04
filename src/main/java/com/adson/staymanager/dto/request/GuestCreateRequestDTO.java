package com.adson.staymanager.dto.request;

import com.adson.staymanager.util.CPF;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


import jakarta.validation.constraints.Size;

public record GuestCreateRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        @CPF
        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        String phone,

        @NotNull(message = "Data de nascimento é obrigatória")
        LocalDate birthDate,

        @NotBlank(message = "CEP é obrigatório")
        String cep,

        @NotBlank(message = "Rua é obrigatória")
        String street,

        @NotNull(message = "Número é obrigatório")
        Integer number,

        @NotBlank(message = "Bairro é obrigatório")
        String district,

        @NotBlank(message = "Cidade é obrigatória")
        String city,

        @NotBlank(message = "Estado é obrigatório")
        String state
) {
    
}
