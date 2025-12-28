package com.example.todo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.repo.AuthRepository
import com.example.todo.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state: StateFlow<UiState<Unit>> = _state
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn
    private val _sessionChecked = MutableStateFlow(false)
    val sessionChecked: StateFlow<Boolean> = _sessionChecked

    fun checkSession() {
        viewModelScope.launch {
            _isLoggedIn.value = repo.hasValidSession()
            _sessionChecked.value = true
        }
    }

    fun login(email: String, password: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            runCatching { repo.login(email, password) }
                .onSuccess {
                    _state.value = UiState.Success(Unit)
                    _isLoggedIn.value = true
                }
                .onFailure { _state.value = UiState.Error(it.message ?: "Login Failed") }
        }
    }

    fun register(email: String, password: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            runCatching { repo.register(email, password) }
                .onSuccess {
                    _state.value = UiState.Success(Unit)
                    _isLoggedIn.value = true
                }
                .onFailure { _state.value = UiState.Error(it.message ?: "Register failed") }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _isLoggedIn.value = false
            _state.value = UiState.Idle
        }
    }

    fun reset() {
        _state.value = UiState.Idle
    }
}