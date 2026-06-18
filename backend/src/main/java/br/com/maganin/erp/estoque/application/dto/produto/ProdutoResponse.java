package br.com.maganin.erp.estoque.application.dto.produto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {

    private UUID id;
    private String codigo;
    private String nome;
    private String descricao;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private Integer quantidade;
    private Integer estoqueMinimo;
    private String unidade;
    private CategoriaInfo categoria;

    /*
      classe estática interna — existe só para carregar as informações de categoria dentro do response do produto.
      No frontend mostra o nome da categoria na tabela sem precisar de uma segunda requisição.
    */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoriaInfo {
        private UUID id;
        private String nome;
    }
}
