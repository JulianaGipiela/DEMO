package com.example.demo

class ProdutoService {
    private val produtos = mutableListOf<Produto>()

    fun criarProduto(produto: Produto) {
        produtos.add(produto)
    }

    fun obterProduto(id: Int): Produto? {
        return produtos.find { it.id == id }
    }

    fun atualizarProduto(produto: Produto) {
        val index = produtos.indexOfFirst { it.id == produto.id }
        if (index != -1) {
            produtos[index] = produto
        }
    }

    fun removerProduto(id: Int) {
        produtos.removeIf { it.id == id }
    }

    fun listarProdutos(): List<Produto> {
        return produtos.toList()
    }


}
