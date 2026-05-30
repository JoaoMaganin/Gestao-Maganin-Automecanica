package br.com.maganinautomec.erp.controller;

import br.com.maganinautomec.erp.dto.EstoqueProdutoDTO;
import br.com.maganinautomec.erp.service.EstoqueProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/estoqueProduto")
@CrossOrigin
public class EstoqueProdutoController {

    @Autowired
    public EstoqueProdutoService estoqueProdutoService;

    @GetMapping
    public List<EstoqueProdutoDTO> listarProdutos() {
        return estoqueProdutoService.listarProdutos();
    }

    @PostMapping
    public void criarProduto(@RequestBody EstoqueProdutoDTO produto) {
        estoqueProdutoService.criarProduto(produto);
    }

    @PutMapping
    public void atualizarProduto(@RequestBody EstoqueProdutoDTO produto) {
        estoqueProdutoService.atualizarProduto(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        estoqueProdutoService.deletarProduto(id);
        return ResponseEntity.ok().build();
    }
}
