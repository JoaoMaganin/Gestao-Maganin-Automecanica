package br.com.maganin.erp.estoque.application.service.Impl;

import br.com.maganin.erp.cadastros.domain.FornecedorEntity;
import br.com.maganin.erp.cadastros.infrastructure.repository.FornecedorRepository;
import br.com.maganin.erp.estoque.application.dto.produto.EntradaEstoqueRequest;
import br.com.maganin.erp.estoque.application.dto.produto.EntradaEstoqueResponse;
import br.com.maganin.erp.estoque.application.dto.produto.ProdutoRequest;
import br.com.maganin.erp.estoque.application.dto.produto.ProdutoResponse;
import br.com.maganin.erp.estoque.application.mapper.ProdutoMapper;
import br.com.maganin.erp.estoque.application.service.ProdutoService;
import br.com.maganin.erp.estoque.domain.MovimentacaoEntity;
import br.com.maganin.erp.estoque.domain.ProdutoEntity;
import br.com.maganin.erp.estoque.domain.TipoMovimentacao;
import br.com.maganin.erp.estoque.infrastructure.repository.ConfiguracaoRepository;
import br.com.maganin.erp.estoque.infrastructure.repository.MovimentacaoRepository;
import br.com.maganin.erp.estoque.infrastructure.repository.ProdutoRepository;
import br.com.maganin.erp.shared.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final FornecedorRepository fornecedorRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ConfiguracaoRepository configuracaoRepository;

    public ProdutoServiceImpl(
            ProdutoRepository produtoRepository,
            ProdutoMapper produtoMapper,
            FornecedorRepository fornecedorRepository,
            MovimentacaoRepository movimentacaoRepository,
            ConfiguracaoRepository configuracaoRepostory
            ) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
        this.fornecedorRepository = fornecedorRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.configuracaoRepository = configuracaoRepostory;
    }

    @Override
    public ProdutoResponse criarProduto(ProdutoRequest request) {
        ProdutoEntity produtoNovo = produtoMapper.toEntity(request);
        produtoNovo.setAtivo(true);

        if(produtoRepository.existsByNomeIgnoreCase(produtoNovo.getNome())) {
            throw new BusinessException("Já existe o produto " + produtoNovo.getNome());
        }

        produtoNovo.setAtivo(true);
        ProdutoEntity produtoSalvo = produtoRepository.save(produtoNovo);

        return produtoMapper.toResponse(produtoSalvo);
    }

    @Override
    public Page<ProdutoResponse> listarProdutos(String nome, Pageable pageable) {
        Page<ProdutoEntity> page;

        if(nome == null || nome.isBlank()) {
            page = produtoRepository.findAll(pageable);
        } else {
            page = produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }

        return page.map(produtoMapper::toResponse);
    }

    @Override
    public ProdutoResponse buscarProdutoPorId(UUID id) {
        return produtoRepository.findById(id)
                .map(produtoMapper::toResponse)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
    }

    @Override
    public ProdutoResponse atualizarProduto(UUID id, ProdutoRequest request) {
        ProdutoEntity produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));

        validarPrecoVenda(request.getPrecoVenda(), produto.getPrecoCusto());

        produtoMapper.updateEntityFromRequest(request, produto);
        produtoRepository.save(produto);
        return produtoMapper.toResponse(produto);
    }

    @Override
    public ProdutoResponse alterarStatus(UUID id, Boolean ativo) {
        ProdutoEntity produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));

        produto.setAtivo(ativo);
        produtoRepository.save(produto);
        return produtoMapper.toResponse(produto);
    }

    @Override
    @Transactional
    public EntradaEstoqueResponse registarEntrada(EntradaEstoqueRequest entrada) {
        ProdutoEntity produto = produtoRepository.findById(entrada.getProdutoId())
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));

        FornecedorEntity fornecedor = fornecedorRepository.findById(entrada.getFornecedorId())
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado."));

        Integer quantidadeAnterior = produto.getQuantidade();
        BigDecimal precoCustoAnterior = produto.getPrecoCusto();

        // Calcula PMP
        // fórmula: (qtd atual * custo médio atual + qtd entrada * custo entrada) / (qtd atual + qtd entrada)
        BigDecimal estoqueAtualValor = produto.getPrecoCustoMedio() != null
                ? produto.getPrecoCustoMedio().multiply(BigDecimal.valueOf(quantidadeAnterior))
                : precoCustoAnterior.multiply(BigDecimal.valueOf(quantidadeAnterior));

        BigDecimal entradaValor = entrada.getPrecoCusto().multiply(BigDecimal.valueOf(entrada.getQuantidade()));
        Integer novaQuantidade = quantidadeAnterior + entrada.getQuantidade();

        BigDecimal novoPrecoCustoMedio = novaQuantidade > 0
                ? estoqueAtualValor.add(entradaValor)
                .divide(BigDecimal.valueOf(novaQuantidade), 2, RoundingMode.HALF_UP)
                : entrada.getPrecoCusto();

        produto.setQuantidade(novaQuantidade);
        produto.setPrecoCusto(entrada.getPrecoCusto());
        produto.setPrecoCustoMedio(novoPrecoCustoMedio);
        produto.setFornecedor(fornecedor);

        // Calcula preço sugerido
        BigDecimal precoSugerido = calcularPrecoSugerido(novoPrecoCustoMedio, produto.getId());
        produto.setPrecoVenda(precoSugerido);

        validarPrecoVenda(produto.getPrecoVenda(), novoPrecoCustoMedio);

        produtoRepository.save(produto);

        // Registro de movimentação
        MovimentacaoEntity movimentacao = new MovimentacaoEntity();
        movimentacao.setProduto(produto);
        movimentacao.setTipo(TipoMovimentacao.ENTRADA);
        movimentacao.setQuantidade(entrada.getQuantidade());
        movimentacao.setQuantidadeAnterior(quantidadeAnterior);
        movimentacao.setMotivo("Entrada de estoque — Fornecedor: " + fornecedor.getNome());
        movimentacaoRepository.save(movimentacao);

        // Monta e retorna response
        EntradaEstoqueResponse response = new EntradaEstoqueResponse();
        response.setProdutoId(produto.getId());
        response.setNomeProduto(produto.getNome());
        response.setQuantidadeAnterior(quantidadeAnterior);
        response.setQuantidadeAtual(novaQuantidade);
        response.setPrecoCustoAnterior(precoCustoAnterior);
        response.setPrecoCustoMedioAtualizado(novoPrecoCustoMedio);
        response.setPrecoVendaSugerido(precoSugerido);

        return response;
    }

    @Override
    public BigDecimal calcularPrecoSugerido(BigDecimal precoCusto, UUID produtoId) {

        // Busca margem específica do produto
        ProdutoEntity produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));

        BigDecimal margem = produto.getMargemLucro();

        // Se não tiver margem específica, usa a margem padrão das configurações
        if (margem == null) {
            margem = configuracaoRepository.findByChave("margem_padrao")
                    .map(config -> new BigDecimal(config.getValor()))
                    .orElse(BigDecimal.valueOf(30)); // fallback se não existir no banco
        }

        // precoSugerido = precoCusto * (1 + margem / 100)
        return precoCusto.multiply(BigDecimal.ONE.add(
                margem.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        )).setScale(2, RoundingMode.HALF_UP);
    }

    private void validarPrecoVenda(BigDecimal precoVenda, BigDecimal precoCusto) {
        if (precoVenda.compareTo(precoCusto) <= 0) {
            throw new BusinessException(
                    "Preço de venda não pode ser menor que o preço de custo. " +
                            "Custo atual: R$ " + precoCusto
            );
        }
    }
}