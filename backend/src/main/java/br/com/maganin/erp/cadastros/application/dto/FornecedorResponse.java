package br.com.maganin.erp.cadastros.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorResponse {

    private UUID id;
    private String nome;
    private String cnpj;
    private String email;
    private Boolean ativo;
}
