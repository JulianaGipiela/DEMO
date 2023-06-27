package com.example.demo

data class Order(val id: Int, val customerName: String, val products: MutableList<Product>)