package br.com.maganin.erp.estoque.application.service.Impl;

import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaRequest;
import br.com.maganin.erp.estoque.application.dto.categoria.CategoriaResponse;
import br.com.maganin.erp.estoque.application.mapper.CategoriaMapper;
import br.com.maganin.erp.estoque.application.service.CategoriaService;
import br.com.maganin.erp.estoque.domain.CategoriaEntity;
import br.com.maganin.erp.estoque.infrastructure.repository.CategoriaRepository;
import br.com.maganin.erp.shared.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaServiceImpl(CategoriaRepository repository, CategoriaMapper mapper) {
        this.categoriaRepository = repository;
        this.categoriaMapper = mapper;
    }

    @Override
    public CategoriaResponse criarCategoria(CategoriaRequest categoria) {

        if (categoriaRepository.existsByNomeIgnoreCase(categoria.getNome())) {
            throw new BusinessException("Já existe uma categoria cadastrada com o nome " + categoria.getNome());
        }

        CategoriaEntity novaCategoria = categoriaMapper.toEntity(categoria);
        CategoriaEntity categoriaSalva = categoriaRepository.save(novaCategoria);

        return categoriaMapper.toResponse(categoriaSalva);
    }

    @Override
    public Page<CategoriaResponse> listarCategorias(String nome, Pageable pageable) {
        Page<CategoriaEntity> page;

        if (nome == null || nome.isBlank()) {
            page = categoriaRepository.findAll(pageable);
        } else {
            page = categoriaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }

        // Mapeia cada CategoriaEntity para CategoriaResponse e retorna
        return page.map(categoriaMapper::toResponse);
    }

    @Override
    public CategoriaResponse buscarPorId(UUID id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toResponse) // Se achar, converte para Response
                .orElseThrow(() -> new BusinessException("Categoria não encontrada.")); // Se não achar, lança o erro
    }

    @Override
    public CategoriaResponse atualizarCategoria(UUID id, CategoriaRequest request) {

        CategoriaEntity categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Categoria não encontrada."));

        categoriaMapper.updateEntityFromRequest(request, categoriaExistente);
        categoriaRepository.save(categoriaExistente);
        return categoriaMapper.toResponse(categoriaExistente);
    }

    @Override
    public CategoriaResponse alterarStatus(UUID id, Boolean ativo){

        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Categoria não encontrada."));

        categoria.setAtivo(ativo);
        categoriaRepository.save(categoria);
        return categoriaMapper.toResponse(categoria);
    }
}
