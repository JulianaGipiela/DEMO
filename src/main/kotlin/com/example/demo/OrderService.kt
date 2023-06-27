package com.example.demo

class OrderService(private val productService: ProductService) {
    private val orders: MutableList<Order> = mutableListOf()
    private var lastOrderId: Int = 0

    // Método para criar um pedido
    fun createOrder(order: Order): Order {
        val newOrder = order.copy(id = ++lastOrderId)
        orders.add(newOrder)
        return newOrder
    }

    // Método para adicionar um produto a um pedido
    fun addProductToOrder(orderId: Int, productId: Int): Boolean {
        val order = orders.find { it.id == orderId }
        val product = productService.getProductById(productId)

        if (order != null && product != null) {
            order.products.add(product)
            return true
        }
        return false
    }

    // Método para remover um produto de um pedido
    fun removeProductFromOrder(orderId: Int, productId: Int): Boolean {
        val order = orders.find { it.id == orderId }
        val product = productService.getProductById(productId)

        if (order != null && product != null) {
            order.products.remove(product)
            return true
        }
        return false
    }

    // Método para consultar pedidos com filtragem e ordenação
    fun getOrders(filterByCustomer: String?, sortBy: String?): List<Order> {
        val filteredOrders = if (filterByCustomer != null) {
            orders.filter { it.customerName.contains(filterByCustomer, ignoreCase = true) }
        } else {
            orders.toList()
        }

        return when (sortBy) {
            "customerName" -> filteredOrders.sortedBy { it.customerName }
            "id" -> filteredOrders.sortedBy { it.id }
            else -> filteredOrders
        }
    }
}
