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
public class EntradaEstoqueResponse {

    private UUID produtoId;
    private String nomeProduto;
    private Integer quantidadeAnterior;
    private Integer quantidadeAtual;
    private BigDecimal precoCustoAnterior;
    private BigDecimal precoCustoMedioAtualizado;
    private BigDecimal precoVendaSugerido;
}
