package br.com.maganin.erp.cadastros.api;

import br.com.maganin.erp.cadastros.application.dto.FornecedorRequest;
import br.com.maganin.erp.cadastros.application.dto.FornecedorResponse;
import br.com.maganin.erp.cadastros.application.service.FornecedorService;
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
@RequestMapping(value = "api/fornecedores")
public class FornecedorController {

    public final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService service) {
        this.fornecedorService = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> criarFornecedor(@Valid @RequestBody FornecedorRequest request){
        FornecedorResponse novoFornecedor = fornecedorService.criarFornecedor(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(novoFornecedor, "Fornecedor criado com sucesso."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listarFornecedores(
            @RequestParam(required = false) String nome,
            @PageableDefault(page = 0, size = 10, sort = "nome") Pageable pageable
    ) {
        Page<FornecedorResponse> pageFornecedor = fornecedorService.listarFornecedores(nome, pageable);

        return ResponseEntity.ok(ApiResponse.ok(pageFornecedor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FornecedorResponse>> buscarFornecedorPorId(@PathVariable UUID id) {

        FornecedorResponse fornecedor = fornecedorService.buscarFornecedorPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(fornecedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FornecedorResponse>> atualizarFornecedor(@PathVariable UUID id, @RequestBody FornecedorRequest request) {
        FornecedorResponse fornecedorAtualizado = fornecedorService.atualizarFornecedor(id, request);
        return ResponseEntity.ok(ApiResponse.ok(fornecedorAtualizado));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ApiResponse<FornecedorResponse>> ativarCategoria(@PathVariable UUID id) {
        FornecedorResponse fornecedorAtivado = fornecedorService.alterarStatusFornecedor(id, true);

        return ResponseEntity.ok(ApiResponse.ok(fornecedorAtivado));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ApiResponse<FornecedorResponse>> desativarCategoria(@PathVariable UUID id) {
        FornecedorResponse fornecedorDesativado = fornecedorService.alterarStatusFornecedor(id, false);

        return ResponseEntity.ok(ApiResponse.ok(fornecedorDesativado));
    }
}
