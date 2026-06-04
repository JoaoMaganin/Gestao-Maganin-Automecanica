package br.com.maganin.erp.cadastros.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorRequest {

    @NotBlank
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
}
