package br.com.maganin.erp.estoque.api;

import br.com.maganin.erp.estoque.application.dto.produto.EntradaEstoqueRequest;
import br.com.maganin.erp.estoque.application.dto.produto.EntradaEstoqueResponse;
import br.com.maganin.erp.estoque.application.dto.produto.ProdutoRequest;
import br.com.maganin.erp.estoque.application.dto.produto.ProdutoResponse;
import br.com.maganin.erp.estoque.application.service.ProdutoService;
import br.com.maganin.erp.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService service) {
        this.produtoService = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProdutoResponse>> criarProduto(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse novoProduto = produtoService.criarProduto(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(novoProduto, "Produto criado com sucesso!"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProdutoResponse>>> listarProdutos(
            @RequestParam(required = false) String nome,
            @PageableDefault(page = 0, size = 10, sort = "nome") Pageable pageable
    ) {
        Page<ProdutoResponse> pageProdutos = produtoService.listarProdutos(nome, pageable);
        return ResponseEntity.ok(ApiResponse.ok(pageProdutos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoResponse>> buscarProdutoPorId(@PathVariable UUID id) {
        ProdutoResponse produto = produtoService.buscarProdutoPorId(id);

        return ResponseEntity.ok(ApiResponse.ok(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoResponse>> atualizarProduto(@PathVariable UUID id, @Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse produtoAtualizado = produtoService.atualizarProduto(id, request);

        return ResponseEntity.ok(ApiResponse.ok(produtoAtualizado, "Produto atualizado com sucesso!"));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ApiResponse<ProdutoResponse>> ativarProduto(@PathVariable UUID id) {
        ProdutoResponse produtoAtivado = produtoService.alterarStatus(id, true);

        return ResponseEntity.ok(ApiResponse.ok(produtoAtivado, "Produto ativado com sucesso!"));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ApiResponse<ProdutoResponse>> desativarProduto(@PathVariable UUID id) {
        ProdutoResponse produtoAtivado = produtoService.alterarStatus(id, false);

        return ResponseEntity.ok(ApiResponse.ok(produtoAtivado, "Produto desativado com sucesso!"));
    }

    @PostMapping("/entrada")
    public ResponseEntity<ApiResponse<EntradaEstoqueResponse>> registrarEntrada(
            @Valid @RequestBody EntradaEstoqueRequest request) {

        EntradaEstoqueResponse entradaEstoque = produtoService.registarEntrada(request);
        return ResponseEntity.ok(ApiResponse.ok(entradaEstoque, "Entrada registrada com sucesso!"));
    }
}
