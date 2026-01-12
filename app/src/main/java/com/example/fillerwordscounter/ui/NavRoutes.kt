package com.example.fillerwordscounter.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val LOADING = "loading"
    const val HOME = "home"
    const val TODAY = "today"
    const val TRENDS = "trends"
}

data class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomTabs = listOf(
    BottomTab(Routes.HOME, "Home", Icons.Filled.Face),
    BottomTab(Routes.TODAY, "Today", Icons.Filled.Today),
    BottomTab(Routes.TRENDS, "Trends", Icons.Filled.ShowChart),
)