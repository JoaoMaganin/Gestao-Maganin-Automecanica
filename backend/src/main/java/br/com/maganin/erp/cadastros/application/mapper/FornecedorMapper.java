package br.com.maganin.erp.cadastros.application.mapper;

import br.com.maganin.erp.cadastros.application.dto.FornecedorResponse;
import br.com.maganin.erp.cadastros.application.dto.FornecedorRequest;
import br.com.maganin.erp.cadastros.domain.FornecedorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FornecedorMapper {
    FornecedorResponse toResponse(FornecedorEntity entity);
    FornecedorEntity toEntity(FornecedorRequest resquet);

    void updateEntityFromRequest(FornecedorRequest request, @MappingTarget FornecedorEntity entity);
}
