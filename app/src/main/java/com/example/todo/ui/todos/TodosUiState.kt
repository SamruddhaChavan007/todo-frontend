package com.example.todo.ui.todos

import com.example.todo.data.remote.dto.TodoDto

data class TodosUiState(
    val isLoading: Boolean = false,
    val todos: List<TodoDto> = emptyList(),
    val errorMessage: String? = null,
    val unauthorized: Boolean = false,
    val isCreating: Boolean = false
)
