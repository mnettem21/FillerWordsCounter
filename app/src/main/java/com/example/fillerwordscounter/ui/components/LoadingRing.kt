package com.example.fillerwordscounter.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AestheticLoadingRing(
    modifier: Modifier = Modifier,
) {
    val infinite = rememberInfiniteTransition(label = "ring")

    val sweep by infinite.animateFloat(
        initialValue = 40f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sweep"
    )

    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Canvas(modifier = modifier.size(44.dp)) {
        val stroke = Stroke(width = 10f, cap = StrokeCap.Round)
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // soft background ring
        drawArc(
            color = Color(0x22FFA24A),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke
        )

        // animated foreground arc
        drawArc(
            color = Color(0xFFFFA24A),
            startAngle = rotation,
            sweepAngle = sweep,
            useCenter = false,
            style = stroke
        )
    }
}