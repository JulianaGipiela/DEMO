package com.example.demo

object AuthenticationService {
    private const val AUTH_TOKEN = "my-auth-token"

    fun authenticate(token: String): Boolean {
        return token == AUTH_TOKEN
    }
}
