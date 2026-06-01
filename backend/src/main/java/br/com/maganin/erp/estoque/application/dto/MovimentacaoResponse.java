package br.com.maganin.erp.estoque.application.dto;

import br.com.maganin.erp.estoque.domain.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoResponse {

    private  UUID id;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private Integer quantidadeAnterior;
    private String motivo;

    private ProdutoInfo produto;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProdutoInfo {
        private UUID id;
        private String nome;
        private String codigo;
    }
}
