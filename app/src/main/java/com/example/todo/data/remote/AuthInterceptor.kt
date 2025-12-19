package com.example.todo.data.remote

import com.example.todo.core.TokenStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = runBlocking { tokenStore.tokenFlow.firstOrNull() }

        val req = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) addHeader("X-Token", token)
        }.build()

        return chain.proceed(req)
    }
}