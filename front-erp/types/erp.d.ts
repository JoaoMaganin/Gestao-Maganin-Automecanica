declare namespace ERP {

    type EstoqueProduto = {
        id?: number | null;
        nomeProduto: string;
        precoCustoProduto: number;
        fornecedor: string;
        quantidadeEstoque: number;
        quantidadeVendida: number;
        precoVendaProduto: number;
        dataCompra: string;
    }
}