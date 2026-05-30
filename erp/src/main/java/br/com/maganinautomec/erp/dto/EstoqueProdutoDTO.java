package br.com.maganinautomec.erp.dto;

import br.com.maganinautomec.erp.entity.EstoqueProdutoEntity;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.Objects;

public class EstoqueProdutoDTO {

    private Long id;
    private String nomeProduto;
    private float precoCustoProduto;
    private String fornecedor;
    private int quantidadeEstoque;
    private int quantidadeVendida;
    private LocalDate dataCompra;
    private float precoVendaProduto;

    public EstoqueProdutoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public float getPrecoCustoProduto() {
        return precoCustoProduto;
    }

    public void setPrecoCustoProduto(float precoProduto) {
        this.precoCustoProduto = precoProduto;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public float getPrecoVendaProduto() {
        return precoVendaProduto;
    }

    public void setPrecoVendaProduto(float precoVendaProduto) {
        this.precoVendaProduto = precoVendaProduto;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EstoqueProdutoDTO that = (EstoqueProdutoDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public EstoqueProdutoDTO(EstoqueProdutoEntity estoqueProduto) {
        BeanUtils.copyProperties(estoqueProduto, this);
    }
}
