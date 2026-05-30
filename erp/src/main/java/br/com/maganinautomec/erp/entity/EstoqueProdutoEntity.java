package br.com.maganinautomec.erp.entity;

import br.com.maganinautomec.erp.dto.EstoqueProdutoDTO;
import jakarta.persistence.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="ERP_ESTOQUEPRODUTO")
public class EstoqueProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false)
    private float precoCustoProduto;

    @Column(nullable = false)
    private String fornecedor;

    @Column(nullable = false)
    private int quantidadeEstoque;

    @Column(nullable = false)
    private int quantidadeVendida;

    @Column(nullable = false)
    private LocalDate dataCompra;

    @Column(nullable = false)
    private float precoVendaProduto;

    public EstoqueProdutoEntity() {
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

    public void setPrecoCustoProduto(float precoCustoProduto) {
        this.precoCustoProduto = precoCustoProduto;
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
        EstoqueProdutoEntity that = (EstoqueProdutoEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public EstoqueProdutoEntity(EstoqueProdutoDTO estoqueProduto) {
        BeanUtils.copyProperties(estoqueProduto, this);
    }
}
