package com.example.todo.ui.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.todo.R


@Composable
fun AddTodoDialog(
    isCreating: Boolean,
    onDismiss: () -> Unit,
    onCreate: (title: String, description: String?, onSuccess: () -> Unit) -> Unit
) {
    val (title, setTitle) = remember { mutableStateOf("") }
    val (description, setDescription) = remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            if (!isCreating && !showSuccess) onDismiss()
        },
        title = { Text(text = "New ToDo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = setTitle,
                    label = { Text("Title") },
                    enabled = !isCreating,
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = setDescription,
                    label = { Text("Description") },
                    enabled = !isCreating
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = !isCreating && title.trim().isNotEmpty(),
                onClick = {
                    val cleanTitle = title.trim()
                    val cleanDescription = description.trim().ifBlank { null }
                    onCreate(cleanTitle, cleanDescription) {
                        showSuccess = true
                        setTitle("")
                        setDescription("")
                    }
                }
            ) {
                if (isCreating && !showSuccess) {
                    val creatingComposition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.creating)
                    )

                    val creatingProgress by animateLottieCompositionAsState(
                        composition = creatingComposition,
                        iterations = LottieConstants.IterateForever
                    )

                    LottieAnimation(
                        composition = creatingComposition,
                        progress = { creatingProgress },
                        modifier = Modifier.size(36.dp)
                    )

                } else if (showSuccess) {
                    val successComposition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.success)
                    )

                    val successProgress by animateLottieCompositionAsState(
                        composition = successComposition,
                        iterations = 1,
                        isPlaying = true
                    )

                    LottieAnimation(
                        composition = successComposition,
                        progress = { successProgress },
                        modifier = Modifier.size(40.dp)
                    )
                    LaunchedEffect(successProgress) {
                        if (successProgress >= 0.99f) {
                            onDismiss()
                        }
                    }
                } else {
                    Text("Create")
                }
            }
        },
        dismissButton = {
            TextButton(
                enabled = !isCreating,
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}