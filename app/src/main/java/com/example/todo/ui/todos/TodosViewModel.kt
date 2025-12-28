package com.example.todo.ui.todos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.repo.TodosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val repo: TodosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TodosUiState(isLoading = true))
    val uiState: StateFlow<TodosUiState> = _uiState

    fun refresh() {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(isLoading = true, errorMessage = null, unauthorized = false)

            val result = repo.fetchTodos()
            result.fold(
                onSuccess = { list ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        todos = list,
                        errorMessage = null,
                        unauthorized = false
                    )
                },
                onFailure = { err ->
                    val unauthorized = (err as? HttpException)?.code() == 401
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        todos = emptyList(),
                        errorMessage = if (unauthorized) "Session expired. Please login again." else "Could not load todos. Please try again.",
                        unauthorized = unauthorized
                    )
                }
            )
        }
    }

    fun createTodo(title: String, description: String?, onDone: () -> Unit) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Title cannot be empty.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, errorMessage = null)

            val result = repo.createTodo(title, description)
            result.fold(
                onSuccess = { created ->
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        todos = listOf(created) + _uiState.value.todos,
                        errorMessage = null
                    )
                    onDone()
                },
                onFailure = { err ->
                    val unauthorized = (err as? HttpException)?.code() == 401
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        unauthorized = unauthorized,
                        errorMessage = if (unauthorized) "Session expired. Please login again." else "Failed to create todo. Try again."
                    )
                }
            )
        }
    }
}