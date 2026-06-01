package br.com.maganin.erp.estoque.domain;

public enum TipoMovimentacao {
    ENTRADA, // Compra de produtos
    SAIDA, // Saída de produtos
    AJUSTE // Correção manual - inventário, perda
}
