package br.com.maganin.erp.estoque.application.dto.movimentacao;

import br.com.maganin.erp.estoque.domain.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoRequest {

    @NotNull
    private UUID produtoId;

    @NotNull
    private TipoMovimentacao tipo;

    @NotNull
    @Min(1)
    private Integer quantidade;

    private String motivo;
}
