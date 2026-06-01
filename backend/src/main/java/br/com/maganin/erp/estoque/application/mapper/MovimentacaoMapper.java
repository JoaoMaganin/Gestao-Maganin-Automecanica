package br.com.maganin.erp.estoque.application.mapper;

import br.com.maganin.erp.estoque.application.dto.movimentacao.MovimentacaoRequest;
import br.com.maganin.erp.estoque.application.dto.movimentacao.MovimentacaoResponse;
import br.com.maganin.erp.estoque.domain.MovimentacaoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovimentacaoMapper {
    MovimentacaoResponse toResponse(MovimentacaoEntity entity);
    MovimentacaoEntity toEntity(MovimentacaoRequest request);
}
