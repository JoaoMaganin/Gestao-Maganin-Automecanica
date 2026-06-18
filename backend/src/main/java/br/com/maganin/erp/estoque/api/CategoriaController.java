package br.com.maganin.erp.estoque.api;

import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaRequest;
import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaResponse;
import br.com.maganin.erp.estoque.application.service.CategoriaService;
import br.com.maganin.erp.estoque.domain.CategoriaEntity;
import br.com.maganin.erp.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> criarCategoria(@Valid @RequestBody CategoriaRequest categoriaRequest) {
        CategoriaResponse novaCategoria = categoriaService.criarCategoria(categoriaRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(novaCategoria, "Categoria criada com sucesso!"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoriaResponse>>> listarCategorias(
            @RequestParam(required = false) String nome,
            @PageableDefault(page = 0, size = 10, sort = "nome") Pageable pageable
    ) {

        Page<CategoriaResponse> pageCategorias = categoriaService.listarCategorias(nome, pageable);
        return ResponseEntity.ok(ApiResponse.ok(pageCategorias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponse>> buscarCategoriaPorId(@PathVariable UUID id) {

        CategoriaResponse categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponse>> atualizarCategoria(@PathVariable UUID id, @Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse categoriaAtualizada = categoriaService.atualizarCategoria(id, request);
        return ResponseEntity.ok(ApiResponse.ok(categoriaAtualizada, "Categoria atualizada com sucesso!"));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ApiResponse<CategoriaResponse>> ativarCategoria(@PathVariable UUID id) {
        CategoriaResponse categoriaAtivada = categoriaService.alterarStatus(id, true);

        return ResponseEntity.ok(ApiResponse.ok(categoriaAtivada, "Categoria ativada com sucesso!"));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ApiResponse<CategoriaResponse>> desativarCategoria(@PathVariable UUID id) {
        CategoriaResponse categoriaAtivada = categoriaService.alterarStatus(id, false);

        return ResponseEntity.ok(ApiResponse.ok(categoriaAtivada, "Categoria desativada com sucesso!"));
    }
}
