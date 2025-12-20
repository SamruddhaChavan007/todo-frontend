package com.example.todo.ui.auth

import com.example.todo.data.repo.AuthRepository
import com.example.todo.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state: StateFlow<UiState<Unit>> = _state

    fun login(email: String, password: String){
        _state.value = UiState.Loading
        viewModelScope.launch{
            runCatching { repo.login(email, password) }
                .onSuccess { _state.value = UiState.Success(Unit) }
                .onFailure { _state.value = UiState.Error(it.message ?: "Login Failed") }
        }
    }

    fun register(email: String, password: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            runCatching { repo.register(email, password) }
                .onSuccess { _state.value = UiState.Success(Unit) }
                .onFailure { _state.value = UiState.Error(it.message ?: "Register failed") }
        }
    }

    fun logout() {
        viewModelScope.launch { repo.logout() }
    }

    fun reset() {
        _state.value = UiState.Idle
    }

    suspend fun hasValidSession(): Boolean {
        return repo.hasValidSession()
    }

}