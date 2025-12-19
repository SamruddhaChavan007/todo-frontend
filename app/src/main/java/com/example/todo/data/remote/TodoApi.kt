package com.example.todo.data.remote

import com.example.todo.data.remote.dto.AuthResponse
import com.example.todo.data.remote.dto.LoginRequest
import com.example.todo.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface TodoApi {

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout()
}