package br.com.maganinautomec.erp.service;

import br.com.maganinautomec.erp.dto.EstoqueProdutoDTO;
import br.com.maganinautomec.erp.entity.EstoqueProdutoEntity;
import br.com.maganinautomec.erp.repository.EstoqueProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueProdutoService {

    @Autowired
    private EstoqueProdutoRepository estoqueProdutoRepository;

    public List<EstoqueProdutoDTO> listarProdutos() {
        List<EstoqueProdutoEntity> produtos = estoqueProdutoRepository.findAll();
        return produtos.stream().map(EstoqueProdutoDTO::new).toList();
    }

    public void criarProduto(EstoqueProdutoDTO estoqueProduto) {
        EstoqueProdutoEntity estoqueProdutoEntity = new EstoqueProdutoEntity(estoqueProduto);
        estoqueProdutoRepository.save(estoqueProdutoEntity);
    }

    public EstoqueProdutoDTO atualizarProduto(EstoqueProdutoDTO estoqueProduto) {
        EstoqueProdutoEntity produtoExistente = estoqueProdutoRepository.findById(estoqueProduto.getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoExistente.setNomeProduto(estoqueProduto.getNomeProduto());
        produtoExistente.setPrecoCustoProduto(estoqueProduto.getPrecoCustoProduto());
        produtoExistente.setFornecedor(estoqueProduto.getFornecedor());
        produtoExistente.setQuantidadeEstoque(estoqueProduto.getQuantidadeEstoque());
        produtoExistente.setDataCompra(estoqueProduto.getDataCompra());
        produtoExistente.setQuantidadeVendida(estoqueProduto.getQuantidadeVendida());
        produtoExistente.setPrecoVendaProduto(estoqueProduto.getPrecoVendaProduto());

        EstoqueProdutoEntity produtoAtualizado = estoqueProdutoRepository.save(produtoExistente);
        return new EstoqueProdutoDTO(produtoAtualizado);
    }

    public void deletarProduto(Long id) {
        EstoqueProdutoEntity produto = estoqueProdutoRepository.findById(id).get();
        estoqueProdutoRepository.delete(produto);
    }

    public EstoqueProdutoDTO buscaPorId(Long id) {
        return new EstoqueProdutoDTO(estoqueProdutoRepository.findById(id).get());
    }
}
