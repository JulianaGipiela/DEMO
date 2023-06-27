package com.example.demo

import org.junit.Test
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals

class DemoApplicationTests {
	@Test
	fun testPostProduct() {
		withTestApplication({ main() }) {
			val productJson = """
                {
                    "name": "Test Product",
                    "price": 19.99
                }
            """.trimIndent()
			handleRequest(HttpMethod.Post, "/products") {
				setBody(productJson)
				addHeader("Content-Type", "application/json")
			}.apply {
				assertEquals(HttpStatusCode.Created, response.status())
			}
		}
	}

}