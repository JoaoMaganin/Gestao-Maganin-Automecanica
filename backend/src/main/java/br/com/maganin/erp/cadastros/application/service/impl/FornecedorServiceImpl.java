package br.com.maganin.erp.cadastros.application.service.impl;

import br.com.maganin.erp.cadastros.application.dto.FornecedorResponse;
import br.com.maganin.erp.cadastros.application.dto.FornecedorRequest;
import br.com.maganin.erp.cadastros.application.mapper.FornecedorMapper;
import br.com.maganin.erp.cadastros.application.service.FornecedorService;
import br.com.maganin.erp.cadastros.domain.FornecedorEntity;
import br.com.maganin.erp.cadastros.infrastructure.repository.FornecedorRepository;
import br.com.maganin.erp.shared.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;

    public FornecedorServiceImpl(FornecedorRepository repository, FornecedorMapper mapper) {
        this.fornecedorRepository = repository;
        this.fornecedorMapper = mapper;
    }

    @Override
    public FornecedorResponse criarFornecedor(FornecedorRequest fornecedor) {

        if(fornecedorRepository.existsByNomeIgnoreCase(fornecedor.getNome())) {
            throw new BusinessException("Erro ao cadastrar! Fornecedor " + fornecedor.getNome().toUpperCase() + "já existe");
        }

        FornecedorEntity novoFornecedor = fornecedorMapper.toEntity(fornecedor);
        novoFornecedor.setAtivo(true);
        FornecedorEntity entidadeSalva = fornecedorRepository.save(novoFornecedor);
        return fornecedorMapper.toResponse(entidadeSalva);
    }

    @Override
    public Page<FornecedorResponse> listarFornecedores(String nome, Pageable pageable) {
        Page<FornecedorEntity> page;

        if(nome == null || nome.isBlank()) {
            page = fornecedorRepository.findAll(pageable);
        } else {
            page = fornecedorRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }

        return page.map(fornecedorMapper::toResponse);
    }

    @Override
    public FornecedorResponse buscarFornecedorPorId(UUID id) {
        return fornecedorRepository.findById(id)
                .map(fornecedorMapper::toResponse)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado."));
    }

    @Override
    public FornecedorResponse atualizarFornecedor(UUID id, FornecedorRequest fornecedor) {
        FornecedorEntity fornecedorAtualizado = fornecedorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado."));

        fornecedorMapper.updateEntityFromRequest(fornecedor, fornecedorAtualizado);
        fornecedorRepository.save(fornecedorAtualizado);
        return fornecedorMapper.toResponse(fornecedorAtualizado);
    }

    @Override
    public FornecedorResponse alterarStatusFornecedor(UUID id, Boolean ativo) {
        FornecedorEntity fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado."));

        fornecedor.setAtivo(ativo);
        fornecedorRepository.save(fornecedor);
        return fornecedorMapper.toResponse(fornecedor);
    }
}
