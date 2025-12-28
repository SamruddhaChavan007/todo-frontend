package com.example.todo.ui.todos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TodosScreen(
    onUnauthorized: () -> Unit,
    vm: TodosViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.refresh()
    }

    LaunchedEffect(state.unauthorized) {
        if (state.unauthorized) onUnauthorized()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {showAddDialog = true}) {
                Icon(Icons.Filled.Add, contentDescription = "Add ToDo")
            }
        }
    ) { }
}