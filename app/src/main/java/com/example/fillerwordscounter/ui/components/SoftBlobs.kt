package com.example.fillerwordscounter.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.fillerwordscounter.ui.theme.AppColors

@Composable
fun SoftBlobs(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // Big warm blob top-left
        drawCircle(
            color = AppColors.Peach.copy(alpha = 0.35f),
            radius = size.minDimension * 0.45f,
            center = Offset(size.width * 0.15f, size.height * 0.10f)
        )
        // Blue blob right
        drawCircle(
            color = AppColors.SoftBlue2.copy(alpha = 0.35f),
            radius = size.minDimension * 0.40f,
            center = Offset(size.width * 0.92f, size.height * 0.28f)
        )
        // Orange blob bottom
        drawCircle(
            color = AppColors.Orange.copy(alpha = 0.18f),
            radius = size.minDimension * 0.55f,
            center = Offset(size.width * 0.55f, size.height * 1.05f)
        )
    }
}