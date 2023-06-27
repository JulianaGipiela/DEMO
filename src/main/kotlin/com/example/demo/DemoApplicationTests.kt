package com.example.demo

import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class DemoApplicationTests {
    @Test
    fun testAddition() {
        val sum = 1 + 1
        assertEquals(2, sum)
    }

}