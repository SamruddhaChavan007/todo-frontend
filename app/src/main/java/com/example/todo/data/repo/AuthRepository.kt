package com.example.todo.data.repo

import com.example.todo.core.TokenStore
import com.example.todo.data.remote.TodoApi
import com.example.todo.data.remote.dto.LoginRequest
import com.example.todo.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: TodoApi,
    private val tokenStore: TokenStore
) {
    suspend fun register(email: String, password: String) {
        val res = api.register(RegisterRequest(email.trim(), password.trim()))
        tokenStore.saveToken(res.token)
    }

    suspend fun login(email: String, password: String) {
        val res = api.login(LoginRequest(email.trim(), password.trim()))
        tokenStore.saveToken(res.token)
    }

    suspend fun logout() {
        runCatching { api.logout() }
        tokenStore.clear()
    }

    suspend fun hasValidSession(): Boolean {
        val token = tokenStore.tokenFlow.first()
            ?: return false

        return try {
            api.logout()
            true
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 401) {
                tokenStore.clear()
                false
            } else {
                true
            }
        }
    }
}