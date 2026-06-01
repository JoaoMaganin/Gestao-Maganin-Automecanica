package br.com.maganin.erp.estoque.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProdutoRequest {

    @NotBlank
    private String codigo;

    @NotBlank
    private String nome;

    private String descricao;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precoCusto;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precoVenda;

    @NotNull
    @Min(0)
    private Integer quantidade;

    @NotNull
    @Min(0)
    private Integer estoqueMinimo;

    @NotBlank
    private String unidade;

    @NotNull
    private UUID categoriaId;
}
