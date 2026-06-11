package br.com.maganin.erp.estoque.application.dto.produto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class EntradaEstoqueRequest {

    @NotNull
    private UUID produtoId;

    @NotNull
    private UUID fornecedorId;

    @NotNull
    @Min(1)
    private Integer quantidade;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precoCusto;
}
