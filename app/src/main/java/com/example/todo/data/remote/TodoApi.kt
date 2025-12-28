package com.example.todo.data.remote

import com.example.todo.data.remote.dto.AuthResponse
import com.example.todo.data.remote.dto.CreateTodoRequest
import com.example.todo.data.remote.dto.ListTodosResponse
import com.example.todo.data.remote.dto.LoginRequest
import com.example.todo.data.remote.dto.RegisterRequest
import com.example.todo.data.remote.dto.TodoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TodoApi {

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout()

    @GET("todos")
    suspend fun listTodos(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): ListTodosResponse

    @POST("todos")
    suspend fun createTodo(
        @Body body: CreateTodoRequest
    ): TodoDto
}