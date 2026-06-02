package br.com.maganin.erp.estoque.application.service;

import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaRequest;
import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoriaService {
    CategoriaResponse criarCategoria(CategoriaRequest request);
    Page<CategoriaResponse> listarCategorias(String nome, Pageable pageable);
    CategoriaResponse buscarPorId(UUID id);
    CategoriaResponse atualizarCategoria(UUID id, CategoriaRequest request);
    CategoriaResponse alterarStatus(UUID id, Boolean ativo);
}