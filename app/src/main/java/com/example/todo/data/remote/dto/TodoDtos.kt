package com.example.todo.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val is_done: Boolean,
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class ListTodosResponse(
    val todos: List<TodoDto>,
    val limit: Int,
    val offset: Int
)

@Serializable
data class CreateTodoRequest(
    val title: String,
    val description: String? = null
)
@Serializable
data class CreateTodoResponse(
    val todo: TodoDto
)