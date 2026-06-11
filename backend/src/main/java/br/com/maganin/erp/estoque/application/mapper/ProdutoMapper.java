package br.com.maganin.erp.estoque.application.mapper;

import br.com.maganin.erp.estoque.application.dto.produto.ProdutoRequest;
import br.com.maganin.erp.estoque.application.dto.produto.ProdutoResponse;
import br.com.maganin.erp.estoque.domain.ProdutoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(source = "categoria.id", target = "categoria.id")
    @Mapping(source = "categoria.nome", target = "categoria.nome")
    ProdutoResponse toResponse(ProdutoEntity entity);

    @Mapping(target = "categoria", ignore = true)
    ProdutoEntity toEntity(ProdutoRequest request);

    void updateEntityFromRequest(ProdutoRequest request, @MappingTarget ProdutoEntity entity);
}
