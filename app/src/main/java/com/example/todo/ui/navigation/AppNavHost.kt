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

@Composable
fun AppNavHost(
    authVm: AuthViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    homeScreen: @Composable () -> Unit,
) {
    val isLoggedIn by authVm.isLoggedIn.collectAsState()

    LaunchedEffect(Unit) {
        authVm.checkSession()
    }

    if (isLoggedIn == null) return

    LaunchedEffect(isLoggedIn) {
        when (isLoggedIn) {
            true -> {
                navController.navigate(Routes.HOME) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }

            false -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }

            null -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                vm = authVm,
                onSuccess = { },
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
                onSuccess = { },
                onGoLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            homeScreen()
        }
    }
}