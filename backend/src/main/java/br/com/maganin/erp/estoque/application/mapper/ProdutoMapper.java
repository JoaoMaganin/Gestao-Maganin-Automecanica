package br.com.maganin.erp.estoque.application.mapper;

import br.com.maganin.erp.estoque.application.dto.ProdutoRequest;
import br.com.maganin.erp.estoque.application.dto.ProdutoResponse;
import br.com.maganin.erp.estoque.domain.ProdutoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(source = "categoria.id", target = "categoria.id")
    @Mapping(source = "categoria.nome", target = "categoria.nome")
    ProdutoResponse toResponse(ProdutoEntity entity);

    @Mapping(target = "categoria", ignore = true)
    ProdutoEntity toEntity(ProdutoRequest request);
}
