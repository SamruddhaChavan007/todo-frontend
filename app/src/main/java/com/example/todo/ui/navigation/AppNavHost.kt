package com.example.todo.ui.navigation

import androidx.compose.runtime.Composable
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
    homeScreen: @Composable () -> Unit, // you can pass your Home/Todo screen here
) {
    val startDestination =
        if (authVm.hasValidSession()) Routes.HOME else Routes.LOGIN

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
                onGoSignup = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                vm = authVm,
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                },
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
