package br.com.maganin.erp.estoque.application.mapper;

import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaRequest;
import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaResponse;
import br.com.maganin.erp.estoque.domain.CategoriaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaResponse toResponse(CategoriaEntity entity);
    CategoriaEntity toEntity(CategoriaRequest request);

    // atualiza a entidade existente com os dados do request
    void updateEntityFromRequest(CategoriaRequest request, @MappingTarget CategoriaEntity entity);
}