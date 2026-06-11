package br.com.maganin.erp.estoque.application.service;

import br.com.maganin.erp.estoque.application.dto.produto.EntradaEstoqueRequest;
import br.com.maganin.erp.estoque.application.dto.produto.EntradaEstoqueResponse;
import br.com.maganin.erp.estoque.application.dto.produto.ProdutoRequest;
import br.com.maganin.erp.estoque.application.dto.produto.ProdutoResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProdutoService {
    ProdutoResponse criarProduto(ProdutoRequest request);
    Page<ProdutoResponse> listarProdutos(String nome, Pageable page);
    ProdutoResponse buscarProdutoPorId(UUID id);
    ProdutoResponse atualizarProduto(UUID id, ProdutoRequest request);
    ProdutoResponse alterarStatus(UUID id, Boolean ativo);
    EntradaEstoqueResponse registarEntrada(EntradaEstoqueRequest entrada);
    BigDecimal calcularPrecoSugerido(BigDecimal precoCusto, UUID produtoId);
}
