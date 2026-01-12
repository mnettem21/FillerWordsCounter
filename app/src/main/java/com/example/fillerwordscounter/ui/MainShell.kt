package com.example.fillerwordscounter.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.fillerwordscounter.ui.theme.AppColors

@Composable
fun MainShell(
    todayVm: TodayViewModel,
    isListening: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = AppColors.Cream,
        bottomBar = {
            NavigationBar(
                containerColor = AppColors.CardAlt,
            ) {
                bottomTabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppColors.Orange,
                            selectedTextColor = AppColors.Orange,
                            indicatorColor = AppColors.Orange.copy(alpha = 0.18f),
                            unselectedIconColor = AppColors.Muted,
                            unselectedTextColor = AppColors.Muted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    isListening = isListening,
                    onStart = onStart,
                    onStop = onStop,
                    onGoToStats = { navController.navigate(Routes.TODAY) } // optional shortcut
                )
            }
            composable(Routes.TODAY) {
                StatsScreen(vm = todayVm)
            }
            composable(Routes.TRENDS) {
                TrendsScreen()
            }
        }
    }
}