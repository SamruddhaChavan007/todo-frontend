package com.example.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.auth.AuthViewModel
import com.example.todo.ui.auth.LoginScreen
import com.example.todo.ui.auth.RegisterScreen
import com.example.todo.ui.todos.TodosScreen

@Composable
fun AppNavHost(
    authVm: AuthViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val isLoggedIn by authVm.isLoggedIn.collectAsState()

    // Session check once at startup
    LaunchedEffect(Unit) {
        authVm.checkSession()
    }

    val loggedIn = isLoggedIn ?: return

    val startDestination = if (loggedIn) Routes.HOME else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                vm = authVm,
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onGoSignup = {
                    navController.navigate(Routes.REGISTER) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                vm = authVm,
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        // clears login/register from backstack
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onGoLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            TodosScreen(
                onUnauthorized = {
                    authVm.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}