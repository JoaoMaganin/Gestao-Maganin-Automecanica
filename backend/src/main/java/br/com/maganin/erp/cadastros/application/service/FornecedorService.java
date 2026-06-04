package br.com.maganin.erp.cadastros.application.service;

import br.com.maganin.erp.cadastros.application.dto.FornecedorResponse;
import br.com.maganin.erp.cadastros.application.dto.FornecedorRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FornecedorService {

    FornecedorResponse criarFornecedor(FornecedorRequest request);
    Page<FornecedorResponse> listarFornecedores(String nome, Pageable pageable);
    FornecedorResponse buscarFornecedorPorId(UUID id);
    FornecedorResponse atualizarFornecedor(UUID id, FornecedorRequest request);
    FornecedorResponse alterarStatusFornecedor(UUID id, Boolean ativo);
}
