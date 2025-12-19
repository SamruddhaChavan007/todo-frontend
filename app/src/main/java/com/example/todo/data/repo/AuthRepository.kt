package com.example.todo.data.repo

import com.example.todo.core.TokenStore
import com.example.todo.data.remote.TodoApi
import com.example.todo.data.remote.dto.LoginRequest
import com.example.todo.data.remote.dto.RegisterRequest

class AuthRepository(
    private val tokenStore: TokenStore,
    private val api: TodoApi
) {
    suspend fun register(email: String, password: String) {
        val res = api.register(RegisterRequest(email.trim(), password.trim()))
        tokenStore.saveToken(res.token)
    }

    suspend fun login(email: String, password: String) {
        val res = api.login(LoginRequest(email.trim(), password.trim()))
    }

    suspend fun logout() {
        runCatching { api.logout() }
        tokenStore.clear()
    }
}