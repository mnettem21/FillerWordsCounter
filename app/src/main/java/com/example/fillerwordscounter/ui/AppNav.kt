package com.example.fillerwordscounter.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNav(
    todayVm: TodayViewModel,
    trendsVm: TrendsViewModel,
    isListening: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.LOADING) {
        composable(Routes.LOADING) {
            LoadingQuotesScreen(
                onDone = {
                    nav.navigate("main") { // you can keep this as a string OR add Routes.MAIN if you want
                        popUpTo(Routes.LOADING) { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainShell(
                todayVm = todayVm,
                trendsVm = trendsVm,
                isListening = isListening,
                onStart = onStart,
                onStop = onStop
            )
        }
    }
}