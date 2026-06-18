package br.com.maganin.erp.estoque.application.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponse {

    private UUID id;
    private String nome;
    private String descricao;
    private Boolean ativo;
}
