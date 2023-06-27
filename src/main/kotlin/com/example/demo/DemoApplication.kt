package com.example.demo

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DemoApplication

fun main() {
	val logger: Logger = LoggerFactory.getLogger(DemoApplication::class.java)
	val productService = ProductService()
	val orderService = OrderService(productService)

	embeddedServer(Netty, port = 8081) {
		install(ContentNegotiation) {
			jackson {
			}
		}

		install(StatusPages) {
		}

		routing {
			route("/products") {
				post {
					logger.info("Endpoint: /products (POST)")
					val product = call.receive<Product>()
					val createdProduct = productService.createProduct(product)
					call.respond(HttpStatusCode.Created, createdProduct)
				}
				get {
					logger.info("Endpoint: /products (GET)")
					val products = productService.getAllProducts()
					call.respond(HttpStatusCode.OK, products)
				}
				put("/{id}") {
					logger.info("Endpoint: /products/{id} (PUT)")
					val productId = call.parameters["id"]?.toIntOrNull()
					val product = call.receive<Product>()

					if (productId != null) {
						val updatedProduct = product.copy(id = productId)
						val result = productService.updateProduct(updatedProduct)

						if (result != null) {
							call.respond(HttpStatusCode.OK, result)
						} else {
							call.respond(HttpStatusCode.NotFound)
						}
					} else {
						call.respond(HttpStatusCode.BadRequest)
					}
				}
				delete("/{id}") {
					logger.info("Endpoint: /products/{id} (DELETE)")
					val productId = call.parameters["id"]?.toIntOrNull()

					if (productId != null) {
						// Verificar autenticação
						val token = call.request.headers["Authorization"] ?: ""
						val authenticated = AuthenticationService.authenticate(token)

						if (authenticated) {
							val result = productService.deleteProduct(productId)

							if (result) {
								call.respond(HttpStatusCode.NoContent)
							} else {
								call.respond(HttpStatusCode.NotFound)
							}
						} else {
							call.respond(HttpStatusCode.Unauthorized)
						}
					} else {
						call.respond(HttpStatusCode.BadRequest)
					}
				}
			}

			route("/orders") {
				post {
					try {
						logger.info("Endpoint: /orders (POST)")
						val order = call.receive<Order>()
						val createdOrder = orderService.createOrder(order)
						call.respond(HttpStatusCode.Created, createdOrder)
					} catch (e: Exception) {
						logger.error("Error creating order", e)
						call.respond(HttpStatusCode.InternalServerError, "Failed to create order")
					}
				}
				put("/{orderId}/products/{productId}") {
					logger.info("Endpoint: /orders/{orderId}/products/{productId} (PUT)")
					val orderId = call.parameters["orderId"]?.toIntOrNull()
					val productId = call.parameters["productId"]?.toIntOrNull()

					if (orderId != null && productId != null) {
						val result = orderService.addProductToOrder(orderId, productId)

						if (result) {
							call.respond(HttpStatusCode.OK, "Product added to order successfully")
						} else {
							call.respond(HttpStatusCode.NotFound)
						}
					} else {
						call.respond(HttpStatusCode.BadRequest)
					}
				}
				delete("/{orderId}/products/{productId}") {
					val orderId = call.parameters["orderId"]?.toIntOrNull()
					val productId = call.parameters["productId"]?.toIntOrNull()

					if (orderId != null && productId != null) {
						// Verificar autenticação
						val token = call.request.headers["Authorization"] ?: ""
						val authenticated = AuthenticationService.authenticate(token)

						if (authenticated) {
							val result = orderService.removeProductFromOrder(orderId, productId)

							if (result) {
								call.respond(HttpStatusCode.NoContent)
							} else {
								call.respond(HttpStatusCode.NotFound)
							}
						} else {
							call.respond(HttpStatusCode.Unauthorized)
						}
					} else {
						call.respond(HttpStatusCode.BadRequest)
					}
				}
				get {
					val filterByCustomer = call.parameters["customer"]
					val sortBy = call.parameters["sort"]

					val orders = orderService.getOrders(filterByCustomer, sortBy)
					call.respond(HttpStatusCode.OK, orders)
				}
			}
		}
	}.start(wait = true)
}

