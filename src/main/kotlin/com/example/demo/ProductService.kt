package com.example.demo

class ProductService {
    private val products: MutableList<Product> = mutableListOf()
    private var lastProductId: Int = 0

    // Método para criar um produto
    fun createProduct(product: Product): Product {
        val newProduct = product.copy(id = ++lastProductId)
        products.add(newProduct)
        return newProduct
    }

    // Método para listar todos os produtos
    fun getAllProducts(): List<Product> {
        return products.toList()
    }

    // Método para obter um produto pelo ID
    fun getProductById(id: Int): Product? {
        val product = products.find { it.id == id }
        if (product != null) {
            return product
        } else {
            throw ProductNotFoundException("Product not found for ID: $id")
        }
    }

    // Método para atualizar um produto existente
    fun updateProduct(updatedProduct: Product): Product? {
        val index = products.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            products[index] = updatedProduct
            return updatedProduct
        }
        return null
    }

    // Método para excluir um produto pelo ID
    fun deleteProduct(id: Int): Boolean {
        val product = products.find { it.id == id }
        if (product != null) {
            products.remove(product)
            return true
        }
        return false
    }
}
