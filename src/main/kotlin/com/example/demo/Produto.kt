package com.example.demo

class Produto(
    var id: Int,
    var nome: String,
    var descricao: String,
    var preco: Double
) {
    override fun toString(): String {
        return "Produto(id=$id, nome='$nome', descricao='$descricao', preco=$preco)"
    }
}
