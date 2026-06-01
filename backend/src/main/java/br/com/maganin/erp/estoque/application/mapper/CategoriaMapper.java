package br.com.maganin.erp.estoque.application.mapper;

import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaRequest;
import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaResponse;
import br.com.maganin.erp.estoque.domain.CategoriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaResponse toResponse(CategoriaEntity entity);
    CategoriaEntity toEntity(CategoriaRequest request);
}