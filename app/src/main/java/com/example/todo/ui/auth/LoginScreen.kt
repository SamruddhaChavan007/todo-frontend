package com.example.todo.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.todo.ui.state.UiState

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onSuccess: () -> Unit,
    onGoSignup: () -> Unit
) {
    val state by vm.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = state is UiState.Loading
    val errorText = (state as? UiState.Error)?.message

    LaunchedEffect(state) {
        if (state is UiState.Success) {
            vm.reset()
            onSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    vm.login(email.trim(), password)
                }
            },
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Signing in..." else "Login")
        }

        if (!errorText.isNullOrBlank()) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(10.dp))

        TextButton(
            onClick = onGoSignup
        ) {
            Text(text = "Don't have an account? Sign Up")
        }
    }
}