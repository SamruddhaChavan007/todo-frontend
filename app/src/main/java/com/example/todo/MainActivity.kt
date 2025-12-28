package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.ui.auth.AuthViewModel
import com.example.todo.ui.navigation.AppNavHost
import com.example.todo.ui.theme.ToDoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val authVm: AuthViewModel = viewModel()
            val sessionChecked by authVm.sessionChecked.collectAsState()
            splashScreen.setKeepOnScreenCondition {
                !sessionChecked
            }
            ToDoTheme {
                AppNavHost(
                    authVm = authVm,
                    homeScreen = {
                        MainApp()
                    }
                )
            }
        }
    }
}